package $site;format="normalize"$.test;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.Error;

// this test must be run by the test runner
@Index("$site;format="normalize"$/tests.txt")
public class ErrorTest extends TestElement {
	final static Log log = getLog(TestElement.class);
	Error it;
	
	@Before
	public void setUp() {
		it = new Error();
	}

	@Test
	public void test() {
		// parse element in a custom env
		parse(it.apply(env(arg("error","Hello, world"))));
		// dump(log);

		// check the result
		assertText("#header h1", "Error");
		assertText("#detail p", "Hello, world");
	}
}
