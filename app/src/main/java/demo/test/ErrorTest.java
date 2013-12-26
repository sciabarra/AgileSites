package demo.test;
import static wcs.Api.*;
import wcs.api.*;
import wcs.java.util.TestElement;
import org.junit.Before;
import demo.element.Error;
import org.junit.Test;

// this test must be run by the test runner
@Index("demo/tests.txt")
public class ErrorTest extends TestElement {

	Error it;

	@Before
	public void setUp() {
		it = new Error();
	}

	@Test
	public void test() {

		// parse element in a custom env
		parse(it.apply(env(arg("error", "Hello, world"))));

		// check the result
		assertText("#detail p", "Hello, world");
	}

}
