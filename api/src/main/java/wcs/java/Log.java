package wcs.java;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class Log {

	String className;

	public Log(@SuppressWarnings("rawtypes") Class clazz) {
		this(clazz.getCanonicalName());
	}

	public Log(String className) {
		this.className = className;
	}

	void dump(String message) {
		System.out.println("[DUMP ](" + className + ") " + message);
	}

	void trace(String message) {
		System.out.println("[TRACE](" + className + ") " + message);
	}

	void debug(String message) {
		System.out.println("[DEBUG](" + className + ") " + message);
	}

	void info(String message) {
		System.out.println("[INFO ](" + className + ") " + message);
	}

	void warn(String message) {
		System.out.println("[WARN ](" + className + ") " + message);
	}

	void error(String message) {
		System.out.println("[ERROR](" + className + ") " + message);
	}

	String error(Exception e) {
		CharArrayWriter caw = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(caw));
		error("Exception: " + caw.toString());
		return caw.toString();
	}

	public static void dbg(String message) {
		System.out.println(" *** " + message);
	}

}
