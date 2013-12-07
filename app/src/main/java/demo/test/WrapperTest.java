package demo.test;
import static wcs.core.Common.*;
import static wcs.core.Log.*;
import wcs.core.Index;
import wcs.core.Log;
import demo.element.Wrapper;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

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
		dump(log);
		assertAttr("render-callelement", "error", "Asset not found");
	}

}
