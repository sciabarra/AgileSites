package demo.test;

import demo.element.Wrapper;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

// this test must be run by the test runner
public class WrapperTest extends TestElement {

	Wrapper wrapper;

	@Before
	public void setUp() {
        wrapper = new Wrapper();
	}

	@Test
	public void test() {
		parse(wrapper.apply(env("")));
		assertAttr("render-callelement", "error", "Asset not found");
	}

}
