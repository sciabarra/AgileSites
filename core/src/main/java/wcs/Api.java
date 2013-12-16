package wcs;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wcs.api.Arg;
import wcs.api.Args;
import wcs.api.Call;
import wcs.api.Content;
import wcs.api.Env;
import wcs.api.Id;
import wcs.api.Log;
import wcs.api.Model;
import wcs.core.Sequencer;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Collections of utils supporting common idioms.
 * 
 * You need to import static wcs.core.Common.* to use them.
 * 
 * @author msciab
 * 
 */
public class Api {
	private static long tmpVarCounter = System.currentTimeMillis();

	/**
	 * Access to the env in a JSP
	 */
	public static Env env(ICS ics) {
		return wcs.core.WCS.getEnv(ics, "wcs.java.Env");
	}
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
	 * Create list of args
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Args args(String name, String... values) {
		return new Args(name, values);
	}

	/**
	 * Create a Model
	 */
	public static Model model(Arg... args) {
		return new Model(args);
	}

	/**
	 * Create a Model
	 */
	public static Model model(Model m, Arg... args) {
		return new Model(m, args);
	}

	/**
	 * Create an array of Arg from a list of strings in the form key=value
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg[] argv(String... args) {
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

	// formatter for fatwire format
	private static SimpleDateFormat fmt = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * Convert as a date, eventually the day 0 of the epoch if you cannot
	 * convert
	 */
	public static java.util.Date toDate(String s) {
		if (s != null) {
			try {
				return fmt.parse(s);
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 
	 * Convert in a long, 0l if errors
	 * 
	 * @param l
	 * @return
	 */
	public static Long toLong(String l) {
		if (l == null)
			return null;
		try {
			long ll = Long.parseLong(l);
			return new Long(ll);
		} catch (NumberFormatException ex) {
			return null;
		} catch (Exception ex) {
			// log.warn(ex, "unexpected");
		}
		return null;
	}

	/**
	 * Convert in a int, 0 if erros
	 * 
	 * @param l
	 * @return
	 */
	public static Integer toInt(String l) {
		if (l == null)
			return null;
		try {
			int ll = Integer.parseInt(l);
			return new Integer(ll);
		} catch (NumberFormatException ex) {
		} catch (Exception ex) {
			// log.warn(ex, "unexpected");
		}
		return null;
	}

	/**
	 * Convenience method to dump the stream resulting of the picker
	 */
	public static String dumpStream(String html) {
		Sequencer seq = new Sequencer(html);
		StringBuilder sb = new StringBuilder(seq.header());
		while (seq.hasNext()) {
			Call call = seq.next();
			sb.append(call.toString());
			sb.append(seq.header());
		}
		return sb.toString();
	}

	/**
	 * Convenience method to dump an IList
	 */
	public static String dumpIList(IList ilist) {
		if (ilist == null)
			return "NULL ILIST!";
		StringBuffer sb = new StringBuffer("*****\n");
		for (int i = 1; i <= ilist.numRows(); i++) {
			sb.append(i + ") ");
			for (int j = 0; j < ilist.numColumns(); j++) {
				String k = ilist.getColumnName(j);
				sb.append(k).append("=");
				try {
					sb.append(ilist.getValue(k).toString()).append(" ");
				} catch (NoSuchFieldException e) {
					// e.printStackTrace();
				}
			}
			ilist.moveTo(i);
			sb.append("\n");
		}
		sb.append("*****");
		return sb.toString();
	}

	/**
	 * Stream an exceptions in a string
	 */
	public static String ex2str(Throwable ex) {
		CharArrayWriter caw = new CharArrayWriter();
		ex.printStackTrace(new PrintWriter(caw));
		return caw.toString();
	}

	/**
	 * Print on standard output
	 */
	public static void out(String message, Object... args) {
		System.out.println(args.length > 0 ? String.format(message, args)
				: message);
	}

	/**
	 * Print on standard output some contents
	 */
	public static void out(String message, Content... contents) {
		System.out.println(message);
		for (Content c : contents) {
			System.out.println(c.dump());
		}
	}

	/**
	 * Print on standard output some contents
	 */
	public static void out(Content... contents) {
		for (Content c : contents) {
			System.out.println(c.dump());
		}
	}

	/**
	 * Print on standard output a content attributes
	 */
	public static void out(String message, Content content, String name) {
		System.out.println(message + ": " + content.dump(name));
	}

	/**
	 * Print on standard output a content attributes
	 */
	public static void out(Content content, String name) {
		System.out.println(content.dump(name));
	}
	
	/**
	 * Get a logger by name
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(String className) {
		return Log.getLog(className);
	}

	/**
	 * Get a logger by class
	 * 
	 * @param clazz
	 * @return
	 */
	public static Log getLog(Class<?> clazz) {
		return Log.getLog(clazz);
	}

}
