package demo;

import org.springframework.stereotype.Component;
import wcs.core.Arg;
import wcs.core.Log;
import wcs.core.URL;
import wcs.core.Call;
import wcs.core.Id;
import wcs.java.Env;

import javax.annotation.Resource;
import java.util.List;
import java.util.StringTokenizer;
import static wcs.core.Common.arg;

/**
 * Simple router invoking the tester only
 * 
 * @author msciab
 * 
 */
@Component
public class Router extends wcs.java.Router {

	//static final Log log = Log.getLog(Router.class);

    Env e;

    public Router(Env env, String site) {
        super();
        Config.getConfig(site).getSite();
        this.e = env;
        e.setRouter(this);
    }

    @Override
	public Call route( URL url) {

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
	public String link(Id id, Arg... args) {
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
