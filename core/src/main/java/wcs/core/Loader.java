package wcs.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Load a jar using a networked classloader. It caches the classloader and check
 * if the underlying jar file changes then reload it.
 * 
 * @author msciab
 * 
 */
public class Loader {

	java.util.logging.Logger log = java.util.logging.Logger
			.getLogger(Loader.class.getCanonicalName());

	private File jar;
	private long jarTimestamp = 0;
	private URLClassLoader ucl;
	private ClassLoader mycl = getClass().getClassLoader();
	private String setupClass;

	/**
	 * Build a loader
	 * 
	 * @param file
	 */
	public Loader(File file, String setupSite) {
		jar = file;
		this.setupClass = setupSite.toLowerCase() + ".model." + "Setup";
	}

	/**
	 * Load jar if modified
	 * 
	 * @param jar
	 * @return
	 * @throws MalformedURLException
	 */
	public ClassLoader loadJar() throws MalformedURLException {

		if (jar == null) {
			if (WCS.debug) {
				System.out.println(">>>LOADER: no jar specified");
				log.severe("no jar specified");
			}

			return mycl;
		}

		if (!jar.exists()) {
			System.out.println(">>>LOADER: jar not found!!!");
			log.severe("no jar specified");
			return mycl;
		}

		// System.out.println("curTimestamp=" + curTimestamp);
		// System.out.println("jarTimestamp=" + jarTimestamp);

		// reloading jar if modified
		long curTimestamp = jar.lastModified();
		if (curTimestamp > jarTimestamp) {
			URL url = jar.toURI().toURL();

			System.out.println(">>>LOADER: reloading " + url);
			log.info("reloading " + url);

			jarTimestamp = curTimestamp;
			ucl = new URLClassLoader(new URL[] { url }, mycl);

		}
		return ucl;

	}
	
	/**
	 * Perform installation of the jar calling the setup method
	 * 
	 * @return
	 */
	public String installJar(String user, String pass) {
		// perform initialization
		try {
			
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(setupClass, true, loadJar());
			Object obj = clazz.newInstance();
			
			// cast and execute
			if (obj instanceof Setup) {
				Setup setup = (Setup) obj;
				return setup.exec(user, pass);
			} else
				return "Error in configuration - cannot find setup class";
			
		} catch (Exception ex) {
			System.out.println(">>>LOADER: setup exception " + ex.getMessage());
			log.warning("setup exception " + ex.getMessage());
			return ex.getMessage();
		}
	}

}
