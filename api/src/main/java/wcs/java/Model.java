package wcs.java;

import static wcs.java.util.Util.toInt;
import static wcs.java.util.Util.toDate;
import static wcs.java.util.Util.toLong;

import wcs.core.Arg;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class to pass data around (mostly to the picker)
 * 
 * @author msciab
 * 
 */
@SuppressWarnings("serial")
public class Model extends HashMap<String, List<String>> implements Content {

	/**
	 * Create a model with a sequence of args. If you need the same arg more
	 * than once, pass the same argument multiple times
	 * 
	 * @param args
	 */
	public Model(Arg... args) {
		for (Arg arg : args) {
			List<String> list = get(arg.name);
			if (list == null)
				put(arg.name, list = new LinkedList<String>());
			list.add(arg.value);
		}
	}

	/**
	 * Check if a value exits
	 */
	@Override
	public boolean exists(String name) {
		return get(name) != null;
	}

	/**
	 * Check if the nth value (1-based) exists
	 */
	@Override
	public boolean exists(String name, int pos) {
		return pos > 0 && get(name) != null && get(name).size() >= pos;
	}

	/**
	 * Return the value as a string
	 * 
	 */
	@Override
	public String getString(String name) {
		return exists(name) ? get(name).get(0) : null;
	}

	/**
	 * Return the nth value as a string
	 * 
	 */
	@Override
	public String getString(String name, int n) {
		return exists(name, n) ? get(name).get(n - 1) : null;
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
