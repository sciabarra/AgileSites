package wcs.java;

import wcs.java.util.QueryString;
import COM.FutureTense.Interfaces.ICS;

abstract public class Router implements wcs.core.Router {

	@Override
	public String route(ICS ics, String path, String query) {
		return route(new Env(ics), path, QueryString.parse(query));
	}

	abstract public String route(Env env, String path, QueryString qs);

}
