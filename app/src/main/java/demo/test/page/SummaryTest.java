package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import demo.element.page.Summary;

// this test must be run by AgileSites TestRunnerElement
@AddIndex("demo/tests.txt")
public class SummaryTest extends TestElement {

	final static Log log = Log.getLog(SummaryTest.class);
	Summary it;
	
	@Before
	public void setUp() {
		it = new Summary();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		odump(log);
		assertText("h4", "Home");		
		assertTextContains("div div", "This is a SUMMARY.");
	}
}
