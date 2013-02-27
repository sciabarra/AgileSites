package wcs.core;

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
	 * Create a call
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Call call(String target, Arg... args) {
		return new Call(target, args);
	}
	
	/**
	 * Create an id
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Id id(String c, Long cid) {
		return new Id(c,cid);
	}

}
