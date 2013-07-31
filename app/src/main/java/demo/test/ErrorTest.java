package demo.test;

import demo.element.Error;
import org.springframework.stereotype.Component;
import wcs.java.Env;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestEnv;

import javax.annotation.Resource;

// this test must be run by the test runner
@Component
public class ErrorTest extends TestElement {

    @Resource
	Error error;

	@Before
	public void setUp() {
	}

	@Test
	public void test() {
		
		// parse element in a custom env
        //TODO
        //testEnv.setVar("error", "Hello, world");
		parse(error.apply());

		// check the result
		assertText("#subtitle", "Hello, world");
	}

}
