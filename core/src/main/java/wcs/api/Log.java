package wcs.api;

import org.apache.log4j.Logger;

/**
 * Log class that logs to a socket if available. This class is a layer on top of
 * Log4J but it is used in order to change implementation if needed at some
 * point.
 * 
 * @author msciab
 * 
 */
public class Log {

	private Logger logger = null;

	private Log(Logger logger) {
		this.logger = logger;
		// this.className = logger.getName();
	}

	/**
	 * Log a trace level message - Use message as a format string for the other
	 * args.
	 * 
	 * @param message
	 * @param args
	 */
	public void trace(String message, Object... args) {
		trace(null, message, args);
	}

	/**
	 * Check if trace level is enabled
	 * 
	 * @return
	 */
	public boolean trace() {
		return logger.isTraceEnabled();
	}

	/**
	 * Log an exception then a trace level message - Use message as a format
	 * string for the other args.
	 * 
	 * @param message
	 * @param args
	 */
	public void trace(Throwable ex, String message, Object... args) {
		if (logger != null)
			logger.trace(args.length > 0 ? String.format(message, args)
					: message, ex);
	}

	/**
	 * Check if trace level is enabled
	 * 
	 * @return
	 */
	public boolean debug() {
		return logger.isDebugEnabled();
	}

	/**
	 * Write a debug message - Use message as a format string for the other
	 * args.
	 * 
	 * @param message
	 * @param args
	 */
	public void debug(String message, Object... args) {
		debug(null, message, args);
	}

	/**
	 * Log an exception then a debug level message - Use message as a format
	 * string for the other args.
	 * 
	 * @param message
	 * @param args
	 */
	public void debug(Throwable ex, String message, Object... args) {
		// System.out.println("[DEBUG]" + className + message + e2s(ex));
		if (logger != null && logger.isDebugEnabled())
			logger.debug(args.length > 0 ? String.format(message, args)
					: message, ex);
	}

	/**
	 * Log an info level message - Use message as a format string for the other
	 * args.
	 * 
	 * @param message
	 * @param args
	 */
	public void info(String message, Object... args) {
		info(null, message, args);
	}

	/**
	 * Log an exception then an info level message - Use message as a format
	 * string for the other args.
	 * 
	 * @param message
	 * @param args
	 */
	public void info(Throwable ex, String message, Object... args) {
		// System.out.println("[ INFO]" + className + message + e2s(ex));
		if (logger != null)
			logger.info(args.length > 0 ? String.format(message, args)
					: message, ex);
	}

	/**
	 * Check if info level is enabled
	 * 
	 * @return
	 */
	public boolean info() {
		return logger.isInfoEnabled();
	}

	/**
	 * Log a warn level message - Use message as a format string for the other
	 * args.
	 * 
	 * @param message
	 * @param args
	 */
	public void warn(String message, Object... args) {
		warn(null, message, args);
	}

	/**
	 * Log an exception then a warn level message - Use message as a format
	 * string for the other args.
	 * 
	 * @param message
	 * @param args
	 */
	public void warn(Throwable ex, String message, Object... args) {
		// System.out.println("[WARN ]" + className + String.format(message,
		// args) + e2s(ex));
		if (logger != null)
			logger.warn(args.length > 0 ? String.format(message, args)
					: message, ex);
	}

	/**
	 * Log an error level message - Use message as a format string for the other
	 * args.
	 * 
	 * @param message
	 * @param args
	 */

	public void error(String message, Object... args) {
		error(null, message, args);
	}

	/**
	 * Log an exception then a error level message - Use message as a format
	 * string for the other args.
	 * 
	 * @param message
	 * @param args
	 */
	public void error(Throwable ex, String message, Object... args) {
		// System.out.print("[ERROR]" + className + String.format(message, args)
		// + e2s(ex));
		if (logger != null)
			logger.error(args.length > 0 ? String.format(message, args)
					: message, ex);
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
		return getLog(clazz == null ? null : clazz.getCanonicalName());
	}

}
