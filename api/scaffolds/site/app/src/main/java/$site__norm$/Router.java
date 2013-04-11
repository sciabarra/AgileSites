package $site;format="normalize"$;

import wcs.core.Log;
import wcs.core.URL;
import wcs.core.Call;
import wcs.core.Id;
import wcs.java.Env;
import java.util.List;
import java.util.StringTokenizer;
import static wcs.core.Common.arg;

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
			c = "$site$_" + st.nextToken();
			name = st.nextToken();
			break;

		// unknown path
		default: // example: http://yoursite/service/action/parameter"
			c = null;
			break;
		}

		// path not split in pieces
		if (c == null || name == null) {
			return call("$prefix$Wrapper",
					arg("error", "Path not found: " + url.getPath()));
		}

		// resolve the name to an id
		List<Id> list = e.find(c, arg("name", name));
		if (list.size() > 0) {
			// found
			return call("$prefix$Wrapper", //
					arg("c", list.get(0).c), //
					arg("cid", list.get(0).cid.toString()));
		} else {
			// not found
			String error = "Asset not found: type:" + c + " name:" + name;
			return call("$prefix$Wrapper", arg("error", error));
		}
	}
}
