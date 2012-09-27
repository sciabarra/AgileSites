package wcs.core;

import java.io.File;

import COM.FutureTense.Interfaces.FTValList;
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
			return "wcs.util.ElementNotFound";

		// checking the name
		assetLoad(ics, "asset", c, cid);
		assetGet(ics, "asset", "name", "value");
		String value = ics.GetVar("value");
		ics.RemoveVar("value");

		if (value.startsWith("/"))
			value = "_" + value.substring(1).replace("/", ".");
		else
			value = value.replace("/", ".");

		String className = "app.view." + c + "." + value;

		if (WCS.debug) {
			System.out.println(">>>DISPATCHER: " + className);
			log.fine("classname=" + className);
		}

		return className;
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

	/**
	 * Dispatcher without a classname - classname is calculated by the asset
	 * name.
	 * 
	 * @param ics
	 * @return
	 */
	public String dispatch(ICS ics) {
		return dispatch(ics, className(ics));
	}

	/**
	 * 
	 * Load an asset
	 */
	private String assetLoad(ICS ics, String name, String type, String objectid) {
		FTValList args = new FTValList();

		args.setValString("NAME", name);
		args.setValString("OBJECTID", objectid);
		args.setValString("TYPE", type);

		String _output = ics.runTag("ASSET.LOAD", args);

		if (WCS.debug) {
			System.out.print(">>DISPATCHER: asset:load name=" + name + " type="
					+ type + " objectid=" + objectid + " => " + _output);
			log.fine("asset:load name=" + name + " type=" + type + " objectid="
					+ objectid + " =>" + _output);
		}

		return _output == null ? "" : _output;
	}

	/**
	 * Get a value from an asset
	 * 
	 * @param ics
	 * @param name
	 * @param field
	 * @param output
	 * @return
	 */
	private String assetGet(ICS ics, String name, String field, String output) {
		FTValList args = new FTValList();

		args.setValString("NAME", name);
		args.setValString("FIELD", field);
		args.setValString("OUTPUT", output);

		String _output = ics.runTag("ASSET.GET", args);

		if (WCS.debug) {
			System.out.print(">>>DISPATCHER: asset:get name=" + name
					+ " field=" + field + " output=" + output + " => "
					+ _output);
			log.fine("asset:get name=" + name + " field=" + field + " output="
					+ output + " => " + _output);
		}

		return _output == null ? "" : _output;
	}
}
