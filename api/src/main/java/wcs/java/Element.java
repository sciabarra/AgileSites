package wcs.java;

import java.util.LinkedList;
import java.util.List;

import wcs.core.Arg;
import wcs.core.Common;
import wcs.core.Log;
import COM.FutureTense.Interfaces.ICS;

/**
 * 
 * Element
 * 
 * @author msciab
 * 
 */
public abstract class Element implements wcs.core.Element {

	static Log log = Log.getLog(Element.class);

	// current site
	protected String site;

	/**
	 * Execute the element
	 * 
	 * The bulk of the method is streaming the result and invoking embedded
	 * method calls.
	 * 
	 */
	@Override
	public String exec(ICS ics) {
		try {
			site = ics.GetVar("site");
			Env env = new Env(ics, site);
			return apply(env);
		} catch (Exception ex) {
			log.error(ex, "exception applying element");
			return ex.getMessage();
		}
	}

	/**
	 * Generic CallElement
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public String call(String name, Arg... args) {
		List<Arg> list = new LinkedList<Arg>();
		for (Arg arg : args)
			list.add(arg);
		list.add(Common.arg("ELEMENT", name));
		return Common.call("ICS:CALLELEMENT", list);
	}

	/**
	 * Convenience method for defining args
	 * 
	 * @param name
	 * @return
	 */
	public Arg arg(String k, String v) {
		return Common.arg(k, v);
	}

	/**
	 * Convenience method to log in a functional way
	 * 
	 * @param name
	 * @return
	 */
	public String log(String msg) {
		log.debug(msg);
		return msg;
	}

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	abstract public String apply(Env env);

}
