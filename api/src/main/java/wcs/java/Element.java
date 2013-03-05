package wcs.java;

import wcs.core.Arg;
import wcs.core.Common;
import COM.FutureTense.Interfaces.ICS;

/**
 * 
 * Element
 * 
 * @author msciab
 * 
 */
public abstract class Element implements wcs.core.Element {

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
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	/**
	 * Call a cs element in the same site
	 * 
	 * @param name
	 * @return
	 */
	public String call(String name, Arg... args) {
		return Common.call(site + "/" + name, args);
	}

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	abstract public String apply(Env env);

}
