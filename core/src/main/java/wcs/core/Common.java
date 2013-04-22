package wcs.core;

import java.util.List;

public class Common {

	private static long tmpVarCounter = System.currentTimeMillis();

	/**
	 * Generate a temporary var
	 * 
	 * @return
	 */
	public synchronized static String tmp() {
		++tmpVarCounter;
		return "_" + Long.toHexString(tmpVarCounter);
	}

	/**
	 * Create an arg
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg arg(String name, String value) {
		return new Arg(name, value);
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
	 * Create an encoded call
	 */
	public static String call(String name, List<Arg> args) {
		return Call.encode(name, args);
	}
	
}
