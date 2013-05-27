package wcs.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Collections of utils supporting common idioms.
 * 
 * You need to import static wcs.core.Common.* to use them.
 * 
 * @author msciab
 * 
 */
public class Common {

	private static long tmpVarCounter = System.currentTimeMillis();

	/**
	 * Generate an unique temporary var name.
	 * 
	 * @return
	 */
	public synchronized static String tmp() {
		++tmpVarCounter;
		return "_" + Long.toHexString(tmpVarCounter);
	}

	/**
	 * Create an arg holder
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg arg(String name, String value) {
		return new Arg(name, value);
	}

	/**
	 * Create an array of Arg from a list of strings in the form key=value
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg[] args(String... args) {
		List<Arg> ls = new ArrayList<Arg>();
		for (String arg : args) {
			int pos = arg.indexOf("=");
			if (pos == -1)
				ls.add(new Arg(arg, ""));
			else
				ls.add(new Arg(arg.substring(0, pos), arg.substring(pos + 1)));
		}
		return ls.toArray(new Arg[0]);
	}

	/**
	 * Create an id
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Id id(String c, Long cid) {
		return new Id(c, cid);
	}

	/**
	 * Create an encoded call
	 */
	public static String call(String name, Arg... args) {
		return Call.encode(name, args);
	}

	/**
	 * Create an encoded call from a list of args
	 */
	public static String call(String name, List<Arg> args) {
		return Call.encode(name, args);
	}

	/**
	 * Guarantee a string will never be null, if parameters is null then it will
	 * be returned as the empty string.
	 * 
	 * @param s
	 * @return
	 */
	public static String nn(String s) {
		return s == null ? "" : s;
	}

	/**
	 * If null returns the alternative otherwise the object
	 * 
	 * @param obj
	 * @param alt
	 * @return
	 */
	public static Object ifn(Object obj, Object alt) {
		return obj == null ? alt : obj;
	}

}
