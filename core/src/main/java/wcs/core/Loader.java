package wcs.core;

// **** DANGER ZONE ****
// This file is critical and difficult to test
// change it at your own risk.
// **********************

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import wcs.api.Log;

/**
 * Load a jar using a networked classloader. It caches the classloader and check
 * if the underlying jar file changes then reload it.
 * 
 * @author msciab
 * 
 */
public class Loader {

	final static Log log = Log.getLog(Loader.class);

	private File jarDir;
	private File spoolDir;
	private long nextCheck = 0;
	private long jarTimeStamp;
	private int reloadInterval = 0;

	private ClassLoader parentClassLoader;
	private ClassLoader currentClassLoader;

	/**
	 * Build a loader
	 * 
	 * @param interval
	 *            for polling
	 * @param dir
	 *            where locate the jars
	 * @param cl
	 *            parent classloader
	 */
	public Loader(File dir, int interval, ClassLoader cl) {
		jarDir = dir;
		spoolDir = new File(dir, "spool");
		// reset spool dir to a known state
		if (!spoolDir.exists())
			spoolDir.mkdirs();
		else
			for (File file : spoolDir.listFiles())
				file.delete();

		parentClassLoader = cl;
		currentClassLoader = parentClassLoader;
		reloadInterval = interval;
		nextCheck = System.currentTimeMillis();
	}

	/**
	 * Build a default loader
	 * 
	 * @param file
	 */
	public Loader() {
		jarDir = null;
		reloadInterval = 0;
	}

	/**
	 * Return the parent classloader
	 */
	public ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}

	private final static URL[] URL0 = new URL[0];

	/**
	 * Return a class loader allowing to access new jars. It copies jars to a
	 * spool dir before loading them.
	 * 
	 * @return
	 */
	public ClassLoader getClassLoader() {

		// get jars if somehing changed
		File[] jars = getJarsIfSomeIsModifiedAfterInterval();
		if (jars == null)
			return currentClassLoader;

		// update classloader
		synchronized (this) {

			// close current classloader in order to be able to load jars
			close();

			// copy changed files in the spool dir
			for (File source : jars) {
				if (source.isDirectory()
						|| !source.getName().toLowerCase().endsWith(".jar"))
					continue;
				File dest = new File(spoolDir, source.getName());
				if (!dest.exists()) {
					try {
						if (!spoolDir.exists())
							spoolDir.mkdirs();
						Files.copy(source.toPath(), dest.toPath(),
								StandardCopyOption.COPY_ATTRIBUTES);
						if (log.trace())
							log.trace("spooling (copy) %s", source.getName());
					} catch (Exception ex) {
						log.error(ex, "trying to copy jar on target");
					}
				} else if (dest.lastModified() < source.lastModified())
					try {
						if (log.trace())
							log.trace("spooling (update) %s", source.getName());
						Files.copy(source.toPath(), dest.toPath(),
								StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						log.error(e, "trying to replace existing jar");
					}
			}

			// create the classloading array
			try {
				File[] newjars = spoolDir.listFiles();
				List<URL> urlls = new ArrayList<URL>();
				StringBuilder sb = new StringBuilder("[Loader] reloading ");
				for (int i = 0; i < newjars.length; i++) {
					File file = newjars[i];
					if (file.isDirectory()
							|| !file.getName().toLowerCase().endsWith(".jar"))
						continue;
					sb.append(file.getName()).append(" ");
					urlls.add(file.toURI().toURL());
				}
				currentClassLoader = new URLClassLoader(urlls.toArray(URL0),
						parentClassLoader);
				if (log.debug())
					log.debug(sb.toString());
			} catch (Exception ex) {
				log.error(ex, "[Loader.getClassLoader]");
			}
			return currentClassLoader;
		}
	}

	public void close() {
		// close current classloader
		if (currentClassLoader instanceof URLClassLoader) {
			try {
				((URLClassLoader) currentClassLoader).close();
			} catch (IOException e) {
				log.error(e, "[Loader.getClassLoader]");
			}
			currentClassLoader = parentClassLoader;
		}
	}

	/**
	 * Return the jars to use only for the classloader if some of them has been
	 * modified Check only once in a given interval.
	 * 
	 * @param jar
	 * @return
	 * @throws MalformedURLException
	 */
	public File[] getJarsIfSomeIsModifiedAfterInterval() {

		// ********************
		// DO NOT CHANGE THIS METHOD WITHOUT CAREFUL TESTING
		// IT IS ACCESSED HEAVILY CONCURRENTLY
		// ********************

		// check it is is time to check
		// do it in an unsychronized way
		long now = System.currentTimeMillis();
		if (now < nextCheck)
			return null;

		synchronized (this) {

			// some time was spent trying to acquire the lock
			// someone else may have updated the time to check
			// checkiung again what is the time
			// if someone updated the nextCheckTime
			// update time for the next check
			now = System.currentTimeMillis();
			if (now < nextCheck)
				return null;

			// set next check time
			nextCheck = nextCheck + reloadInterval;

			// get the more recent lastmodified timestamp
			long curTimeStamp = 0;

			File[] jars = jarDir.listFiles();
			if (jars.length == 0) {
				log.warn("no jars in the jar folder");
				return null;
			}

			for (File file : jars) {
				if (file.isDirectory()
						|| !file.getName().toLowerCase().endsWith(".jar"))
					continue;
				curTimeStamp = Math.max(curTimeStamp, file.lastModified());
			}

			// log.trace("curTimestamp=%d jarTimestamp=%d", curTimestamp,
			// jarTimestamp);
			if (curTimeStamp > jarTimeStamp) {
				if (log.debug())
					log.debug("jar changed, timestamp=%d", curTimeStamp);
				jarTimeStamp = curTimeStamp;
				return jars;
			} else {
				if (log.trace())
					log.trace("no changes detected");
			}
			return null;
		}
	}

	/**
	 * Load a class for name from current class loaders
	 * 
	 * @param classname
	 * @return
	 */
	public Class<?> loadClass(String classname) {
		try {
			ClassLoader cl = getClassLoader();
			/*
			 * // print jars in the path if (DEBUG) if (cl instanceof
			 * URLClassLoader) { for (URL u : ((URLClassLoader) cl).getURLs())
			 * System.out.println(u.toString()); }
			 */
			if (log.trace())
				log.trace("loading %s", classname);
			return Class.forName(classname, true, cl);
		} catch (ClassNotFoundException ex) {
			log.error(ex, "[Loader.loadClass]");
			return null;
		}
	}
}
