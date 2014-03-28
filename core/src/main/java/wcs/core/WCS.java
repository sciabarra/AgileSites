package wcs.core;

import java.io.IOException;
import java.util.Properties;

import wcs.api.Call;
import wcs.api.Config;
import wcs.api.Log;
import wcs.api.Router;
import COM.FutureTense.Interfaces.ICS;

/**
 * 
 * Main class of the framework, mostly used by JSP to invoke extension methods.
 * 
 * @author msciab
 * 
 */
public class WCS {

	static {
		// disable loggin in jar class loader library (jcl)
		System.setProperty("jcl.isolateLogging", "false");
	}

	final static Log log = Log.getLog(WCS.class);

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
		if (log.debug())
			log.debug("[WCS.route] site=%s path=%s query=%s", site, path, query);
		Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
		if (dispatcher != null) {
			return dispatcher.route(ics, site, path, query);
		} else {
			if (log.debug())
				log.debug("[WCS.route] Not found jar.");
		}
		return null;
	}

	/**
	 * Return a router for the site. It uses the current site variable
	 * 
	 * @param site
	 * @return
	 */
	public static Router getRouter(ICS ics) {
		try {
			Router router = (Router) Dispatcher.getDispatcher(ics)
					.loadSiteClass(ics, "Router").newInstance();
			router.init(ics.GetVar("site"));
			return router;
		} catch (Exception ex) {
			log.error(ex, "[WCS.getRouter]");
			return null;
		}
	}

	/**
	 * Load the config for the current site
	 * 
	 * @param site
	 * @return
	 */
	public static Config getConfig(ICS ics) {
		try {
			return (Config) Dispatcher.getDispatcher(ics)
					.loadSiteClass(ics, "Config").newInstance();
		} catch (Exception ex) {
			log.error(ex, "[WCS.getConfig]");
			return null;
		}
	}

	/**
	 * Load an env class initializing it with an ICS
	 * 
	 * @param site
	 * @return
	 */
	public static wcs.api.Env getEnv(ICS ics, String className) {
		try {
			wcs.api.Env env = (wcs.api.Env) Dispatcher.getDispatcher(ics)
					.loadClass(className).newInstance();
			env.init(ics);
			return env;
		} catch (Exception ex) {
			log.error(ex, "[WCS.getEnv]");
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
