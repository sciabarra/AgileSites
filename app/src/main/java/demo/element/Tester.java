package demo.element;

import demo.Config;
import org.springframework.stereotype.Component;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.TestRunnerElement;
import wcs.java.util.Util;


@Component
public class Tester extends TestRunnerElement {

	public static AssetSetup setup() {
		return new CSElement("DmTester", demo.element.Tester.class, //
				new SiteEntry("demo-tester", false, "Demo/DmTester"));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource(Config.site, "tests.txt");
	}
}
