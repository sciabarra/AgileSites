package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Asset;
import wcs.java.util.TestElement;
import demo.element.page.ContentLayout;
import static wcs.core.Common.arg;

// this test must be run by AgileSites TestRunnerElement
public class ContentLayoutTest extends TestElement {
	
	Log log = Log.getLog(ContentLayoutTest.class);
	ContentLayout it;
	
	@Before
	public void setUp() {
		it = new ContentLayout();
	}

	@Test
	public void test() {
		Env e = env(""); // route the home page
		parse(it.apply(e));
		//dump(log);
		//Asset a = e.findOne("Page", arg("name", "Home"));
		assertText("#title", "Home");
		assertText("#subtitle", "Home Page");
		assertText("#teaser-title1", "First Teaser");
		assertText("#teaser-body1", "First Teaser Text");
		assertText("#teaser-title2", "Second Teaser");
		assertText("#teaser-body2", "Second Teaser Text");
	}
}
