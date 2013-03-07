package wcs.core;

import java.io.File;
import java.util.StringTokenizer;

import COM.FutureTense.Interfaces.ICS;

public class Dispatcher {

	private Loader loader;

	private static Dispatcher dispatcher = null;

	/**
	 * Load the dispatcher singleton
	 * 
	 * @param ics
	 * @return
	 */
	static Dispatcher getDispatcher(ICS ics) {
		if (dispatcher == null) {
			String jarPath = ics.GetProperty("agilesites.jar");
			File jar = new File(jarPath);
			if (jar.exists()) {
				WCS.debug("[Dispatcher.getDispatcher] from " + jar);
				dispatcher = new Dispatcher(jar);
			} else {
				WCS.debug("[Dispatcher.getDispatcher] not found jar " + jar);
			}
		}
		return dispatcher;
	}

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher(File jar) {
		WCS.debug("[Dispatcher.<init>] load jar=" + jar);
		loader = new Loader(jar);
		WCS.debug("[Dispatcher.<init>] got loader");
	}

	/**
	 * Call the given class after reloading the jar and creating a wrapper for
	 * ICS and the Element
	 * 
	 * @param ics
	 * @return
	 */
	public String dispatch(ICS ics, String className) {
		try {

			// jar & classname
			ClassLoader cl = loader.loadJar();

			// instantiate
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className, true, cl);
			Object obj = clazz.newInstance();

			// cast and execute
			if (obj instanceof Element) {
				Element element = (Element) obj;
				return element.exec(ics);
			}
			return "<h1>Not Found Element " + className + "<h1>";

		} catch (Exception e) {
			WCS.debug("[Dispacher.dispach] exception loading " + className
					+ ":" + e.getMessage());
			e.printStackTrace();
			return "<h1>Exception</h1><p>Class: " + className
					+ "</p>\n<p>Message: " + e.getMessage() + "</p>\n";
		}

	}

	/**
	 * Route a call from the url assembler
	 * 
	 * @param ics
	 * @return
	 * @throws Exception
	 */
	public Call route(ICS ics, String site, String path, String query)
			throws Exception {
		String className = WCS.normalizeSiteName(site) + ".Router";
		WCS.debug("[WCS.route] className=" + className);
		try {
			// jar & classname
			ClassLoader cl = loader.loadJar();

			// instantiate
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className, true, cl);
			Object obj = clazz.newInstance();

			// cast and execute
			if (obj instanceof Router) {
				Router router = (Router) obj;
				return router.route(ics, site, path, query);
			} else {
				throw new Exception("Router not found");
			}
		} catch (Exception e) {
			WCS.debug("[Dispacher.dispach] exception " + e.getMessage()
					+ " loading " + className);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Deploy will invoke the <site>.Model class passing username and password.
	 * 
	 * Note that <site> is the normalised site name
	 * 
	 * @param ics
	 * @return
	 */
	public String deploy(ICS ics, String sites, String user, String pass) {

		if (sites == null) {
			WCS.debug("site is null !!!");
			return "Cannot Setup, no sites specified!";
		}

		ClassLoader cl = null;
		try {
			// jar & classname
			WCS.debug("[Dispatcher.deploy] loading jar");
			cl = loader.loadJar();
			WCS.debug("[Dispatcher.deploy] loaded classloader " + cl);
		} catch (Exception ex) {
			WCS.debug("[Dispacher.deploy] exception loading jar: "
					+ ex.getMessage());
			ex.printStackTrace();
			return "<h1>Exception</h1><p>Loading Jar: " //
					+ "</p>\n<p>Message: " + ex.getMessage() + "</p>\n";
		}

		StringTokenizer st = new StringTokenizer(sites, ",");
		StringBuilder msg = new StringBuilder();

		while (st.hasMoreTokens()) {

			// next setup to run...
			String site = st.nextToken().trim();
			String className = WCS.normalizeSiteName(site) + ".Setup";
			try {
				// instantiate

				WCS.debug("[Dispatcher.deploy] loading class " + className);
				Class<?> clazz = Class.forName(className, true, cl);
				WCS.debug("[Dispatcher.deploy] loaded class " + clazz);
				Object obj = clazz.newInstance();
				WCS.debug("[Dispatcher.deploy] loaded instance " + obj);

				// cast to Setup and execute
				if (obj instanceof wcs.core.Setup) {
					WCS.debug("[Dispatcher.deploy] obj is a wcs.core.Setup");
					Setup setup = (wcs.core.Setup) obj;
					msg.append(setup.exec(ics, site, user, pass));
				} else {
					WCS.debug("[Dispatcher.deploy] obj is NOT a wcs.core.Setup");
					msg.append("<h1>Not Found Setup for " + site + "<h1>");
				}
			} catch (ClassNotFoundException cnfe) {
				WCS.debug("[Dispacher.deploy] not found " + className);
			} catch (Exception e) {
				// logging errors and returning the message
				WCS.debug("[Dispacher.deploy] exception loading " + className
						+ " : " + e.getMessage());
				e.printStackTrace();
				msg.append("<h1>Exception</h1><p>Class: " + className
						+ "</p>\n<p>Message: " + e.getMessage() + "</p>\n");
			}
		}

		return msg.toString();
	}
}
