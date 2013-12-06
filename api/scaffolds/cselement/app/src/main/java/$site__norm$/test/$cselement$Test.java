package $site;format="normalize"$.test;
import static wcs.core.Common.*;
import org.junit.Before;
import org.junit.Test;
import wcs.core.Index;
import wcs.core.Log;
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
		assertText("#footer", "(c) Sciabarra ltd");
	}

}
