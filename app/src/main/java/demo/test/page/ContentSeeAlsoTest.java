package demo.test.page;

import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.page.ContentSeeAlso;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
public class ContentSeeAlsoTest extends TestElement {

	final static Log log = Log.getLog(ContentSeeAlsoTest.class);
	ContentSeeAlso it;

	@Before
	public void setUp() {
		it = new ContentSeeAlso();
	}

	@Test
	public void testHome() {
		parse(it.apply(env("")));
		//dump(log);
		assertText("h6", "Home");
		assertTextContains("p", "This is a SUMMARY.");
	}

	@Test
	public void testAboutLink() {
		parse(it.apply(env("/About")));
		assertAttrContains("a", "href", "/About");
	}
}
