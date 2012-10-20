package wcs.core;

import java.io.File;

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
			File jar = new File(ics.GetProperty("scalawcs.jar"));
			String site = ics.GetProperty("scalawcs.site");

			if (jar.exists()) {
				WCS.debug("[Dispatcher.getDispatcher] site " //
						+ site + " from " + jar);
				dispatcher = new Dispatcher(jar);
			} else {
				WCS.debug("[Dispatcher.getDispatcher] Jar " + jar + "for "
						+ site + "not found");
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
	 * Call the given class after reloading the jar and creating a wrapper for
	 * ICS and the Element
	 * 
	 * @param ics
	 * @return
	 */
	public String deploy(ICS ics, String site, String user, String pass) {

		if (site == null) {
			WCS.debug("site is null!!!");
			return "Cannot deploy, no site!";
		}
		String className = site.toLowerCase().replace(" ", "_")
				.replaceAll("[^a-zA-Z_\\.]", "")
				+ ".Setup";
		WCS.debug("[Dispatcher.deploy] className=" + className);

		try {
			// jar & classname
			ClassLoader cl = loader.loadJar();
			WCS.debug("[Dispatcher.deploy] loaded classloader " + cl);

			// instantiate
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className, true, cl);
			WCS.debug("[Dispatcher.deploy] loaded class " + clazz);

			Object obj = clazz.newInstance();
			WCS.debug("[Dispatcher.deploy] loaded instance " + obj);

			// cast and execute
			if (obj instanceof wcs.core.Setup) {
				WCS.debug("[Dispatcher.deploy] obj is a wcs.core.Setup");
				Setup setup = (wcs.core.Setup) obj;
				return setup.exec(user, pass);
			} else {
				WCS.debug("[Dispatcher.deploy] obj is NOT a wcs.core.Setup");
			}
			return "<h1>Not Found Setup for " + site + "<h1>";

		} catch (Exception e) {
			WCS.debug("[Dispacher.deploy] exception loading " + className
					+ " : " + e.getMessage());
			e.printStackTrace();
			return "<h1>Exception</h1><p>Class: " + className
					+ "</p>\n<p>Message: " + e.getMessage() + "</p>\n";

		}
	}
}
