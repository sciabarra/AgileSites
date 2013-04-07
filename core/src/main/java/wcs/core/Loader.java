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

	final static Log log = Log.getLog(Loader.class);

	private long jarTimestamp = 0;
	private URLClassLoader ucl;
	private ClassLoader mycl = getClass().getClassLoader();
	private File jar;

	/**
	 * Build a loader
	 * 
	 * @param file
	 */
	public Loader(File file) {
		jar = file;
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

			log.debug("[Loader]: no jar specified");
			return mycl;
		}

		if (!jar.exists()) {
			log.debug("[Loader]: jar not found!!!");
			return mycl;
		}

		// reloading jar if modified
		long curTimestamp = jar.lastModified();
		log.trace("curTimestamp=%l jarTimestamp=%j", curTimestamp, jarTimestamp);
		if (curTimestamp > jarTimestamp) {
			URL url = jar.toURI().toURL();
			log.debug("[Loader] reloading " + url);
			jarTimestamp = curTimestamp;
			ucl = new URLClassLoader(new URL[] { url }, mycl);
		}
		return ucl;

	}

}
