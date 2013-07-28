package demo.test;

import org.junit.Before;
import org.junit.Test;

import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.Topmenu;

// this test must be run by AgileSites TestRunnerElement
@AddIndex("demo/tests.txt")
public class TopmenuTest extends TestElement {

	final static Log log = Log.getLog(TopmenuTest.class);

	Topmenu it;
	
	@Before
	public void setUp() {
		it = new Topmenu();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		odump(log);
		// TODO: test the results
	}

}
