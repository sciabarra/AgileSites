package demo.test;

import demo.element.Error;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

// this test must be run by the test runner
@AddIndex("demo/tests.txt")
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
