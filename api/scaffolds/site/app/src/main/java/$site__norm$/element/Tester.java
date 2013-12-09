package $site;format="normalize"$.element;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.Util;
import wcs.java.util.TestRunnerElement;
import $site;format="normalize"$.Config;

@Index("$site;format="normalize"$/elements.txt")
public class Tester extends TestRunnerElement {
	public static AssetSetup setup() {
		return new CSElement("$site$_Tester", $site;format="normalize"$.element.Tester.class, //
				new SiteEntry("$site;format="normalize"$-tester", false, "$site$/$site$_Tester"));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource("$site$", "tests.txt");
	}
}
