package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import org.springframework.stereotype.Component;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.page.ContentSeeAlso;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
@Component
public class ContentSeeAlsoTest extends TestElement {

	final static Log log = Log.getLog(ContentSeeAlsoTest.class);

    @Resource
    ContentSeeAlso contentSeeAlso;

	@Before
	public void setUp() {
        contentSeeAlso = new ContentSeeAlso();
	}

	@Test
	public void testHome() {
		parse(contentSeeAlso.apply());
		assertText("h6", "Home");
		assertTextContains("div", "This is a SUMMARY.");
	}

	@Test
	public void testAboutLink() {
		//parse(it.apply(env("/About")));
        parse(contentSeeAlso.apply());
		assertAttrContains("a", "href", "/About");
	}
}
