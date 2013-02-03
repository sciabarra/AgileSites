package wcs.java;

import java.util.StringTokenizer;

import wcs.core.Arg;
import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.ICS;
import static java.lang.System.out;

/**
 * 
 * Element
 * 
 * @author msciab
 * 
 */
public abstract class Element implements wcs.core.Element {
	
	
	
	/**
	 * Shortcut to create an arg, to be used with a static import
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg arg(String name, String value) {
		return new Arg(name, value);
	}


	// separators
	private final static String sep = "\0";
	private final static String sep2 = sep + sep;

	// current site
	protected String site;

	/**
	 * Execute the element
	 * 
	 * The bulk of the method is streaming the result and invoking embedded
	 * method calls.
	 * 
	 */
	@Override
	public String exec(ICS ics) {
		try {
			site = ics.GetVar("site");
			Env env = new Env(ics);
			stream(ics, apply(env));
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	/**
	 * Stream the result of the apply with embedded calls
	 * 
	 * @param ics
	 * @param res
	 */
	protected void stream(ICS ics, String res) {
		out.println("\n=====\n" + res + "\n======\n");

		int start = res.indexOf(sep2);

		out.println("START=" + start);

		while (start != -1) {

			ics.StreamText(res.substring(0, start));

			int end = res.indexOf(sep2 + sep, start + 2);
			out.println("END=" + end);
			if (end == -1)
				end = res.length();

			String call = res.substring(start, end);

			out.println("ELEMENTCALL " + call);

			StringTokenizer st = new StringTokenizer(call, sep);

			String element = st.nextToken();
			FTValList list = new FTValList();

			out.print("CALL " + element);
			while (st.hasMoreTokens()) {
				try {
					String k = st.nextToken();
					String v = st.nextToken();
					out.print(" " + k + "=" + v);
					list.setValString(k, v);
				} catch (Exception ex) {
					out.println("OPS " + ex.getMessage());
				}
			}
			out.println();

			if (element.equals("!RCT"))
				ics.runTag("RENDER.CALLTEMPLATE", list);
			else if (element.equals("!ICT"))
				ics.runTag("INSITE.CALLTEMPLATE", list);
			else
				ics.CallElement(element, list);

			res = res.substring(end + 3);
			start = res.indexOf(sep2);
			out.println("START=" + start);
		}

		ics.StreamText(res);
	}

	/**
	 * Schedule the call to a specific element. Elements starting with "!" have
	 * special meaning
	 * 
	 * - !RCT will invoke a render:calltemplate
	 * 
	 * - !ICT will invoke a insite:calltemplate
	 * 
	 * otherwise it will invoke a ics.Callelement
	 * 
	 * @param name
	 * @param args
	 */
	public static String scheduleCall(String name, Arg... args) {
		StringBuilder sb = new StringBuilder();
		// elements to call have the site name as a prefix
		sb.append(sep2).append(name).append(sep);
		for (Arg arg : args) {
			if (arg.value != null)
				sb.append(arg.name).append(sep).append(arg.value).append(sep);
		}
		sb.append(sep2);
		return sb.toString();
	}

	/**
	 * Call a cs element in the same site
	 * 
	 * @param name
	 * @return
	 */
	public String call(String name, Arg... args) {
		return scheduleCall(site + "/" + name, args);
	}

	

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	abstract public String apply(Env env);
}
