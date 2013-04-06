package wcs.java.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

/**
 * Log class that logs to a socket if available
 * 
 * @author msciab
 * 
 */
public class Log {

	private String className;

	private Logger logger = null;

	private String e2s(Exception e) {
		if (e == null)
			return "";
		CharArrayWriter caw = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(caw));
		return "\n" + caw.toString();
	}

	private Log(Logger logger) {
		// this(logger.getName());
		this.logger = logger;
		this.className = logger.getName();
	}

	public void trace(String message, Object... args) {
		trace(null, message, args);
	}

	public void trace(Exception ex, String message, Object... args) {
		System.out.println("[TRACE]" + className + String.format(message, args)
				+ e2s(ex));
		if (logger != null && logger.isTraceEnabled())
			logger.trace(String.format(message, args), ex);
	}

	public void debug(String message, Object... args) {
		debug(null, message, args);
	}

	public void debug(Exception ex, String message, Object... args) {
		// System.out.println("[DEBUG]" + className + message + e2s(ex));
		if (logger != null && logger.isDebugEnabled())
			logger.debug(String.format(message, args), ex);
	}

	public void info(String message, Object... args) {
		info(null, message, args);
	}

	public void info(Exception ex, String message, Object... args) {
		System.out.println("[ INFO]" + className + message + e2s(ex));
		if (logger != null && logger.isInfoEnabled())
			logger.info(String.format(message, args), ex);
	}

	public void warn(String message, Object... args) {
		warn(null, message, args);
	}

	public void warn(Exception ex, String message, Object... args) {
		System.out.println("[WARN ]" + className + String.format(message, args)
				+ e2s(ex));
		if (logger != null)
			logger.warn(String.format(message, args), ex);
	}

	public void error(String message, Object... args) {
		error(null, message, args);
	}

	public void error(Exception ex, String message, Object... args) {
		System.out.print("[ERROR]" + className + String.format(message, args)
				+ e2s(ex));
		if (logger != null)
			logger.error(String.format(message, args), ex);
	}

	/**
	 * Get a logger
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(Class<?> clazz) {
		Logger logger;
		if (clazz != null)
			logger = Logger.getLogger(clazz.getCanonicalName());
		else
			logger = Logger.getRootLogger();
		return new Log(logger);
	}
}
