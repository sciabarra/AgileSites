package wcs.api;

import COM.FutureTense.Interfaces.ICS;

/**
 * Interface for the Router
 * 
 * @author msciab
 * 
 */

public interface Router {

	public Call route(ICS ics, String site, String path, String query);

	/**
	 * Route an asset
	 * 
	 * @param env
	 * @param url
	 * @return
	 */
	public Call route(Env env, URL url);

	/**
	 * Link an asset
	 * 
	 * @param env
	 * @param id
	 * @return
	 */
	public String link(Env env, Id id, Arg... args);

}
