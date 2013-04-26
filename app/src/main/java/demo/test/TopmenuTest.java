package demo.test;

import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestElement;
import demo.element.Topmenu;

// this test must be run by AgileSites TestRunnerElement
public class TopmenuTest extends TestElement {

	Topmenu it;
	
	@Before
	public void setUp() {
		it = new Topmenu();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertText("h1", "Topmenu");
	}

}
