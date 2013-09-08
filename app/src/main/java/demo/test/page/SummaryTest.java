package demo.test.page;

import org.junit.Before;
import org.junit.Test;

import org.springframework.stereotype.Component;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.page.Summary;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
public class SummaryTest extends TestElement {

	final static Log log = Log.getLog(SummaryTest.class);

    Summary summary;
	
	@Before
	public void setUp() {
        summary = new Summary();
	}

	@Test
	public void test() {
		parse(summary.apply(env("")));
		odump(log);
		assertText("h4", "Home");		
		assertTextContains("div div", "This is a SUMMARY.");
	}
}
