package wcs.java.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wcs.java.Env;
import COM.FutureTense.Interfaces.ICS;

/**
 * Testable env - can override variables, lists
 * 
 * @author msciab
 * 
 */
public class TestEnv extends Env {

	ICS i;

	public TestEnv(ICS ics, String site) {
		super(ics, site);
		i = ics;
	}

	/**
	 * Set a variable
	 * 
	 * @param k
	 * @param v
	 */
	public TestEnv setVar(String k, String v) {
		if (v != null)
			i.SetVar(k, v);
		return this;
	}

	/**
	 * Set an IList (that is basically a table), each java list is a column and
	 * the first element is the field name is the first element
	 * 
	 * @param name
	 * @param cols
	 */
	public TestEnv setList(String name, @SuppressWarnings("unchecked") java.util.List<String>... cols) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (List<String> col : cols)
			map.put(col.get(0), col.subList(1, col.size()));
		i.RegisterList(name, new MapIList(name, map));
		throw new RuntimeException("not implemented");
		// return this;
	}

}
