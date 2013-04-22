package wcs.java.util;

import java.util.Date;

public class IfNull {
	
	public static String ifn(String s) {
		return s == null ? "" : s;
	}
	
	public static String ifn(String s, String ifn) {
		return s == null ? ifn : s;
	}

	public static long ifn(Long l) {
		return l == null ? 0l : l;
	}
	
	public static long ifn(Long l, Long ifn) {
		return l == null ? ifn : l;
	}
	
	public static int ifn(Integer i) {
		return i == null ? 0 : i;
	}
	
	public static long ifn(Integer i, Integer ifn) {
		return i == null ? ifn : i;
	}

	
	public static Date ifn(Date d, Date ifn) {
		return d == null ?  ifn : d;
	}


	
	public static Object ifn(Object o, Object ifn) {
		return o == null ?  ifn : o;
	}

	
}
