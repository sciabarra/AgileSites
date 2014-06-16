package $site;format="normalize"$.test.$type;format="ndeprefix"$;
//import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$Layout;

// this test must be run by AgileSites TestRunnerElement
@Index("$site;format="normalize"$/tests.txt")
public class $subtype;format="deprefix"$LayoutTest extends TestElement {
	final static Log log = Log.getLog($subtype;format="deprefix"$LayoutTest.class); 
	$subtype;format="deprefix"$Layout it;
	
	@Before
	public void setUp() {
		it = new $subtype;format="deprefix"$Layout();
	}

	@Test
	public void test() {
		parse(it.apply(env("/Home")));
		// dump(log);
		assertText("#header h1", "Home Page Title");
	}
}
