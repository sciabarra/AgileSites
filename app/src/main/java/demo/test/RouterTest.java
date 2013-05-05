package demo.test;

import demo.Config;
import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.Router;
import wcs.java.util.TestElement;
import static wcs.core.Common.*;
import org.junit.Before;
import org.junit.Test;


public class RouterTest extends TestElement {

	final static Log log = Log.getLog(RouterTest.class);
	Router it;
	Env e;
	Asset home;

	@Before
	public void setUp() {
		it = Router.getRouter(Config.site);
		e = env();
		home = e.findOne("Page", arg("name", "Home"));
	}

	@Test
	public void test0() {
		parse(it.route(e, url("")));
		dump(log);
		if (home == null)
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		else {
			assertAttr("ics-callelement", "element", "Demo/DmWrapper");
			assertAttr("ics-callelement", "c", "Page");
			assertAttr("ics-callelement", "cid", home.getCid().toString());
		}

		parse(it.route(e, url("?a=1&b=2&c=3")));
		if (home == null)
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		else
			assertAttr("ics-callelement", "c", "Page");

	}

	@Test
	public void test1() {
		parse(it.route(env(), url("/DoesNotExist")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:DoesNotExist");

		parse(it.route(env(), url("/Hello?extra=parameter")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Hello");
	}

	@Test
	public void test2() {
		parse(it.route(env(), url("/Article/Welcome")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Demo_Article name:Welcome");
		parse(it.route(env(), url("/Article/Welcome?with=parameter&other")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Demo_Article name:Welcome");
	}

	@Test
	public void test3() {
		parse(it.route(env(), url("/something/not/supported?with=parameters")));
		assertAttr("ics-callelement", "error",
				"Path not found: /something/not/supported");
	}
}
