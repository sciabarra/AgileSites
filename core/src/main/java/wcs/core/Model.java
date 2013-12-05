package wcs.core;

import static wcs.core.Common.toInt;
import static wcs.core.Common.toDate;
import static wcs.core.Common.toLong;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Model class to pass data around (mostly to the picker)
 * 
 * @author msciab
 * 
 */
public class Model implements Content {

	Map<String, List<String>> map;

	// initialize the map from the arguments
	private void init(Arg... args) {
		for (Arg arg : args) {
			List<String> list = map.get(arg.name);
			if (list == null)
				map.put(arg.name, list = new LinkedList<String>());
			if (arg instanceof Args) {
				Args arglist = (Args) arg;
				list.addAll(arglist.values);
			} else {
				list.add(arg.value);
			}
		}
	}

	/**
	 * Create a model with a sequence of args. If you need the same arg more
	 * than once, pass the same argument multiple times
	 * 
	 * @param args
	 */
	public Model(Arg... args) {
		map = new HashMap<String, List<String>>();
		init(args);
	}

	/**
	 * Create a model with a sequence of args, building it on top of an existing
	 * If you need the same arg more than once, pass the same argument multiple
	 * times
	 * 
	 * @param args
	 */
	public Model(Model m, Arg... args) {
		if (m == null)
			map = new HashMap<String, List<String>>();
		else
			map = new HashMap<String, List<String>>(m.map);
		init(args);
	}

	/**
	 * Check if a value exits
	 */
	@Override
	public boolean exists(String name) {
		// System.out.println(map);
		return map.get(name) != null;
	}

	/**
	 * Check if the nth value (1-based) exists
	 */
	@Override
	public boolean exists(String name, int pos) {
		return pos > 0 && map.get(name) != null && map.get(name).size() >= pos;
	}

	/**
	 * Return the value as a string
	 * 
	 */
	@Override
	public String getString(String name) {
		return exists(name) ? map.get(name).get(0) : null;
	}

	/**
	 * Return the nth value as a string
	 * 
	 */
	@Override
	public String getString(String name, int n) {
		return exists(name, n) ? map.get(name).get(n - 1) : null;
	}

	/**
	 * Return the value as a int
	 * 
	 */
	@Override
	public Integer getInt(String name) {
		return toInt(getString(name));
	}

	/**
	 * Return the nth value as a int
	 * 
	 */
	@Override
	public Integer getInt(String name, int n) {
		return toInt(getString(name, n));
	}

	/**
	 * Return the value as a long
	 * 
	 */
	@Override
	public Long getLong(String name) {
		return toLong(getString(name));
	}

	/**
	 * Return the nth value as a long
	 * 
	 */
	@Override
	public Long getLong(String name, int n) {
		return toLong(getString(name, n));
	}

	/**
	 * Return the value as a date
	 * 
	 */
	@Override
	public Date getDate(String name) {
		return toDate(getString(name));
	}

	/**
	 * Return the nth value as a date
	 * 
	 */
	@Override
	public Date getDate(String name, int n) {
		return toDate(getString(name, n));
	}

}
