package wcs.api;

import COM.FutureTense.Interfaces.ICS;

/**
 * Interface for the Router
 * 
 * @author msciab
 * 
 */

public interface Router {
	
	/**
	 * Initialize the router with the site
	 * @param site
	 */
	public void init(String site);

	/**
	 * Route requests
	 * 
	 * @param ics
	 * @param site
	 * @param path
	 * @param query
	 * @return
	 */
	public Call route(ICS ics, String path, String query);

	/**
	 * Route an asset
	 * 
	 * @param env
	 * @param url
	 * @return
	 */
	public Call route(Env env, URL url);

	/**
	 * Generate the link to an asset
	 * 
	 * @param env
	 * @param id
	 * @return
	 */
	public String link(Env env, Id id, Arg... args);

}
