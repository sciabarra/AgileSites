package agilesites.tests;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static wcs.java.util.TestRunnerElement.getEnv;

import org.junit.Before;
import org.junit.Test;

import wcs.java.Env;
import wcs.java.util.Util;
import wcs.java.tag.ListobjectTag;
import COM.FutureTense.Interfaces.ICS;

//import wcs.java.ListobjectTag1;

public class EnvTest {

	Env e;
	ICS i;

	@Before
	public void setUp() {
		out.println("EnvTest");
		e = getEnv();
		i = e.ics;

	}

	@Test
	public void testVar() {
		i.SetVar("c", "Page");
		i.SetVar("cid", "1");
		i.SetVar("d", "2001-01-01 01:02:03");

		assertEquals(e.getString("c"), "Page");
		assertEquals(e.getDate("d"), Util.toDate("2001-01-01 01:02:03"));
		assertEquals(e.getC(), "Page");
		assertEquals(e.getCid(), new Long(1));
	}

	@Test
	public void testList() {

		ListobjectTag.create().name("l").columns("a,b,c").run(i);
		ListobjectTag.addrow().name("l").set("a", "1").set("b", "2")
				.set("c", "3").run(i);
		ListobjectTag.addrow().name("l").set("a", "11").set("b", "22")
				.set("c", "33").run(i);
		ListobjectTag.addrow().name("l").set("a", "111").set("b", "222")
				.set("c", "333").run(i);
		ListobjectTag.tolist().name("l").listvarname("l").run(i);

		assertTrue(e.getSize("l") == 3);
		assertTrue(e.getInt("l", "a") == 1);
		assertTrue(e.getInt("l", "b") == 2);
		assertTrue(e.getInt("l", "c") == 3);

		assertTrue(e.getInt("l", 1, "a") == 1);
		assertTrue(e.getInt("l", 1, "b") == 2);
		assertTrue(e.getInt("l", 1, "c") == 3);

		assertTrue(e.getInt("l", 2, "a") == 11);
		assertTrue(e.getInt("l", 2, "b") == 22);
		assertTrue(e.getInt("l", 2, "c") == 33);

		assertTrue(e.getInt("l", 3, "a") == 111);
		assertTrue(e.getInt("l", 3, "b") == 222);
		assertTrue(e.getInt("l", 3, "c") == 333);

	}

	@Test
	public void testLoop() {

		ListobjectTag.create().name("ll").columns("a,b").run(i);
		ListobjectTag.addrow().name("ll").set("a", "1").set("b", "2").run(i);
		ListobjectTag.addrow().name("ll").set("a", "11").set("b", "22").run(i);
		ListobjectTag.tolist().name("ll").listvarname("ll").run(i);

		for (Integer i : e.getRange("ll")) {

			out.println("loop " + i);

			if (i == 1) {
				assertTrue(e.getInt("ll", i, "a") == 1);
				assertTrue(e.getInt("ll", i, "b") == 2);
			}
			if (i == 2) {
				assertTrue(e.getInt("ll", i, "a") == 11);
				assertTrue(e.getInt("ll", i, "b") == 22);
			}

		}
	}

}
