package demo.test;

import static wcs.Api.*;
import wcs.api.*;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

@Index("demo/tests.txt")
public class RouterTest extends TestElement {

	final static Log log = Log.getLog(RouterTest.class);
	Env e;
	Asset home;
	Router it;

	@Before
	public void setUp() {
		e = env();
		it = e.getRouter();
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
			assertAttr("ics-callelement", "element", "Demo/Demo_Wrapper");
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

		parse(it.route(env(), url("/DoesNotExist?extra=parameter")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:DoesNotExist");
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
