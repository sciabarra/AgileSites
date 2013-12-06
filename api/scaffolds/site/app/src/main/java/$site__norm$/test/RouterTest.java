package $site;format="normalize"$.test;

import static wcs.core.Common.*;
import wcs.core.Asset;
import wcs.core.Index;
import wcs.java.Env;
import wcs.java.Router;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;
import $site;format="normalize"$.Config;

@Index("$site;format="normalize"$/tests.txt")
public class RouterTest extends TestElement {

	Router it;

	@Before
	public void setUp() {
		it = Router.getRouter(Config.site);
	}

	@Test
	public void test0() {
		Env e = env();
		parse(it.route(e, url("")));
		Asset home = e.findOne("Page", arg("name", "Home"));
		if(home==null) {
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		} else {
			assertAttr("ics-callelement", "element", "$site$/$site$_Wrapper");
			assertAttr("ics-callelement", "c", "Page");
			assertAttr("ics-callelement", "cid", home.getCid().toString());
		}

		parse(it.route(env(), url("?a=1&b=2&c=3")));
		if(home==null) {
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		} else {
			assertAttr("ics-callelement", "element", "$site$/$site$_Wrapper");
			assertAttr("ics-callelement", "c", "Page");
			assertAttr("ics-callelement", "cid", home.getCid().toString());			
		}
	}

	@Test
	public void test1() {

		parse(it.route(env(), url("/Hello")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Hello");

		parse(it.route(env(), url("/Hello?extra=parameter")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Hello");
	}

	@Test
	public void test2() {

		parse(it.route(env(), url("/Article/Welcome")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:$site$_Article name:Welcome");

		parse(it.route(env(), url("/Article/Welcome?with=parameter&other")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:$site$_Article name:Welcome");
	}

	@Test
	public void test3() {
		
		parse(it.route(env(), url("/something/not/supported?with=parameters")));
		assertAttr("ics-callelement", "error",
				"Path not found: /something/not/supported");

	}
}
