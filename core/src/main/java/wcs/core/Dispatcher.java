package wcs.core;

import java.io.File;

import COM.FutureTense.Interfaces.ICS;

public class Dispatcher {

	java.util.logging.Logger log = java.util.logging.Logger
			.getLogger(Dispatcher.class.getCanonicalName());

	private Loader loader;

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher(File jar) {
		if (WCS.debug) {
			System.out.println(">>>DISPATCHER: jar=" + jar);
			log.fine("jar=" + jar);
		}
		setJar(jar);
	}

	public void setJar(File jar) {
		loader = new Loader(jar);
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
			return "<h1>Not Found " + className + "<h1>";

		} catch (Exception e) {
			return "<h1>EXCEPTION</h1><p>" + e.getMessage() + "</p>";
		}

	}
}
