package $site;format="normalize"$.test;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import $site;format="normalize"$.element.Wrapper;

// this test must be run by the test runner
@Index("$site;format="normalize"$/tests.txt")
public class WrapperTest extends TestElement {
	static final Log log = getLog(WrapperTest.class);
	Wrapper it;

	@Before
	public void setUp() {
		it = new Wrapper();
	}

	@Test
	public void testError() {
		parse(it.apply(env()));
		// dump(log); 
		assertText("title", "Error");
		assertAttr("meta[name=description]", "content", "Asset not found");
		assertAttr("render-callelement", "error", "Asset not found");
	}
	
	// uncomment this when you have a Page named Home with description Home Page 
	/*@Test 
	public void testHome() {
		parse(it.apply(env("/Home")));
		// dump(log); // dump the result of the parsing
		assertText("title", "Home");
		assertAttr("meta[name=description]", "content", "Home Page");		 
		assertAttr("render-callt", "c", "Page");
	}*/
}
