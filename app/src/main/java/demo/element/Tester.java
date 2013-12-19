package demo.element;

import wcs.api.Index;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.TestRunnerElement;
import wcs.java.util.Util;
import demo.Config;

@Index("demo/elements.txt")
public class Tester extends TestRunnerElement {

	public static AssetSetup setup() {
		return new CSElement("Tester", demo.element.Tester.class, //
				new SiteEntry("Tester", false));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource(Config.site, "tests.txt");
	}
}
