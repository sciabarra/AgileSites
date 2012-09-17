package wcs.boot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import COM.FutureTense.Interfaces.ICS;

public class Dispatcher {

	File jar = null;
	long jarTimestamp = 0;
	URLClassLoader ucl;

	static long generator = System.currentTimeMillis();

	/**
	 * New dispatcher looking for a given jar
	 * 
	 * @param jar
	 */
	public Dispatcher(File jar) {
		System.out.println("Dispatcher(" + jar + ")");
		setJar(jar);
	}

	static String gen(String prefix) {
		++generator;
		return prefix + generator;
	}

	public void setJar(File jar) {
		this.jar = jar;
	}

	/**
	 * Find classname from the asset loaded
	 */
	public String className(ICS ics) {

		String c = "Template";
		String cid = ics.GetVar("tid");
		if (cid == null) {
			c = "CSElement";
			cid = ics.GetVar("eid");
		}
		if (cid == null)
			return "wcs.NotFound";

		String obj = gen("obj");
		String name = gen("name");

		// checking the name
		Asset.load(ics, obj, c, cid);
		Asset.get(ics, obj, "name", name);
		String value = ics.GetVar(name);
		ics.RemoveVar(name);
		ics.RemoveVar(obj);

		if (value.startsWith("/"))
			value = "_" + value.substring(1).replace("/", ".");
		else
			value = value.replace("/", ".");

		String className = "app." + c + "." + value;

		System.out.println(className);

		return className;
	}

	/**
	 * Load jar if modified
	 * 
	 * @param jar
	 * @return
	 * @throws MalformedURLException
	 */
	public ClassLoader loadJar(File jar) throws MalformedURLException {

		ClassLoader mycl = getClass().getClassLoader();

		if (jar == null) {
			System.out.println(">>> no jar specified");
			return mycl;
		}

		if (!jar.exists()) {
			System.out.println(">>> jar not found!!!");
			return mycl;
		}

		// System.out.println("curTimestamp=" + curTimestamp);
		// System.out.println("jarTimestamp=" + jarTimestamp);

		// reloading jar if modified
		long curTimestamp = jar.lastModified();
		if (curTimestamp > jarTimestamp) {
			URL url = jar.toURI().toURL();
			System.out.println(">>> reloading " + url);
			jarTimestamp = curTimestamp;
			ucl = new URLClassLoader(new URL[] { url }, mycl);
		}
		return ucl;

	}

	/**
	 * Call the appropriate class after reloading the jar and creating a wrapper
	 * for ICS and the Element
	 * 
	 * @param ics
	 * @return
	 */
	public String dispatch(ICS ics) {
		try {

			// jar & classname
			String className = className(ics);
			ClassLoader cl = loadJar(jar);

			// instantiate
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(className, true, cl);
			Object obj = clazz.newInstance();

			// cast and execute
			if (obj instanceof wcs.Element) {
				wcs.Element element = (wcs.Element) obj;
				return element.exec(ics);
			}
			return "<h1>Not Found " + className + "<h1>";

		} catch (Exception e) {
			return "<h1>EXCEPTION</h1><p>" + e.getMessage() + "</p>";
		}

	}
}
