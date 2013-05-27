package $site;format="normalize"$.test.$type;format="normalize"$;

import static wcs.core.Common.*;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.$type;format="normalize"$.$subtype$$template$;

// this test must be run by AgileSites TestRunnerElement
public class $subtype$$template$Test extends TestElement {

	final static Log log = Log.getLog($subtype$$template$Test.class);
	$subtype$$template$ it;
	
	@Before
	public void setUp() {
		it = new $subtype$$template$();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertText("h1", "$prefix$$subtype$$template$");
	}
}
