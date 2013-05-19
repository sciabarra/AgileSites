package wcs.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Call {

	public final static String SEP = "\0";
	public final static String SEP2END = SEP + "~";
	public final static String SEP2START = "~" + SEP;

	private Map<String, String> map = new HashMap<String, String>();
	private String name = "";

	/**
	 * Create a call with name and parameters
	 * 
	 * @param name
	 * @param args
	 */
	public Call(String name, Arg... args) {
		this.name = name;
		if (args.length > 0)
			for (Arg arg : args)
				addArg(arg.name, arg.value);
	}

	public String getName() {
		return name;
	}

	/**
	 * Return the value of a parameter, then delete it (to be used only once)
	 * 
	 * @param key
	 * @return
	 */
	public String getOnce(String key) {
		String val = map.get(key);
		map.remove(key);
		return val;
	}

	/**
	 * Get the value of a parameter.
	 */
	public String get(String key) {
		return map.get(key);
	}

	/**
	 * Add an argument
	 */
	public void addArg(String name, String value) {
		if (name != null && value != null)
			map.put(name, value);
	}

	private String[] voidArStr = new String[0];

	/**
	 * Keys left
	 */
	public String[] keysLeft() {
		return (String[]) map.keySet().toArray(voidArStr);
	}

	/**
	 * Decode a call encoded as a string
	 * 
	 * @param encoded
	 * @return
	 */
	public static Call decode(String encoded) {

		// System.out.println("decoding "+encoded);

		StringTokenizer st = new StringTokenizer(encoded, SEP);

		String name = st.nextToken();
		Call call = new Call(name);

		while (st.hasMoreTokens()) {
			try {
				String k = URLDecoder.decode(st.nextToken(), "UTF-8");
				String v = URLDecoder.decode(st.nextToken(), "UTF-8");
				// out.println(">>>" + k + "=" + v);
				call.map.put(k, v);
			} catch (Exception ex) {
			}
		}
		return call;
	}

	/**
	 * Encode a call as a string
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public static String encode(String name, Arg... args) {
		StringBuilder sb = new StringBuilder();
		sb.append(SEP2START).append(name).append(SEP);
		for (Arg arg : args) {
			if (arg.value != null)
				sb.append(arg.name).append(SEP).append(arg.value).append(SEP);
		}
		sb.append(SEP2END);
		return sb.toString();
	}

	/**
	 * Encode the call as a string
	 */
	public String encode() {
		StringBuilder sb = new StringBuilder();
		sb.append(SEP2START).append(name).append(SEP);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			try {
				String k = URLEncoder.encode(entry.getKey(), "UTF-8");
				String v = URLEncoder.encode(entry.getValue(), "UTF-8");
				sb.append(k).append(SEP).append(v).append(SEP);
			} catch (UnsupportedEncodingException e) {
			}
		}
		sb.append(SEP2END);
		return sb.toString();
	}

	/**
	 * Encode a call as a string
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public static String encode(String name, List<Arg> args) {
		StringBuilder sb = new StringBuilder();
		// elements to call have the site name as a prefix
		sb.append(SEP2START).append(name).append(SEP);
		for (Arg arg : args) {
			if (arg.value != null)
				try {
					sb.append(URLEncoder.encode(arg.name, "UTF-8")).append(SEP);
					sb.append(URLEncoder.encode(arg.value, "UTF-8"))
							.append(SEP);
				} catch (Exception ex) {
				}
		}
		sb.append(SEP2END);
		return sb.toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(name.replace(':', '-')).append(" ");
		String[] keys = keysLeft();
		Arrays.sort(keys);
		for (String k : keys) {
			sb.append(k).append("=\"").append(map.get(k)).append("\" ");
		}
		if (sb.toString().endsWith(" "))
			sb.setLength(sb.length() - 1);
		sb.append("/>");
		return sb.toString();
	}
}
