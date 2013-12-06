package $site;format="normalize"$.test.$type;format="normalize"$;

import $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$Layout;
import static wcs.core.Common.*;
import org.junit.Before;
import org.junit.Test;
import wcs.core.Index;
import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Asset;
import wcs.java.util.TestElement;

// this test must be run by AgileSites TestRunnerElement
@Index("$site;format="normalize"$/tests.txt")
public class $subtype;format="deprefix"$LayoutTest extends TestElement {
	final static Log log = Log.getLog($subtype;format="deprefix"$LayoutTest.class); 
	$subtype;format="deprefix"$Layout it;
	
	@Before
	public void setUp() {
		it = new $subtype;format="deprefix"$Layout();
	}

	@Test
	public void test() {
		parse(it.apply(env("/Home")));
		// dump(log) 
		assertText("#header h1", "Home Page Title");
	}
}
