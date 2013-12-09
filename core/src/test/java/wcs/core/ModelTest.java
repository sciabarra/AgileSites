package wcs.core;

import static wcs.Api.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import wcs.api.Model;

public class ModelTest {

	@Test
	public void testEmptyModel() {
		Model m = new Model();
		assertFalse(m.exists("hello"));
		assertFalse(m.exists("hello", 1));
		assertFalse(m.exists("hello", 2));
		assertNull(m.getString("hello"));
		assertNull(m.getInt("hello"));
		assertNull(m.getDate("hello"));
	}

	@Test
	public void testNewModel() {

		Model m = model(arg("a", "1"));
		System.out.println(m.dump());
		Model n = model(m, args("b", "10", "20"));
		System.out.println(n.dump());

		assertTrue(m.exists("a"));
		assertTrue(m.exists("a", 1));
		assertFalse(m.exists("a", 0));
		assertFalse(m.exists("a", 2));
		assertFalse(m.exists("b"));

		assertTrue(n.exists("a"));
		assertTrue(n.exists("a", 1));
		assertFalse(n.exists("a", 0));
		assertFalse(n.exists("a", 2));
		assertEquals(n.getString("a"), "1");
		assertEquals(n.getString("a", 1), "1");
		assertEquals(n.getInt("a"), new Integer(1));
		assertEquals(n.getInt("a", 1), new Integer(1));
		assertEquals(n.getLong("a"), new Long(1));
		assertEquals(n.getLong("a", 1), new Long(1));

		assertTrue(n.exists("b"));
		assertTrue(n.exists("b", 1));
		assertTrue(n.exists("b", 2));
		assertFalse(n.exists("b", 0));
		assertFalse(n.exists("b", 3));

		assertEquals(n.getSize("a"), 1);
		assertEquals(n.getSize("b"), 2);
		assertEquals(n.getSize("c"), 0);
		assertEquals(m.getSize("b"), 0);

		for (int i : n.getRange("a")) {
			switch (i) {
			case 1:
				assertEquals(n.getString("a", i), "1");
				break;
			default:
				fail("should not happen");
			}
		}

		for (int i : n.getRange("b")) {
			switch (i) {
			case 1:
				assertEquals(n.getString("b", i), "10");
				break;
			case 2:
				assertEquals(n.getString("b", i), "20");
				break;
			default:
				fail("should not happen");
			}
		}
	}

}
