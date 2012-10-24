package wcs.java;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fatwire.assetapi.data.AssetData;

/**
 * Utility and support data
 * 
 * @author msciab
 * 
 */
public class Util {

	/**
	 * Id class
	 * 
	 * @author msciab
	 * 
	 */
	static class Id {
		public Id(String type, Long id) {
			this.type = type;
			this.id = id;
		}

		String type;
		Long id;
	}

	/**
	 * Arg class
	 * 
	 * @author msciab
	 * 
	 */
	static class Arg {
		public Arg(String name, String value) {
			this.name = name;
			this.value = value;
		}

		String name;
		String value;

	}

	/**
	 * Shortcut to create an id, to be used with a static import
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public static Id id(String type, long id) {
		return new Id(type, id);
	}

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

	/**
	 * Create a list from multiple arguments
	 * 
	 * @param objs
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List list(Object... objs) {
		List result = new LinkedList();
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
		List<String> result = new LinkedList<String>();
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
		List<AssetData> result = new LinkedList<AssetData>();
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
	public static List<String> array2list(String[] array) {
		List<String> ls = new ArrayList<String>();
		for (String s : array)
			ls.add(s);
		return ls;
	}

}
