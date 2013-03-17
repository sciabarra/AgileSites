package $site;format="normalize"$;

import static wcs.core.Common.*;
import wcs.core.*;

import java.util.List;
import wcs.java.Env;
import wcs.java.util.Log;
import wcs.java.util.QueryString;
/**
 * Simple router looking for pages by name
 * 
 * @author msciab
 *
 */
public class Router extends wcs.java.Router {

	private static Log log = new Log(Router.class);
	
	@Override
	public Call route(Env e, String path, QueryString qs) {
		// default home page
		if (path == null)
			path = "Home";

		try {
			// search for a page with the given name
			List<Id> pages = e.find("Page", arg("name", path));
			if (pages.size() > 0) {
				String cid = pages.get(0).cid.toString();
				return call("$pfx$Wrapper", arg("c", "Page"), arg("cid", cid));
			} else {
				return call("$pfx$Error", arg("msg", "Page Not Found"));
			}
		} catch(Exception ex) {
			log.warn(ex.getMessage());
			return call("$pfx$Error", arg("msg", "Exception: "+ex.getMessage()));
		}
	}

}
