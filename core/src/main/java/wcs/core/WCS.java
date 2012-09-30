package wcs.core;

import java.io.File;

public class WCS {

	java.util.logging.Logger log = java.util.logging.Logger.getLogger(WCS.class
			.getCanonicalName());

	public final static boolean debug = System.getProperty("wcs.core.debug") != null;

	static Dispatcher dispatcher = null;

	public static String dispatch(COM.FutureTense.Interfaces.ICS ics,
			String clazz) {

		if (dispatcher == null) {
			File jar = new File(ics.GetProperty("scalawcs.jar"));
			if (jar.exists()) {
				System.err.println("Loading from " + jar);
				dispatcher = new Dispatcher(jar);
			} else
				return "Not found " + jar;
		}

		if (clazz == null)
			return dispatcher.dispatch(ics);
		else
			return dispatcher.dispatch(ics, clazz);
	}

	public static String dispatch(COM.FutureTense.Interfaces.ICS ics) {
		return dispatch(ics, null);
	}
}
