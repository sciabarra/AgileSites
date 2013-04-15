package $site;format="normalize"$.test.$type;format="normalize"$;

import org.junit.Before;
import org.junit.Test;
import wcs.java.Env;
import wcs.java.Asset;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.$type;format="normalize"$.$subtype$Layout;
import static wcs.core.Common.arg;

// this test must be run by AgileSites TestRunnerElement
public class $subtype$LayoutTest extends TestElement {
	
	$subtype$Layout it;
	
	@Before
	public void setUp() {
		it = new $subtype$Layout();
	}

	@Test
	public void test() {
		Env e = env();
		parse(it.apply(e));
		Asset a = e.findOne("Page", arg("name", "Home"));
		assertText("#title", a.getName());
		assertText("#subtitle", a.getDescription());
	}
}
