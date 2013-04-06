package agilesites.test;

import static java.lang.System.out;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.util.TestElement;
import wcs.java.util.Util;
import COM.FutureTense.Interfaces.ICS;

public class AssetTest extends TestElement {

	Asset a;
	Env e;
	ICS i;

	String clean(String html) {
		return Jsoup.parse(html).text().trim();
	}

	@Before
	public void setUp() {
		out.println("EnvTest");
		e = env();
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

		// out.println("*** Range(AssociatedItems):"+a.getAssocRange("AssociatedItems"));
		for (int i : a.getAssocRange("AssociatedItems")) {
			String s = a.getAssocType("AssociatedItems", i) + ":"
					+ a.getAssocId("AssociatedItems", i);
			if (i == 1)
				assertEquals(s, "Agile_Blog:1351276304546");
			if (i == 2)
				assertEquals(s, "Agile_Article:1351276304506");
			// out.println(s);
		}

		// out.println()
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

	@Test
	public void testBlobUrl() {
		a = e.getAsset("Agile_Image", 1351276306919L);
		out.println(a.getId("Blob"));
		String url = a.getBlobUrl("Blob", "image/jpeg");
		assertTrue(url.startsWith("/cs/BlobServer"));
		assertTrue(url.indexOf("blobtable=MungoBlobs") != -1);
		assertTrue(url.indexOf("blobcol=urldata") != -1);
		assertTrue(url.indexOf("blobkey=id") != -1);
		assertTrue(url.indexOf("blobwhere=") != -1);
		assertTrue(url.indexOf("blobheader=image%2Fjpeg") != -1);
	}

}
