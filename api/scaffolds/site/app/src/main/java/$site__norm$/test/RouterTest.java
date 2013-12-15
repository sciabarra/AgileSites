package $site;format="normalize"$.test;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Asset;
import wcs.api.Index;
import wcs.api.Env;
import wcs.api.Router;
import wcs.java.util.TestElement;
import org.junit.Test;

@Index("$site;format="normalize"$/tests.txt")
public class RouterTest extends TestElement {
	final static Log log = getLog(TestElement.class);
	Router it;

	@Test
	public void test0() {
		Env e = env();
		it = e.getRouter();
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
		Env e = env();
		it = e.getRouter();
		parse(it.route(e, url("/Hello")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Hello");
		parse(it.route(env(), url("/Hello?extra=parameter")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:Page name:Hello");
	}

	@Test
	public void test2() {
		Env e = env();
		it = e.getRouter();
		parse(it.route(e, url("/Article/Welcome")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:$site$_Article name:Welcome");
		parse(it.route(env(), url("/Article/Welcome?with=parameter&other")));
		assertAttr("ics-callelement", "error",
				"Asset not found: type:$site$_Article name:Welcome");
	}

	@Test
	public void test3() {
		Env e = env();
		it = e.getRouter();
		parse(it.route(e, url("/something/not/supported?with=parameters")));
		assertAttr("ics-callelement", "error",
				"Path not found: /something/not/supported");
	}
}
