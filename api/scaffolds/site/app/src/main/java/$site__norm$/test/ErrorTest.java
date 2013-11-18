package $site;format="normalize"$.test;

import $site;format="normalize"$.element.Error;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.AddIndex;

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
		parse(it.apply(env().setVar("error", "Hello, world")));

		// check the result
		assertText("#subtitle", "Hello, world");
	}

}
