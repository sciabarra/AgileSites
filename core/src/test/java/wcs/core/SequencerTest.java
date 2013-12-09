package wcs.core;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static wcs.Api.arg;
import static wcs.Api.call;

import org.junit.Before;
import org.junit.Test;

import wcs.api.Call;

public class SequencerTest {

	private String stringOnly;
	private String simpleCall;
	private String multiCall;
	private String callOnly;

	@Before
	public void init() {
		
		stringOnly = "hello";

		callOnly = call("test", arg("a", "1"));

		simpleCall = "before" + call("hello");

		multiCall = "Element:"
				+ call("element") //
				+ "Template:"
				+ call("template", arg("a", "1")) //
				+ "Insite:" + call("insite", arg("a", "1"), arg("b", "2"))
				+ "End";
	}

	@Test
	public void stringOnly() {
		// dump(stringOnly);
		Sequencer seq = new Sequencer(stringOnly);
		assertFalse(seq.hasNext());
		assertEquals(seq.header(), "hello");
	}

	@Test
	public void simpleCall() {
		// dump(simpleCall);
		Sequencer seq = new Sequencer(simpleCall);
		assertEquals(seq.header(), "before");
		assertTrue(seq.hasNext());
		assertEquals(seq.next().toString(), "<hello/>");
		assertFalse(seq.hasNext());
		assertEquals(seq.header(), "");
	}

	// assertEquals(seq.header(), "Element:");

	// assertTrue(seq.hashCode())

	@Test
	public void multiCall() {
		// dump(multiCall);

		Sequencer seq = new Sequencer(multiCall);

		assertEquals(seq.header(), "Element:");
		assertTrue(seq.hasNext());
		Call call = seq.next();
		assertEquals(call.toString(), "<element/>");
		assertNull(call.getOnce("a"));
		assertEquals(call.toString(), "<element/>");

		assertEquals(seq.header(), "Template:");
		assertTrue(seq.hasNext());
		call = seq.next();
		assertEquals(call.toString(), "<template a=\"1\"/>");
		assertEquals(call.getOnce("a"), "1");
		assertNull(call.getOnce("a"));
		assertEquals(call.toString(), "<template/>");

		assertEquals(seq.header(), "Insite:");
		assertTrue(seq.hasNext());
		call = seq.next();
		assertEquals(call.toString(), "<insite a=\"1\" b=\"2\"/>");
		assertEquals(call.getOnce("a"), "1");
		assertNull(call.getOnce("a"));
		assertEquals(call.toString(), "<insite b=\"2\"/>");

		assertFalse(seq.hasNext());
		assertEquals(seq.header(), "End");

	}

	@Test
	public void callOnly() {
		// dump(callOnly);
		Sequencer seq = new Sequencer(callOnly);
		assertEquals(seq.header(), "");
		assertTrue(seq.hasNext());
		assertEquals(seq.next().toString(), "<test a=\"1\"/>");
		assertEquals(seq.header(), "");
		assertFalse(seq.hasNext());
	}

	public void dump(String sequence) {
		Sequencer seq = new Sequencer(sequence);
		out.println("STRING#" + seq.header().length() + ":" + seq.header());
		while (seq.hasNext()) {
			Call call = seq.next();
			out.println("CALL:" + call.toString());
			// out.print(" a=" + call.getOnce("a") + " ");
			// out.println(call.toString());
			out.println("STRING#" + seq.header().length() + ":" + seq.header());
		}
	}
}
