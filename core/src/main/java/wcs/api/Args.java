package wcs.api;

import java.util.LinkedList;
import java.util.List;


/**
 * Holder of a list of args
 * 
 * @author msciab
 *
 */
public class Args extends Arg {

	public List<String> values = new LinkedList<String>();

	// must be invoked with at least ONE argument and that is not controlled
	public Args(String name, String... args) {
		super(name, args[0]);
		for (String arg : args)
			values.add(arg);
	}

	/**
	 * Create an holder of a list of args, with at least one value mandatory 
	 * 
	 * @param name
	 * @param value
	 * @param rest
	 */
	public Args(String name, String value, String... rest) {
		super(name, value);
		values.add(value);
		for (String etc : rest)
			values.add(etc);
	}

}
