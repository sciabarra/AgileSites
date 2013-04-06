package wcs.core;

import java.util.Properties;
import org.apache.log4j.Logger;

import COM.FutureTense.Interfaces.ICS;

public class WCS {

	final static Logger log = Logger.getLogger(WCS.class.getCanonicalName());

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
	 * Print debug messages if the core debug property is enabled
	 * 
	 * @param msg
	 */
	public static String debug(String msg) {
		if (debug) {
			System.err.println(">>> " + msg);
			log.debug(msg);
		}
		return msg;
	}

	/**
	 * Dispatch requests
	 * 
	 * @param ics
	 * @param clazz
	 * @return
	 */
	public static String dispatch(ICS ics, String clazz) {
		WCS.debug("[WCS.dispatch] Dispatching...");
		try {
			Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
			if (dispatcher != null)
				return dispatcher.dispatch(ics, clazz);
			else
				return WCS.debug("[WCS.dispatch] Not found jar.");
		} catch (Exception ex) {
			return WCS.debug("[WCS.dispatch] !!! Cannot dispatch: "
					+ ex.getMessage());
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
		WCS.debug("[WCS.deploy] Deploying sites=" + site + " user=" + user
				+ " pass=" + pass);
		try {
			WCS.debug("[WCS.deploy] Getting dispatcher.");
			Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
			if (dispatcher != null) {
				WCS.debug("[WCS.deploy] Deploying...");
				String result = dispatcher.deploy(ics, site, user, pass);
				WCS.debug("[WCS.deploy] Deployed.");
				return result;
			} else
				return WCS.debug("[WCS.dispatch] Not found jar.");
		} catch (Exception ex) {
			return WCS.debug("[WCS.deploy] Error invoking deploy:"
					+ ex.getMessage());
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
		WCS.debug("[WCS.dispatch] Dispatching...");
		Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
		if (dispatcher != null) {
			return dispatcher.route(ics, site, path, query);
		} else {
			WCS.debug("[WCS.router] Not found jar.");
		}
		return null;
	}

}
