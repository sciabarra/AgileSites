package $site;format="normalize"$.test.$type;format="normalize"$;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.$type;format="normalize"$.$subtype$$template$;

// this test must be run by AgileSites TestRunnerElement
public class $subtype$$template$Test extends TestElement {

	Log log = Log.getLog($site;format="normalize"$.element.$type;format="normalize"$.$subtype$$template$.class);
	$subtype$$template$ it;
	
	@Before
	public void setUp() {
		it = new $subtype$$template$();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertText("h1", "$subtype$$template$");
	}
}
