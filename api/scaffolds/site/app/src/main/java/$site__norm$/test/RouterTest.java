package $site;format="normalize"$.test;

import $site;format="normalize"$.Config;
import wcs.java.Router;
import wcs.java.util.TestElement;
import org.junit.Before;
import org.junit.Test;

public class RouterTest extends TestElement {

	Router it;

	@Before
	public void setUp() {
		it = Router.getRouter(Config.site);
	}

	@Test
	public void test0() {
		parse(it.route(env(), url("")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Home");

		parse(it.route(env(), url("?a=1&b=2&c=3")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Home");
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
				"Asset not found: type:MySite_Article name:Welcome");

		parse(it.route(env(), url("/Article/Welcome?with=parameter&other")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:MySite_Article name:Welcome");
	}

	@Test
	public void test3() {

		parse(it.route(env(), url("/something/not/supported?with=parameters")));
		assertAttr("ics-callelement", "error",
				"Path not found: /something/not/supported");

	}
}
