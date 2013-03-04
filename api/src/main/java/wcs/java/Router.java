package wcs.java;

import wcs.core.Arg;
import wcs.core.Call;
import wcs.java.util.Log;
import wcs.java.util.QueryString;
import COM.FutureTense.Interfaces.ICS;

abstract public class Router implements wcs.core.Router {
	private static Log log = new Log(Router.class);

	private ICS i;
	private Env e;
	private String site;

	@Override
	public Call route(ICS ics, String site, String path, String query) {
		log.debug("site=" + site + " path=" + path + " query=" + query);
		this.i = ics;
		this.e = new Env(i, site);
		this.site = Config.getConfig(site).getSite();
		return route(e, path, QueryString.parse(query));
	}

	public Call call(String name, Arg... args) {
		Call call = new Call(site + "/" + name, args);
		call.addArg("site", site);
		return call;
	}

	abstract public Call route(Env env, String path, QueryString qs);

}
