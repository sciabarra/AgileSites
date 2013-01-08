package wcs.core;

import java.util.logging.Logger;
import COM.FutureTense.Interfaces.ICS;

public class WCS {

	static Logger log = Logger.getLogger(WCS.class.getCanonicalName());

	final static boolean debug = System.getProperty("wcs.core.debug") != null;

	static {
		System.out.println("OUT wcs.core.debug=" + debug);
		System.err.println("ERR wcs.core.debug=" + debug);
	}

	/**
	 * Print debug messages if the core debug property is enabled
	 * 
	 * @param msg
	 */
	public static String debug(String msg) {
		if (debug) {
			System.err.println(">>> " + msg);
			log.fine(msg);
		}
		return msg;
	}

	/**
	 * Return the config loading from the jar
	 * 
	 * @param site
	 * @param ics
	 * @return
	 */
	public static Config config(String site, ICS ics) {
		Dispatcher dispatcher = null;
		try {
			WCS.debug("[WCS.config] getting dispatcher");
			dispatcher = Dispatcher.getDispatcher(ics);
			return dispatcher.config(site, ics);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Return the config dispatching 
	 * 
	 * @param ics
	 * @param clazz
	 * @return
	 */
	public static String dispatch(ICS ics, String clazz) {
		Dispatcher dispatcher = null;
		try {
			dispatcher = Dispatcher.getDispatcher(ics);
		} catch (Exception ex) {
			ex.printStackTrace();
			return WCS.debug("[WCS.dispatch] Error getting dispatcher :"
					+ ex.getMessage());
		}

		try {
			if (dispatcher != null)
				return dispatcher.dispatch(ics, clazz);
			else
				return WCS.debug("Not found  jar");
		} catch (Exception ex) {
			return WCS.debug("[WCS.dispatch] Error invoking dispatch :"
					+ ex.getMessage());
		}
	}

	public static String deploy(ICS ics, String site, String user, String pass) {
		Dispatcher dispatcher = null;

		try {
			WCS.debug("[WCS.deploy] getting dispatcher");
			dispatcher = Dispatcher.getDispatcher(ics);
			WCS.debug("[WCS.deploy] got dispatcher");
		} catch (Exception ex) {
			ex.printStackTrace();
			return WCS.debug("[WCS.deploy] Error getting dispatcher :"
					+ ex.getMessage());
		}

		try {
			if (dispatcher != null) {
				WCS.debug("[WCS.deploy] deploying");
				String result = dispatcher.deploy(ics, site, user, pass);
				WCS.debug("[WCS.deploy] deployed");
				return result;
			} else
				return WCS.debug("Not found  jar");
		} catch (Exception ex) {
			return WCS.debug("[WCS.deploy] Error invoking deploy:"
					+ ex.getMessage());
		}

	}

}
