package wcs.java;

import wcs.core.Arg;
import wcs.java.util.Log;
import wcs.java.util.QueryString;
import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.ICS;

abstract public class Router implements wcs.core.Router {
	private static Log log = new Log(Router.class);

	private ICS i;
	private Env e;
	

	@Override
	public void route(ICS ics, String path, String query) {
		log.debug("path=" + path + " query=" + query);
		this.i = ics;
		this.e = new Env(i);
		route(e, path, QueryString.parse(query));
	}

	/**
	 * Call a given CSElement
	 * 
	 * @param what
	 * @param args
	 */
	public void call(String what, Arg... args) {
		String what2 = getSite()+"/"+what;
		FTValList list = new FTValList();
		list.setValString("site", getSite());
		for (Arg arg : args) {
			list.setValString(arg.name, arg.value);
		}
		i.CallElement(what2, list);
	}

	abstract public void route(Env env, String path, QueryString qs);
	abstract public String getSite();

}
