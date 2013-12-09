package demo;
import static wcs.Api.*;
import wcs.api.*;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Simple router invoking the tester only
 * 
 * @author msciab
 * 
 */
public class Router extends wcs.java.Router {

	static final Log log = Log.getLog(Router.class);

	@Override
	public Call route(Env e, URL url) {

		//for (int i = 0; i < 10; i++) {
		//	System.out
		//			.println(AssemblerEngine.getAssemblerClassnamePropName(i));
		//	System.out
		//			.println(AssemblerEngine.getAssemblerShortFormPropName(i));
		//}

		// split the token
		String c = null;
		String name = null;

		StringTokenizer st = url.getPathTokens();
		switch (st.countTokens()) {
		case 0: // example: http://yoursite.com
			// look for the home page
			c = "Page";
			name = "Home";
			break;

		case 1: // example: http://yoursite.com/Welcome
			// look for a named page
			c = "Page";
			name = st.nextToken();
			break;

		case 2: // example: http://yoursite/Article/About
			// the following assume all the asset types
			// have the same prefix as the site name
			c = "Demo_" + st.nextToken();
			name = st.nextToken();
			break;

		// unknown path
		default: // example: http://yoursite/service/action/parameter"
			c = null;
			break;
		}

		// path not split in pieces
		if (c == null || name == null) {
			return call("demo",
					arg("error", "Path not found: " + url.getPath()));
		}

		// resolve the name to an id
		List<Id> list = e.find(c, arg("name", name));
		if (list.size() > 0) {
			// found
			return call("Demo/DmWrapper", //
					arg("c", list.get(0).c), //
					arg("cid", list.get(0).cid.toString()));
		} else {
			// not found
			String error = "Asset not found: type:" + c + " name:" + name;
			return call("demo", arg("error", error));
		}
	}

	/**
	 * Create a link with just the page name
	 * 
	 * Special case: the home page, normalized to just the void string
	 */
	@Override
	public String link(Env e, Id id, Arg... args) {
		String name = e.getAsset(id).getName();
		if (id.c.equals("Page"))
			// home page
			if (name.equals("Home"))
				return "";
			else
				return "/" + name;
		else
			// assuming all the types starts with Demo_, remove the prefix
			return "/" + id.c.substring(6) + "/" + name;
	}
}
