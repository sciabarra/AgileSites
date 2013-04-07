package wcs.core;

import org.apache.log4j.Logger;

/**
 * Log class that logs to a socket if available.
 * This class is a layer on top of Log4J but it is used in order to change implementation 
 * if needed at some point.
 * 
 * @author msciab
 * 
 */
public class Log {

	private Logger logger = null;

	/*
	private String className;
	private String e2s(Exception e) {
		if (e == null)
			return "";
		CharArrayWriter caw = new CharArrayWriter();
		e.printStackTrace(new PrintWriter(caw));
		return "\n" + caw.toString();
	}*/

	private Log(Logger logger) {
		this.logger = logger;
		//this.className = logger.getName();
	}

	public void trace(String message, Object... args) {
		trace(null, message, args);
	}

	public void trace(Exception ex, String message, Object... args) {
		//System.out.println("[TRACE]" + className + String.format(message, args) + e2s(ex));
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
		//System.out.println("[ INFO]" + className + message + e2s(ex));
		if (logger != null && logger.isInfoEnabled())
			logger.info(String.format(message, args), ex);
	}

	public void warn(String message, Object... args) {
		warn(null, message, args);
	}

	public void warn(Exception ex, String message, Object... args) {
		//System.out.println("[WARN ]" + className + String.format(message, args) + e2s(ex));
		if (logger != null)
			logger.warn(String.format(message, args), ex);
	}

	public void error(String message, Object... args) {
		error(null, message, args);
	}

	public void error(Exception ex, String message, Object... args) {
		//System.out.print("[ERROR]" + className + String.format(message, args) + e2s(ex));
		if (logger != null)
			logger.error(String.format(message, args), ex);
	}

	/**
	 * Get a logger by name
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(String className) {
		Logger logger;
		if (className != null)
			logger = Logger.getLogger(className);
		else
			logger = Logger.getRootLogger();
		return new Log(logger);
	}

	
	/**
	 * Get a logger by class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(Class<?> clazz) {
		return getLog(clazz==null ? null : clazz.getCanonicalName());
	}
}
