package wcs.boot;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class WCS {
	static Properties config = new Properties();
	static Dispatcher dispatcher = null;

	public static String dispatch(COM.FutureTense.Interfaces.ICS ics) {
		try {
			if (dispatcher == null) {
				config.load(Dispatcher.class
						.getResourceAsStream("/scalawcs.properties"));
				File jar = new File(config.getProperty("scalawcs.jar"));
				if (jar.exists()) {					
					System.err.println("Loading from " + jar);
					dispatcher = new Dispatcher(jar);
				} else
					return "Not found "+jar;
			}
			return dispatcher.dispatch(ics);
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR " + e.getMessage();
		}
	}
}
