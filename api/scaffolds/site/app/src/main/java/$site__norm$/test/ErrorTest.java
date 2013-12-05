package $site;format="normalize"$.test;

import static wcs.core.Common.arg;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.Error;


// this test must be run by the test runner
@AddIndex("$site;format="normalize"$/tests.txt")
public class ErrorTest extends TestElement {
	
	Error it;

	@Before
	public void setUp() {
		it = new Error();
	}

	@Test
	public void test() {
		
		// parse element in a custom env
		parse(it.apply(env(arg("error","Hello, world"))));

		// check the result
		assertText("#header h1", "Hello, world");
		assertText("#detail p", "Hello, world");
		
	}

}
