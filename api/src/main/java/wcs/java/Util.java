package wcs.java;

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
		public Id(String type, long id) {
			this.type = type;
			this.id = id;
		}
		String type;
		long id;
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
	 * Shortcut to create am arg, to be used with a static import 
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Arg arg(String name, String value) {
		return new Arg(name, value);
	}
	
}
