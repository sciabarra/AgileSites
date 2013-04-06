package wcs.java.util;

import COM.FutureTense.Interfaces.ICS;
import wcs.implx.XmlICS;
import wcs.java.Env;

/**
 * Testable env - can override variables, lists
 * 
 * @author msciab
 * 
 */
public class TestEnv extends Env {

	XmlICS i;

	public TestEnv(ICS ics, String site) {
		super(ics, site);
		i = (XmlICS) ics;
	}

	/**
	 * Set a variable
	 * 
	 * @param k
	 * @param v
	 */
	public TestEnv setVar(String k, String v) {
		if (v != null)
			i.setVar(k, v);
		return this;
	}

	/**
	 * Set an IList (that is basically a table), each java list is a column and
	 * the first element is the field name is the first element
	 * 
	 * @param name
	 * @param cols
	 */
	public TestEnv setList(String name, java.util.List<String>... cols) {
		i.setList(name, cols);
		return this;
	}

}
