package agilesites.element;

import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.TestRunnerElement;
import wcs.java.util.Util;
import agilesites.Config;

public class Tester extends TestRunnerElement {

	public static AssetSetup setup() {
		return new CSElement("MyTester", agilesites.element.Tester.class, //
				new SiteEntry("TestRunner", false));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource(Config.site, "tests.txt");
	}

}
