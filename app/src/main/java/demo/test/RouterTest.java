package demo.test;

import demo.Config;
import org.springframework.stereotype.Component;
import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.Router;
import wcs.java.util.TestElement;
import wcs.core.Log;

import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static demo.Config.*;
import static wcs.core.Common.*;

public class RouterTest extends TestElement {

	final static Log log = Log.getLog(RouterTest.class);

    Router router;
    Env e;
    Asset home;

	@Before
	public void setUp() {
        e = env();
        router = Router.getRouter(site, e);
		home = e.findOne("Page", arg("name", "Home"));
	}

	@Test
	public void test0() {
		parse(router.route(url("")));
		dump(log);
		if (home == null)
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		else {
			assertAttr("ics-callelement", "element", "Demo/DmWrapper");
			assertAttr("ics-callelement", "c", "Page");
			assertAttr("ics-callelement", "cid", home.getCid().toString());
		}

		parse(router.route(url("?a=1&b=2&c=3")));
		if (home == null)
			assertAttr("ics-callelement", "error",
					"Asset not found: type:Page name:Home");
		else
			assertAttr("ics-callelement", "c", "Page");

	}

	@Test
	public void test1() {
		parse(router.route(url("/DoesNotExist")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:DoesNotExist");

		parse(router.route(url("/DoesNotExist?extra=parameter")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:DoesNotExist");
	}

	@Test
	public void test2() {
		parse(router.route(url("/Article/Welcome")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Demo_Article name:Welcome");
		parse(router.route(url("/Article/Welcome?with=parameter&other")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Demo_Article name:Welcome");
	}

	@Test
	public void test3() {
		parse(router.route(url("/something/not/supported?with=parameters")));
		assertAttr("ics-callelement", "error",
				"Path not found: /something/not/supported");
	}
}
