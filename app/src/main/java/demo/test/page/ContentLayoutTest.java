package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import org.springframework.stereotype.Component;
import wcs.java.Env;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.page.ContentLayout;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
@Component
public class ContentLayoutTest extends TestElement {
	
	final static Log log = Log.getLog(ContentLayoutTest.class);

    @Resource
    ContentLayout contentLayout;

	@Before
	public void setUp() {
	}

	@Test
	public void test() {
		
		//Env e = env(""); // route the home page
		parse(contentLayout.apply());
		odump(log);

		assertText("#title", "Home");
		assertText("#subtitle", "Home Page");
		assertText("#teaser-title1", "First Teaser");
		assertText("#teaser-body1", "First Teaser Text");
		assertText("#teaser-title2", "Second Teaser");
		assertText("#teaser-body2", "Second Teaser Text");
	}
}
