package wcs.core;

import COM.FutureTense.Interfaces.ICS;

public class WCS {

	java.util.logging.Logger log = java.util.logging.Logger.getLogger(WCS.class
			.getCanonicalName());

	final static boolean debug = System.getProperty("wcs.core.debug") != null;

	public static String dispatch(ICS ics, String clazz) {
		Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
		if (dispatcher != null)
			return dispatcher.dispatch(ics, clazz);
		else
			return "Not found  jar";
	}

	public static String deploy(ICS ics, String setupClassname, String user,
			String pass) {
		Dispatcher dispatcher = Dispatcher.getDispatcher(ics);
		if (dispatcher != null)
			return dispatcher.deploy(ics, setupClassname, user, pass);
		else
			return "Not found  jar";
	}

}
