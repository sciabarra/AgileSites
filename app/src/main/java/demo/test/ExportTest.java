package demo.test;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.util.TestElement;
import demo.element.Export;

// this test must be run by AgileSites TestRunnerElement
public class ExportTest extends TestElement {

	final static Log log = Log.getLog(ExportTest.class);

	Export it;
	
	@Before
	public void setUp() {
		it = new Export();
	}

	@Test
	public void test() {
		log.trace("testing Export");
		parse(it.apply(env()));
		assertText("h1", "Export");
	}

}
