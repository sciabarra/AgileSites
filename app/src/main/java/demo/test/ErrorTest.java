package demo.test;

import demo.element.Error;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import wcs.java.Env;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestEnv;
import wcs.java.util.TestRunnerElement;

import javax.annotation.Resource;

// this test must be run by the test runner
public class ErrorTest extends TestElement {

	Error error;

	@Before
	public void setUp() {
	}

	@Test
	public void test() {

		// parse element in a custom env
        //TODO
        env().setVar("error", "Hello, world");
		parse(error.apply(env("")));

		// check the result
		assertText("#subtitle", "Hello, world");
	}

}
