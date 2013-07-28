package demo.element;

import demo.Config;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.AddIndex;
import wcs.java.util.TestRunnerElement;
import wcs.java.util.Util;

@AddIndex("demo/elements.txt")
public class Tester extends TestRunnerElement {

	public static AssetSetup setup() {
		return new CSElement(137502363004l, "DmTester", demo.element.Tester.class, //
				new SiteEntry(137502363005l, "demo-tester", false, "Demo/DmTester"));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource(Config.site, "tests.txt");
	}
}
