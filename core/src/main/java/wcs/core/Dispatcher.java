package wcs.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import wcs.Api;
import wcs.api.Call;
import wcs.api.Element;
import wcs.api.Log;
import wcs.api.Router;
import wcs.core.Service;
import COM.FutureTense.Interfaces.ICS;

public class Dispatcher {

	final static Log log = Log.getLog(Dispatcher.class);

	private Loader loader;

	private static Dispatcher dispatcher = null;

	/**
	 * Load the dispatcher singleton using parameters from the futuretense.ini
	 * 
	 * @param ics
	 * @return
	 */
	static Dispatcher getDispatcher(ICS ics) {
		if (dispatcher == null) {
			String jarDir = ics.GetProperty("agilesites.dir");
			if (jarDir == null) {
				log.debug("[Dispatcher.getDispatcher] creating static dispatcher");
				dispatcher = new Dispatcher();
				return dispatcher;
			}

			int poll = 100;
			try {
				poll = Integer.parseInt(ics.GetProperty("agilesites.poll"));
			} catch (Exception e) {
			}
			File jarDirFile = new File(jarDir);
			if (jarDirFile.exists()) {
				log.debug("[Dispatcher.getDispatcher] dir=%s poll=%d", jarDir,
						poll);
				dispatcher = new Dispatcher(jarDirFile, poll);
			} else {
				log.debug("[Dispatcher.getDispatcher] not found jar dir, creating static dispatcher");
				dispatcher = new Dispatcher();
				return dispatcher;
			}
		}
		return dispatcher;
	}

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher(File jarDir, int reload) {
		log.debug("[Dispatcher.<init>] jarDir=%s reload=%d", jarDir, reload);
		loader = new Loader(jarDir, reload, Thread.currentThread()
				.getContextClassLoader()
		/* getClass().getClassLoader() */);
		log.debug("[Dispatcher.<init>] got loader");
	}

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher() {
		log.debug("[Dispatcher.<init>] static loader");
		loader = new Loader();
		log.debug("[Dispatcher.<init>] got loader");
	}

	/**
	 * Call a service class (wcs.hubby.Service) shutting down a past instance
	 * and initializing a new one.
	 */

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
			ClassLoader cl = loader.getClassLoader();

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
			log.debug("[Dispacher.dispach] exception loading " + className
					+ ":" + e.getMessage());
			e.printStackTrace();
			return "<h1>Exception</h1><p>Class: " + className
					+ "</p>\n<p>Message: " + e.getMessage() + "</p>\n";
		}

	}

	private long lastModifiedClassloader = 0;
	private Map<String, Service> serviceMap = new HashMap<String, Service>();

	/**
	 * Call the given class after reloading the jar and creating a wrapper for
	 * ICS and the Element
	 * 
	 * @param ics
	 * @return
	 */
	public String service(ICS ics, String className) {
		
		// get the classloader and update timestamp
		ClassLoader cl = loader.getClassLoader();

		StringBuilder sb = new StringBuilder();

		// stopping services if they are now to be replaced
		if (loader.getTimeStamp() > lastModifiedClassloader) {
			// stopping all the services
			for (Entry<String, Service> entry : serviceMap.entrySet()) {
				if (log.trace())
					log.trace("stopping service %s", entry.getKey());
				sb.append("STOP ").append(entry.getKey()).append(": ");
				try {
					sb.append(entry.getValue().stop(ics));
				} catch (Exception ex) {
					log.error("[Dispatch.service] STOP ", ex);
					sb.append(Api.ex2str(ex));
				}
				sb.append("\n");
				serviceMap.remove(entry.getKey());
			}
			// mark the current one as the last modified
			lastModifiedClassloader = loader.getTimeStamp();
		}

		// checking status of existing services
		Service service = serviceMap.get(className);
		if (service != null) {
			sb.append("STATUS ").append(className).append(": ");
			try {
				if (log.trace())
					log.trace("status service %s", className);
				sb.append(service.status(ics));
			} catch (Exception ex) {
				log.error("[Dispatch.service] STATUS ", ex);
				sb.append(Api.ex2str(ex));
			}
			sb.append("\n");
			return sb.toString();
		}

		// creating and starting a new service
		sb.append("START ").append(className).append(": ");
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className, true, cl);
			Object obj = clazz.newInstance();
			// cast and execute
			if (obj instanceof Service) {
				if (log.trace())
					log.trace("start service %s", className);
				service = (Service) obj;
				sb.append(service.start(ics));
				serviceMap.put(className, service);
				sb.append("\n");
			} else {
				log.warn("%s not a Service ", className);
				sb.append("!!! " + className + " not a Service, is "+obj.getClass());
			}
		} catch (Exception ex) {
			log.error("[Dispatch.service] START ", ex);
			sb.append(Api.ex2str(ex));
		}
		return sb.toString();
	}

	/**
	 * Load a class from the classloader
	 * 
	 * @param ics
	 * @param name
	 * @return
	 */
	public Class<?> loadClass(String className) {
		return loader.loadClass(className);
	}

	/**
	 * Load a site specific class from the classloader
	 * 
	 * @param ics
	 * @param name
	 * @return
	 */
	public Class<?> loadSiteClass(ICS ics, String name) {
		String site = ics.GetVar("site");
		String className = WCS.normalizeSiteName(site) + "." + name;
		return loadClass(className);
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
		try {
			// get and extecute the router
			Object obj = loadSiteClass(ics, "Router").newInstance();
			if (obj instanceof Router) {
				Router router = (Router) obj;
				router.init(site);
				return router.route(ics, path, query);
			} else {
				throw new Exception("Router not found");
			}
		} catch (Exception e) {
			log.error(e, "[Dispatcher.route]");
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
			return "Cannot Setup, no sites specified!";
		}

		StringTokenizer st = new StringTokenizer(sites, ",");
		StringBuilder msg = new StringBuilder();
		while (st.hasMoreTokens()) {
			// next setup to run...
			String site = st.nextToken().trim();
			String className = WCS.normalizeSiteName(site) + ".Setup";
			try {
				// cast to Setup and execute
				Class<?> clazz = loadClass(className);
				Object obj = null;
				if (clazz != null)
					obj = clazz.newInstance();
				if (obj != null && obj instanceof wcs.core.Setup) {
					log.debug("[Dispatcher.deploy] obj is a wcs.core.Setup");
					Setup setup = (wcs.core.Setup) obj;
					msg.append(setup.exec(ics, site, user, pass));
				} else {
					log.debug("[Dispatcher.deploy] obj is NOT a wcs.core.Setup");
					msg.append("*** Not Found Setup for " + site + "\n");
				}
			} catch (Exception e) {
				log.error(e, "[Dispatcher.deploy]");
				msg.append("<h1>Exception</h1><p>Class: " + className
						+ "</p>\n<p>Message: " + e.getMessage() + "</p>\n");
			}
		}
		return msg.toString();
	}
}
