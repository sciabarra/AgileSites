package wcs.core;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import COM.FutureTense.Interfaces.ICS;

public class WCS {

	final static Logger log = Logger.getLogger(WCS.class.getCanonicalName());

	final static boolean debug = System.getProperty("wcs.core.debug") != null;

	final static Properties properties = new Properties();

	static String jarPath;

	static {
		System.out.println("OUT wcs.core.debug=" + debug);
		System.err.println("ERR wcs.core.debug=" + debug);

		try {
			properties.load(WCS.class.getResourceAsStream("/agilewcs.prp"));
			System.out.println(properties.toString());
			jarPath = properties.getProperty("agilewcs.jar");
		} catch (IOException e) {
			WCS.debug("[WCS] !!! Cannot load properties:" + e.getMessage());
		}
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
		WCS.debug("[WCS.config] getting config");
		try {
			return Dispatcher.getDispatcher(jarPath).config(site, ics);
		} catch (Exception ex) {
			WCS.debug("[WCS.config] !!! Cannot get config: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}
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
			Dispatcher dispatcher = Dispatcher.getDispatcher(jarPath);
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
	 * Dispatch requests
	 * 
	 * @param ics
	 * @param clazz
	 * @return
	 */
	public static String route(ICS ics, String site, String path, String query) {
		WCS.debug("[WCS.dispatch] Dispatching...");
		try {
			Dispatcher dispatcher = Dispatcher.getDispatcher(jarPath);
			if (dispatcher != null)
				return dispatcher.route(ics, site, path, query);
			else
				return WCS.debug("[WCS.router] Not found jar.");
		} catch (Exception ex) {
			return WCS
					.debug("[WCS.route] !!! Cannot route: " + ex.getMessage());
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
		WCS.debug("[WCS.deploy] Deploying...");
		try {
			Dispatcher dispatcher = Dispatcher.getDispatcher(jarPath);
			if (dispatcher != null) {
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

	private static long tmpVarCounter = System.currentTimeMillis();

	/**
	 * Generate a temporary var
	 * 
	 * @return
	 */
	public synchronized static String tmpVar() {
		++tmpVarCounter;
		return "_" + Long.toHexString(tmpVarCounter);
	}

	/**
	 * Normalize Site name
	 */
	public static String normalizeSiteName(String site) {
		return site.toLowerCase().replaceAll("[^a-z0-9]+", "");
	}
}
