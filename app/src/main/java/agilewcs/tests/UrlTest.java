package agilewcs.tests;

import static java.lang.System.out;
import static org.junit.Assert.assertTrue;
import static wcs.java.util.TestRunnerElement.getEnv;

import org.jsoup.Jsoup;
import org.junit.Before;
import org.junit.Test;

import wcs.java.Asset;
import wcs.java.Env;
import COM.FutureTense.Interfaces.ICS;

public class UrlTest {

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

	//@Test
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

	@Test
	public void testUrl() {
		a = e.getAsset("Page", 1351276304585l);
		String url = a.getUrl();
		out.println(url);

		a = e.getAsset("Agile_Article", 1351276304506L);
		url = a.getUrl();
		out.println(url);
	}

}
