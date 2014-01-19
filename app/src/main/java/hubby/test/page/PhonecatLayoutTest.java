package hubby.test.page;
//import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import hubby.element.page.PhonecatLayout;

// this test must be run by AgileSites TestRunnerElement
@Index("hubby/tests.txt")
public class PhonecatLayoutTest extends TestElement {
	final static Log log = Log.getLog(PhonecatLayoutTest.class); 
	PhonecatLayout it;
	
	@Before
	public void setUp() {
		it = new PhonecatLayout();
	}

	@Test
	public void test() {
		parse(it.apply(env("/Home")));
		// dump(log);
		assertText("#header h1", "Home Page Title");
	}
}
