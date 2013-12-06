package $site;format="normalize"$.test;

import $site;format="normalize"$.element.Wrapper;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import wcs.core.Index;
import wcs.core.Log;

// this test must be run by the test runner
@Index("$site;format="normalize"$/tests.txt")
public class WrapperTest extends TestElement {
	
	static final Log log = Log.getLog(WrapperTest.class);

	Wrapper it;

	@Before
	public void setUp() {
		it = new Wrapper();
	}

	@Test
	public void testError() {
		parse(it.apply(env()));
		// odump(log); // dump the result of the parsing
		assertText("title", "Error");
		assertAttr("meta[name=description]", "content", "Asset not found");
		assertAttr("render-callelement", "error", "Asset not found");
	}
	
	// uncomment this when you have a Page named Home with description Home Page 
	/*@Test 
	public void testHome() {
		parse(it.apply(env("/Home")));
		// odump(log); // dump the result of the parsing
		assertText("title", "Home");
		assertAttr("meta[name=description]", "content", "Home Page");		 
		assertAttr("render-callelement", "c", "Page");
	}*/
}
