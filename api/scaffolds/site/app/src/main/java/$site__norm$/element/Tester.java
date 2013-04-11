package $site;format="normalize"$.element;

import $site;format="normalize"$.Config;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.TestRunnerElement;
import wcs.java.util.Util;

public class Tester extends TestRunnerElement {

	public static AssetSetup setup() {
		return new CSElement("$prefix$Tester", $site;format="normalize"$.element.Tester.class, //
				new SiteEntry("$prefix$Tester", false));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource(Config.site, "tests.txt");
	}
}
