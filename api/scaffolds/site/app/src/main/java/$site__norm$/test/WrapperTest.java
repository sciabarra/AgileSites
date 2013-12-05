package $site;format="normalize"$.test;

import $site;format="normalize"$.element.Wrapper;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.AddIndex;

// this test must be run by the test runner
@AddIndex("$site;format="normalize"$/tests.txt")
public class WrapperTest extends TestElement {

	Wrapper it;

	@Before
	public void setUp() {
		it = new Wrapper();
	}

	@Test
	public void testError() {
		parse(it.apply(env()));
		assertText("title", "Error");
		assertAttr("meta[description]", "content", "Hello, world");
		assertAttr("render-callelement", "error", "Asset not found");
	}
	
	// uncomment this when you have a Page named Home with description Home Page 
	//@Test 
	public void testError() {
		parse(it.apply(env("/Home")));
		assertText("title", "Home");
		assertAttr("meta[description]", "content", "Home Page");		 
		assertAttr("render-callelement", "c", "Page");
	}
}
