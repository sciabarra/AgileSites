package wcs.api;

import java.util.Date;

/**
 * Common interface to all the classes that provides content
 * 
 * @author msciab
 * 
 */
public interface Content {

	/**
	 * Check if the give attribute is a valid value
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean exists(String attribute);

	/**
	 * Check if the given attribute at the given position is a valid value
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean exists(String attribute, int pos);

	/**
	 * Return the first attribute of the the named attribute as a string, or
	 * null if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String getString(String attribute);

	/**
	 * Return the nth named attribute as a string, or null if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String getString(String attribute, int n);

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Integer getInt(String attribute);

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public Integer getInt(String attribute, int n);

	/**
	 * Get variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public Long getLong(String var);

	/**
	 * Get the nth variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public Long getLong(String var, int n);

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Date getDate(String attribute);

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public Date getDate(String attribute, int n);

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract int getSize(String attribute);

	/**
	 * Return an iterable of the indexes of the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract Iterable<Integer> getRange(String attribute);

	/**
	 * Return a string dump of this content
	 * 
	 * @param asset
	 * @return
	 */
	public String dump();

	/**
	 * Return a string dump of an attriute
	 * 
	 * @param asset
	 * @return
	 */
	public String dump(String attribute);

}
