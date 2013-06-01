package wcs.java;

import wcs.core.Arg;
import wcs.core.Call;
import wcs.core.Id;
import wcs.core.Log;
import wcs.core.URL;
import wcs.core.WCS;
import COM.FutureTense.Interfaces.ICS;

/**
 * The router - implement the abstract methods to provide url->asset and
 * asset->url conversions.
 * 
 * @author msciab
 * 
 */
abstract public class Router implements wcs.core.Router {

	private static Log log = Log.getLog(Router.class);

	private String site;

	/**
	 * Return a router for the site. You can use both the site name or his
	 * normalized name for to get the router.
	 * 
	 * 
	 * @param site
	 * @return
	 */
	public static Router getRouter(String site) {
		try {
			String clazz = WCS.normalizeSiteName(site) + ".Router";
			log.info("router=" + clazz);
			Router router = (Router) Class.forName(clazz).newInstance();
			// router site must be initialized here
			// config is retrieved because the site name can be normalized
			// to get the full site name
			router.site = Config.getConfig(site).getSite();
			return router;
		} catch (Exception ex) {
			log.error(ex, "cannot get router");
			return null;
		}
	}

	private ICS i;
	private Env e;

	@Override
	public Call route(ICS ics, String _site, String _path, String _query) {
		log.debug("site=" + _site + " path=" + _path + " query=" + _query);
		this.i = ics;
		site = Config.getConfig(_site).getSite();
		this.e = new Env(i, site);
		if (_query == null || _query.trim().length() == 0)
			_query = "";
		else
			_query = "?" + _query;
		return route(e, URL.parse(_path, _query));
	}

	public Call call(String name, Arg... args) {
		Call call = new Call("ICS:CALLELEMENT", args);
		call.addArg("site", site);
		call.addArg("element", name);
		log.trace("call returns %s", call.toString());
		return call;
	}

	/**
	 * Route an asset
	 * 
	 * @param env
	 * @param url
	 * @return
	 */
	abstract public Call route(Env env, URL url);

	/**
	 * Link an asset
	 * 
	 * @param env
	 * @param id
	 * @return
	 */
	abstract public String link(Env env, Id id, Arg... args);
}
