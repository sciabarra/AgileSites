package wcs.core;

import java.io.IOException;
import java.util.Properties;

import wcs.core.Config;
import COM.FutureTense.Interfaces.ICS;

/**
 * 
 * Main class of the framework, mostly used by JSP to invoke extension methods.
 * 
 * @author msciab
 * 
 */
public class WCS {

	final static Log log = Log.getLog(WCS.class);

	final static boolean debug = System.getProperty("wcs.core.debug") != null;

	/**
	 * Normalize Site name
	 */
	public static String normalizeSiteName(String site) {
		return site.toLowerCase().replaceAll("[^a-z0-9]+", "");
	}

	private static Properties properties = null;
	
	/**
	 * Return a property configured in setup
	 * 
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {		
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(Config.class
						.getResourceAsStream("/agilesites.properties"));
				System.out.println(properties.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

	/**
	 * Return a config, eventually cached. You can use both the site name or his
	 * normalized name for to get the config.
	 * 
	 * 
	 * @param site
	 * @return
	 */
	public static Config getConfig(String site) {
		try {
			return (Config) Class.forName(
					WCS.normalizeSiteName(site) + ".Config").newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static long tmpVarCounter = System.currentTimeMillis();

	/**
	 * Generate an unique temporary var name.
	 * 
	 * @return
	 */
	public synchronized static String tmp() {
		++tmpVarCounter;
		return "_" + Long.toHexString(tmpVarCounter);
	}

}
