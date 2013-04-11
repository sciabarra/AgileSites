package demo.test;

import demo.element.Wrapper;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

// this test must be run by the test runner
public class WrapperTest extends TestElement {

	Wrapper it;

	@Before
	public void setUp() {
		it = new Wrapper();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertAttr("render-callelement", "error", "Asset not found");
	}

}
