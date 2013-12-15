package $site;format="normalize"$.test;
//import static wcs.Api.*;
import wcs.api.Index;
import wcs.api.Log;
import org.junit.Before;
import org.junit.Test;
import wcs.java.util.TestElement;
import $site;format="normalize"$.element.$cselement$;

// this test must be run by AgileSites TestRunnerElement
@Index("$site;format="normalize"$/tests.txt")
public class $cselement$Test extends TestElement {
	final static Log log = Log.getLog($cselement$Test.class);
	$cselement$ it;
	
	@Before
	public void setUp() {
		it = new $cselement$();
	}

	@Test
	public void test() {
		// log.trace("testing $cselement$");
		parse(it.apply(env()));
		// dump(log);
		assertText("small", "by Sciabarra.com ltd");
	}

}
