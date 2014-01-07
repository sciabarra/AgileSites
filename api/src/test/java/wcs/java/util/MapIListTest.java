package wcs.java.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.junit.Test;

public class MapIListTest {

	private List<String> list(String... args) {
		List<String> ls = new LinkedList<String>();
		for (String arg : args)
			ls.add(arg);
		return ls;
	}

	private Map<String, List<String>> map(String keys, @SuppressWarnings("unchecked") List<String>... lists) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		int n = 0;
		StringTokenizer st = new StringTokenizer(keys, ",");
		while (st.hasMoreTokens())
			map.put(st.nextToken(), lists[n++]);
		// System.out.println(map.toString());
		return map;
	}

	MapIList l0 = new MapIList("l0", new HashMap<String, List<String>>());
	@SuppressWarnings("unchecked")
	MapIList l1 = new MapIList("l1", map("a", list("1")));
	@SuppressWarnings("unchecked")
	MapIList l3 = new MapIList("l3", map("b", list("1", "2", "3")));
	@SuppressWarnings("unchecked")
	MapIList l22 = new MapIList("l22", map("a,b", list("1", "2", "3"),
			list("10", "20")));

	@Test
	public void test0() {
		assertTrue(l0.numColumns() == 1);
		assertTrue(l0.getColumnName(2) == null);
		assertTrue(l0.hasData() == false);
		assertTrue(l0.numRows() == 0);

		try {
			l0.getValue("any-value");
			fail("no exception!");
		} catch (Exception e) {

		}
	}

	@Test
	public void test1() throws NoSuchFieldException {
		assertTrue(l1.numColumns() == 1);
		assertTrue(l1.hasData());

		assertTrue(l1.hasData() == true);
		assertTrue(l1.getValue("a").equals("1"));

		try {
			l1.getValue("b");
			fail("no exception!");
		} catch (Exception e) {
		}
	}

	@Test
	public void test3() throws NoSuchFieldException {
		assertTrue(l3.numRows() == 3);
		assertTrue(l3.numColumns() == 1);
		assertTrue(l3.getValue("b") != null);
		try {
			l3.getValue("a");
			fail("no exception");
		} catch (Exception e) {
		}
	}

	@Test
	public void test22() throws NoSuchFieldException {
		assertTrue(l22.numRows() == 2);
		// System.out.println(l22.numColumns());
		assertTrue(l22.numColumns() == 2);
		assertTrue(l22.hasData());

		assertEquals(l22.getValue("a"), "1");
		assertEquals(l22.getValue("b"), "10");

		try {
			l22.getValue("c");
			fail("no exception");
		} catch (Exception ex) {
		}
		assertTrue(l22.moveTo(2));

		assertEquals(l22.getValue("a"), "2");
		assertEquals(l22.getValue("b"), "20");

		assertEquals(l22.getColumnName(1), "a");
		assertEquals(l22.getColumnName(2), "b");

	}

}
