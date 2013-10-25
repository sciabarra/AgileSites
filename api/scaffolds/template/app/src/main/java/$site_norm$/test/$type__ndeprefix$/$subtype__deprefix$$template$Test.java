package $site;format="normalize"$.test.$type;format="normalize"$;

import static wcs.core.Common.*;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$$template$;

// this test must be run by AgileSites TestRunnerElement
@AddIndex("$site;format="normalize"$/tests.txt")
public class $subtype;format="deprefix"$$template$Test extends TestElement {

	final static Log log = Log.getLog($subtype;format="deprefix"$$template$Test.class);
	$subtype;format="deprefix"$$template$ it;
	
	@Before
	public void setUp() {
		it = new $subtype;format="deprefix"$$template$();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertText("h1", "$subtype$$template$");
	}
}
