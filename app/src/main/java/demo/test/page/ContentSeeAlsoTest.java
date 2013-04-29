package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.TestElement;
import demo.element.page.ContentSeeAlso;

// this test must be run by AgileSites TestRunnerElement
public class ContentSeeAlsoTest extends TestElement {

	final static Log log = Log.getLog(ContentSeeAlsoTest.class);
	ContentSeeAlso it;

	@Before
	public void setUp() {
		it = new ContentSeeAlso();
	}

	@Test
	public void test() {
		// dump(log);
		parse(it.apply(env("")));
		assertText("#seealso-title1", "Home");
		assertTextContains("div", "This is a SUMMARY.");
	}
}
