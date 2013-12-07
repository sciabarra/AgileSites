package demo.test;
import static wcs.core.Common.*;
import static wcs.core.Log.*;
import wcs.core.Index;
import wcs.core.Log;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestElement;
import demo.element.Topmenu;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
public class TopmenuTest extends TestElement {

	final static Log log = Log.getLog(TopmenuTest.class);

	Topmenu it;
	
	@Before
	public void setUp() {
		it = new Topmenu();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		//dump(log);
		// TODO: test the results
	}

}
