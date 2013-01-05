package agilewcs.tests;

import static java.lang.System.out;
import static wcs.java.util.TestRunnerElement.getEnv;

import COM.FutureTense.Interfaces.ICS;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.util.Util;

public class AssetTest {

	Asset a;
	Env e;
	ICS i;

	String clean(String html) {
		return Jsoup.parse(html).text().trim();
	}

	@Before
	public void setUp() {
		out.println("EnvTest");
		e = getEnv();
		i = e.ics;
	}

	@Test
	public void testPage() {
		a = e.getAsset("Page", 1351276304585l);

		assertEquals(a.getName(), "Home");
		assertEquals(a.getDescription(), "Home Page");
		// out.println("*** start date="+a.getStartDate());
		assertEquals(a.getStartDate(), Util.toDate("2013-01-01 00:00:00"));
		assertEquals(a.getEndDate(), null);
		// out.println("*** file=" + a.getFilename());
		assertEquals(a.getFilename(), "index.html");
		assertEquals(a.getPath(), "/");

		assertEquals(a.getSubtype(), "Blog");

		for (Integer i : a.getRange("SideBlocks")) {
			Long id = a.getId("SideBlocks", i);
			if (i == 1)
				assertTrue(id == 1351276304506L);
			if (i == 2)
				assertTrue(id == 1351276305500L);
		}
	}

	@Test
	public void testArticle() {
		a = e.getAsset("Agile_Article", 1351276304506L);

		assertEquals(a.getSubtype(), "Article");

		// out.println("*** name=" + a.getName());
		// out.println("*** author=" + a.getString("Author"));
		assertEquals(a.getString("Author"), "Michele Sciabarra");

		// out.println("*** Summary=" + a.getString("Summary"));
		assertTrue(a.getString("Summary").indexOf("Welcome") != -1);
	}

}
