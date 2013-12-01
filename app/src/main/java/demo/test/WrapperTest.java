package demo.test;

import demo.element.Wrapper;
import wcs.core.Log;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

// this test must be run by the test runner
@AddIndex("demo/tests.txt")
public class WrapperTest extends TestElement {

	final static Log log = Log.getLog(WrapperTest.class);

	Wrapper it;

	@Before
	public void setUp() {
		it = new Wrapper();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		odump(log);
		assertAttr("render-callelement", "error", "Asset not found");
	}

}
