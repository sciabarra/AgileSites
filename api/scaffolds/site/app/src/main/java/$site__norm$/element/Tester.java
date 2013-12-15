package $site;format="normalize"$.element;
import wcs.api.Index;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.util.Util;
import wcs.java.util.TestRunnerElement;

@Index("$site;format="normalize"$/elements.txt")
public class Tester extends TestRunnerElement {
	public static AssetSetup setup() {
		return new CSElement("Tester", $site;format="normalize"$.element.Tester.class, //
				new SiteEntry("Tester", false));
	}

	@Override
	public Class<?>[] tests() {
		// all the tests of the suite
		return Util.classesFromResource("$site$", "tests.txt");
	}
}
