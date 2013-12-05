package wcs.core;

import static org.junit.Assert.assertEquals;
import static wcs.core.Common.arg;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wcs.core.Arg;
import wcs.core.Content;
import wcs.core.Picker;

public class PickerTest {

	class TestContent implements Content {

		HashMap<String, List<String>> map = new HashMap<String, List<String>>();

		public TestContent(Arg... args) {
			for (Arg arg : args) {
				List<String> list = new LinkedList<String>();
				list.add(arg.value);
				map.put(arg.name, list);
			}
		}

		@Override
		public String getString(String attribute) {
			return getString(attribute, 1);
		}

		@Override
		public String getString(String attribute, int n) {
			List<String> l = map.get(attribute);
			if (l != null && l.size() >= n)
				return l.get(n - 1);
			return null;
		}

		@Override
		public boolean exists(String attribute) {
			return getString(attribute) != null;
		}

		@Override
		public boolean exists(String attribute, int pos) {
			return getString(attribute, pos) != null;
		}

		// the rest is irrelevant

		@Override
		public Long getLong(String var, int n) {
			return null;
		}

		@Override
		public Long getLong(String var) {
			return null;
		}

		@Override
		public Integer getInt(String attribute, int n) {
			return null;
		}

		@Override
		public Integer getInt(String attribute) {
			return null;
		}

		@Override
		public Date getDate(String attribute, int n) {
			return null;
		}

		@Override
		public Date getDate(String attribute) {
			return null;
		}

	};

	Picker none = Picker.create("None");
	Picker one1 = Picker.create("{{First}} Some");
	Picker one2 = Picker.create("Some {{First}}");
	Picker one3 = Picker.create("Some {{First}} Else");
	Picker two = Picker.create("Some {{First}} then {{Second}}");
	Picker two2 = Picker
			.create("Some {{First}} then {{Second}} then {{First}}");

	Content c0 = new TestContent();
	Content c1 = new TestContent(arg("First", "[first]"));
	Content c2 = new TestContent(arg("Second", "[second]"));
	Content c3 = new TestContent(arg("First", "[firstbis]"), arg("Second",
			"[secondbis]"));

	private String log(String s) {
		s = s.replaceAll("(?s).*<body>((?s).*)</body>(?s).*", "$1").trim();
		//System.out.println(s);
		return s;
	}

	@Test
	public void testReplace() {
		assertEquals(log(none.html(c1)), "None");
		System.out.println("First="+c1.getString("First"));
		System.out.println("First="+c1.exists("First"));
		assertEquals(log(one1.html(c1)), "[first] Some");
		assertEquals(log(one2.html(c1)), "Some [first]");
		assertEquals(log(one3.html(c1)), "Some [first] Else");
		assertEquals(log(one1.html(c0)), "{{First}} Some");
		assertEquals(log(one2.html(c0)), "Some {{First}}");
		assertEquals(log(one3.html(c0)), "Some {{First}} Else");
		assertEquals(log(two.html(c0)), "Some {{First}} then {{Second}}");
		assertEquals(log(two.html(c1)), "Some [first] then {{Second}}");
		assertEquals(log(two.html(c0, c1)), "Some [first] then {{Second}}");
		assertEquals(log(two.html(c0, c1, c2)), "Some [first] then [second]");
		assertEquals(log(two.html(c0, c1, c2, c3)), "Some [first] then [second]");
		assertEquals(log(two.html(c2, c1, c0)), "Some [first] then [second]");
		assertEquals(log(two.html(c3, c2, c1, c0)), "Some [firstbis] then [secondbis]");
		assertEquals(log(two2.html(c0)), "Some {{First}} then {{Second}} then {{First}}");
		assertEquals(log(two2.html(c1)), "Some [first] then {{Second}} then [first]");
		assertEquals(log(two2.html(c0, c1)), "Some [first] then {{Second}} then [first]");
		assertEquals(log(two2.html(c0, c1, c2)), "Some [first] then [second] then [first]");
		assertEquals(log(two2.html(c0, c1, c2, c3)), "Some [first] then [second] then [first]");
		assertEquals(log(two2.html(c1, c2, c0)), "Some [first] then [second] then [first]");
		assertEquals(log(two2.html(c3, c2, c1, c0)), "Some [firstbis] then [secondbis] then [firstbis]");
		assertEquals(log(two2.html(c3)), "Some [firstbis] then [secondbis] then [firstbis]");
	}
}
