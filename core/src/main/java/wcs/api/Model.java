package wcs.api;

import static wcs.Api.toDate;
import static wcs.Api.toInt;
import static wcs.Api.toLong;

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
			if (arg.value == null)
				continue;
			List<String> list = map.get(arg.name);
			if (list == null)
				map.put(arg.name, list = new LinkedList<String>());
			if (arg instanceof Args) {
				Args arglist = (Args) arg;
				for (String value : arglist.values)
					if (value != null)
						list.add(value);
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

	/**
	 * Return the size of the attribute
	 */
	@Override
	public int getSize(String attribute) {
		if (exists(attribute))
			return map.get(attribute).size();
		else
			return 0;
	}

	/**
	 * Return the range of the attribute: an iterator returning the valid values
	 * 
	 * for(int i: m.getRange("attr")) { doSometing(m.getString("attr", i)); }
	 */
	@Override
	public Iterable<Integer> getRange(String attribute) {
		return new Range(getSize(attribute));
	}

	/**
	 * Dump the model
	 */
	@Override
	public String dump() {
		StringBuilder sb = new StringBuilder();
		for (String k : map.keySet()) {
			sb.append(dump(k));
		}
		return sb.toString();
	}

	/**
	 * Dump an attribute
	 */
	@Override
	public String dump(String attribute) {
		List<String> list = map.get(attribute);
		if (list == null)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(attribute).append("=");
		for (String s : list) {
			sb.append(s).append(",");
		}
		sb.setCharAt(sb.length()-1, '\n');
		return sb.toString();
	}

}
