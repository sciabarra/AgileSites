package wcs.core;

import java.util.Properties;
import COM.FutureTense.Interfaces.ICS;

public class WCS {

	final static Log log = Log.getLog(WCS.class);

	final static boolean debug = System.getProperty("wcs.core.debug") != null;

	final static Properties properties = new Properties();

	/**
	 * Normalize Site name
	 */
	public static String normalizeSiteName(String site) {
		return site.toLowerCase().replaceAll("[^a-z0-9]+", "");
	}

	/**
	 * Return a property configured in setup
	 * 
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		return properties.getProperty(name);
	}

	/**
	 * Dispatch requests
	 * 
	 * @param ics
	 * @param clazz
	 * @return
	 */
	public static String dispatch(ICS ics, String clazz) {
		log.trace("[WCS.dispatch] Dispatching...");
		try {
			Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
			if (dispatcher != null)
				return dispatcher.dispatch(ics, clazz);
			else {
				log.error("[WCS.dispatch] Not found jar.");
				return "[WCS.dispatch] Not found jar";
			}
		} catch (Exception ex) {
			log.warn(ex, "[WCS.dispatch]Cannot dispatch!");
			return "[WCS.dispatch] Cannot dispatch: " + ex.getMessage();
		}
	}

	/**
	 * Deploy
	 * 
	 * @param ics
	 * @param site
	 * @param user
	 * @param pass
	 * @return
	 */
	public static String deploy(ICS ics, String site, String user, String pass) {
		log.debug("[WCS.deploy] Deploying sites=%s user=%s", site, user);
		try {
			log.debug("[WCS.deploy] Getting dispatcher.");
			Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
			if (dispatcher != null) {
				log.debug("[WCS.deploy] Deploying...");
				String result = dispatcher.deploy(ics, site, user, pass);
				log.debug("[WCS.deploy] Deployed.");
				return result;
			} else {
				log.debug("[WCS.deploy] Not found jar.");
				return "[WCS.deploy] Not found jar.";
			}
		} catch (Exception ex) {
			log.debug(ex, "[WCS.deploy] Error invoking deploy");
			return "[WCS.deploy] Error invoking deploy";
		}
	}

	/**
	 * Dispatch requests
	 * 
	 * @param ics
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Call route(ICS ics, String site, String path, String query)
			throws Exception {
		log.debug("[WCS.route] Dispatching...");
		Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
		if (dispatcher != null) {
			return dispatcher.route(ics, site, path, query);
		} else {
			log.debug("[WCS.route] Not found jar.");
		}
		return null;
	}

}
