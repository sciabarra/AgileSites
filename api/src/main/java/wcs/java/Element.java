package wcs.java;

import COM.FutureTense.Interfaces.ICS;
import wcs.java.Util.Arg;
import wcs.java.Util.Id;

/**
 * 
 * Element
 * 
 * @author msciab
 * 
 */
public abstract class Element implements wcs.core.Element {

	/**
	 * Execute the element
	 * 
	 */
	@Override
	public String exec(ICS ics) {
		try {
			Env env = new Env(ics);
			String res = apply(env);
			// TODO split stream
			ics.StreamText(res);
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	/**
	 * Call another element
	 * 
	 * @param name
	 * @param args
	 */
	public void call(String name, Arg... args) {
		// TODO
	}

	/**
	 * Generate the url to a given asset.
	 * 
	 * @param id
	 * @return
	 */
	public String url(Id id) {
		// TODO
		return null;
	}

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	abstract public String apply(Env env);
}
