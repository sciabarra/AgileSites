package demo.test;
import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.Wrapper;

// this test must be run by the test runner
@Index("demo/tests.txt")
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
		//dump(log);
		assertAttr("render-callelement", "error", "Asset not found");
	}

}
