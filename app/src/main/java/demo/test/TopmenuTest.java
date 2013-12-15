package demo.test;

import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.Topmenu;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
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
		// TODO: test the results
	}

}
