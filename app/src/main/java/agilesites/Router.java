package agilesites;

import static wcs.core.Common.*;
import wcs.core.*;

import java.util.List;
import wcs.java.Env;
//import wcs.java.util.Log;
import wcs.java.util.URL;

public class Router extends wcs.java.Router {

	// private static Log log = new Log(Router.class);

	@Override
	public Call route(Env e, URL url) {

		String path = url.getPath();
		
		// default home page
		if (path == null)
			path = "Home";

		// remove extension from the name
		if (path.endsWith(".html")) {
			path = path.substring(0, path.length() - 5);
		}

		// search for a page with the given name
		List<Id> pages = e.find("Page", arg("name", path));
		if (pages.size() > 0) {
			String cid = pages.get(0).cid.toString();
			return call("AwWrapper", arg("c", "Page"), arg("cid", cid));
		} else {
			return call("AwError", arg("msg", "Page Not Found"));
		}
	}

}
