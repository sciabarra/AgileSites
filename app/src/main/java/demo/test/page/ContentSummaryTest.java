package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.TestElement;
import demo.element.page.ContentSummary;

// this test must be run by AgileSites TestRunnerElement
public class ContentSummaryTest extends TestElement {

	Log log = Log.getLog(ContentSummary.class);
	ContentSummary it;

	@Before
	public void setUp() {
		it = new ContentSummary();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		odump(log);
		assertText("h4", "Home");		
		assertTextContains("div div", "This is a SUMMARY.");
	}
}
