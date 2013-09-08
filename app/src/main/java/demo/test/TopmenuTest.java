package demo.test;

import org.junit.Before;
import org.junit.Test;

import org.springframework.stereotype.Component;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.Topmenu;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
public class TopmenuTest extends TestElement {

	final static Log log = Log.getLog(TopmenuTest.class);

	Topmenu topmenu;
	
	@Before
	public void setUp() {
        topmenu = new Topmenu();
	}

	@Test
	public void test() {
		parse(topmenu.apply(env("")));
		odump(log);
		// TODO: test the results
	}

}
