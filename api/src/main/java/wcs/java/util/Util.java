package wcs.java.util;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import wcs.core.Log;
import wcs.core.Sequencer;

import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.openmarket.xcelerate.asset.AttributeDefImpl;

/**
 * Utility and support data
 * 
 * TODO: stub only class for now
 * 
 * @author msciab
 * 
 */
public class Util {

	private static Log log = Log.getLog(Util.class);

	/**
	 * Create a list from multiple arguments
	 * 
	 * @param objs
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List list(Object... objs) {
		List result = new ArrayList();
		for (Object obj : objs)
			result.add(obj);
		// System.out.println(result);
		return result;
	}

	/**
	 * Create a list from multiple arguments
	 * 
	 * @param objs
	 * @return
	 */
	public static List<String> listString(String... objs) {
		List<String> result = new ArrayList<String>();
		for (String obj : objs)
			result.add(obj);
		// System.out.println(result);
		return result;
	}

	/**
	 * Create a list from multiple arguments
	 * 
	 * @param objs
	 * @return
	 */
	public static List<AssetData> listData(AssetData... objs) {
		List<AssetData> result = new ArrayList<AssetData>();
		for (AssetData obj : objs)
			result.add(obj);
		// System.out.println(result);
		return result;
	}

	/**
	 * from an array to a list
	 * 
	 * @param array
	 * @return
	 */
	public static List<Object> array2list(Object[] array) {
		List<Object> ls = new ArrayList<Object>();
		for (Object s : array)
			ls.add(s);
		return ls;
	}

	/**
	 * from an array to a list
	 * 
	 * @param array
	 * @return
	 */
	public static List<String> array2listString(String[] array) {
		List<String> ls = new ArrayList<String>();
		for (String s : array)
			ls.add(s);
		return ls;
	}

	public static String dumpAssetData(AssetData data) {
		String s = new com.thoughtworks.xstream.XStream().toXML(data);
		try {
			FileWriter fw = new FileWriter("/tmp/out.xml", true);
			fw.write(s);
			fw.close();
		} catch (Exception ex) {
		}

		return s;
	}

	private static AttributeDefImpl def(String name, AttributeTypeEnum type) {
		return new AttributeDefImpl(name, type);
	}

	/**
	 * String attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AttributeDataImpl attrString(String name, String value) {
		AttributeTypeEnum type = AttributeTypeEnum.STRING;
		return new AttributeDataImpl(def(name, type), name, type, value);
	}

	/**
	 * Blob Attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AttributeDataImpl attrBlob(String name, BlobObject value) {
		AttributeTypeEnum type = AttributeTypeEnum.URL;
		return new AttributeDataImpl(def(name, type), name, type, value);
	}

	/**
	 * Structure default arguments
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static AttributeDataImpl attrStructKV(String name, String value) {
		HashMap map = new HashMap<String, Object>();
		map.put("name", attrString("name", name));
		map.put("value", attrString("value", value));
		return attrStruct("Structure defaultarguments", map);
	}

	/**
	 * Struct (map) attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AttributeDataImpl attrStruct(String name,
			Map<String, Object> value) {
		AttributeTypeEnum type = AttributeTypeEnum.STRUCT;
		return new AttributeDataImpl(def(name, type), name, type, value);
	}

	/**
	 * Array (list) attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AttributeDataImpl attrArray(String name, Object... value) {
		AttributeTypeEnum type = AttributeTypeEnum.ARRAY;
		System.out.println("#####" + array2list(value));
		return new AttributeDataImpl(def(name, type), name, type,
				(Object) array2list(value));

	}

	/**
	 * Blob Attribute
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static AttributeDataImpl attrBlob(String name, String folder,
			String filename, String value) {
		AttributeTypeEnum type = AttributeTypeEnum.URL;
		return new AttributeDataImpl(def(name, type), name, type,
				new BlobObjectImpl(folder + "/" + filename, folder,
						value.getBytes()));

	}

	/**
	 * Get as a date
	 */
	private static SimpleDateFormat fmt = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static java.util.Date toDate(String s) {
		if (s != null) {
			try {
				return fmt.parse(s);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	public static Long toLong(String l) {
		if (l == null)
			return null;
		try {
			long ll = Long.parseLong(l);
			return new Long(ll);
		} catch (NumberFormatException ex) {
			return null;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}

	}

	public static Integer toInt(String l) {
		if (l == null)
			return null;
		try {
			int ll = Integer.parseInt(l);
			return new Integer(ll);
		} catch (NumberFormatException ex) {
			return null;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}

	}

	/**
	 * Return a class list read from a list of resources
	 * 
	 * @param site
	 * @param resourceName
	 * @return
	 */
	public static Class<?>[] classesFromResource(String site,
			String resourceName) {
		String res = "/" + wcs.core.WCS.normalizeSiteName(site) + "/"
				+ resourceName;
		log.debug("res=" + res);
		List<Class<?>> classList = new LinkedList<Class<?>>();
		try {
			InputStream is = Util.class.getResourceAsStream(res);
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			String className = br.readLine();
			while (className != null) {
				log.debug("read " + className);
				try {
					if (className.trim().length() > 0)
						classList.add(Class.forName(className));
				} catch (Exception e) {
					log.warn("oops! cannot create " + className);
				}
				className = br.readLine();
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
			// e.printStackTrace();
		}
		return classList.toArray(new Class<?>[0]);
	}

	/**
	 * Get a resource from the classpath
	 * 
	 * @param res
	 * @return
	 */
	public static String getResource(String res) {
		InputStream is = null;
		try {
			return new java.util.Scanner(
					is = Util.class.getResourceAsStream(res)).useDelimiter(
					"\\A").next();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Get a resource property file from the classpath
	 * 
	 * @param res
	 * @return
	 */
	public static Properties getResourceProperties(String res) {
		InputStream is = null;
		Properties prp = new Properties();
		try {
			is = Util.class.getResourceAsStream(res);
			prp.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
		return prp;
	}

	/**
	 * Convenience method to dump the stream resulting of the picker
	 */
	public static String dumpStream(String html) {
		Sequencer seq = new Sequencer(html);
		StringBuilder sb = new StringBuilder(seq.header());
		while (seq.hasNext()) {
			sb.append(seq.next().toString());
			sb.append(seq.header());
		}
		return sb.toString();
	}
}
