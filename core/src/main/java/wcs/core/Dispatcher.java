package wcs.core;

import java.io.File;
import java.util.logging.*;
import COM.FutureTense.Interfaces.ICS;

public class Dispatcher {

	private static Logger log = Logger.getLogger(Dispatcher.class
			.getCanonicalName());

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
				System.err.println("Loading for" + site + "from " + jar);
				dispatcher = new Dispatcher(jar, site);
			}
		}
		return dispatcher;

	}

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher(File jar, String site) {
		if (WCS.debug) {
			System.out.println(">>>DISPATCHER: jar=" + jar);
			log.fine("jar=" + jar);
		}
		loader = new Loader(jar, site);
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
			return "<h1>EXCEPTION</h1>\n<p>" + e.getMessage() + "</p>";
		}

	}

	/**
	 * Call the given class after reloading the jar and creating a wrapper for
	 * ICS and the Element
	 * 
	 * @param ics
	 * @return
	 */
	public String deploy(ICS ics, String setupClassName, String user,
			String pass) {
		try {
			// jar & classname
			ClassLoader cl = loader.loadJar();

			// instantiate
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(setupClassName, true, cl);
			Object obj = clazz.newInstance();

			// cast and execute
			if (obj instanceof Setup) {
				Setup setup = (Setup) obj;
				return setup.exec(user, pass);
			}
			return "<h1>Not Found Setup " + setupClassName + "<h1>";

		} catch (Exception e) {
			return "<h1>EXCEPTION</h1>\n<p>" + e.getMessage() + "</p>";
		}
	}
}
