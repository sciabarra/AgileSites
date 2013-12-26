package demo.test.page;

import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.page.Summary;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
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
		dump(log);
		assertText("h4", "Home");
		assertTextContains("p", "This is a SUMMARY.");
	}
}
