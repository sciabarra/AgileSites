package wcs.api;

import java.util.Date;


public interface Asset extends Content {

	/**
	 * Return the current site name
	 * 
	 * @return
	 */
	public abstract String getSite();

	/**
	 * The current asset id
	 * 
	 * @return
	 */
	public abstract Id getId();

	/**
	 * The current asset type
	 * 
	 * @return
	 */
	public abstract String getC();

	/**
	 * The current id, or null if undefined
	 * 
	 * @return
	 */
	public abstract Long getCid();

	/**
	 * The current template or null if undefined
	 * 
	 * @return
	 */
	public abstract String getTemplate();

	/**
	 * Range of an asset association
	 */
	public abstract Iterable<Integer> getAssocRange(String assoc);

	/**
	 * Id of the first associated asset
	 */
	public abstract Long getAssocId(String assoc);

	/**
	 * Id of the nth associated asset
	 */
	public abstract Long getAssocId(String assoc, int pos);

	/**
	 * Type of the first associated asset
	 */
	public abstract String getAssocType(String assoc);

	/**
	 * Type of the nth associated asset
	 */
	public abstract String getAssocType(String assoc, int pos);

	/**
	 * The current asset subtype, or the void string if no subtype
	 * 
	 * @return
	 */
	public abstract String getSubtype();

	/**
	 * The current asset name
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * The current asset description, or the name if the description is
	 * undefined
	 * 
	 * @return
	 */
	public abstract String getDescription();

	/**
	 * Current asset file
	 * 
	 * @return
	 */
	public abstract String getFilename();

	/**
	 * Current asset path
	 * 
	 * @return
	 */
	public abstract String getPath();

	/**
	 * Current asset start date or null if undefined
	 * 
	 * @return
	 */
	public abstract Date getStartDate();

	/**
	 * Current asset end date or null if undefined
	 * 
	 * @return
	 */
	public abstract Date getEndDate();

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract int getSize(String attribute);

	/**
	 * Return the first attribute of the attribute list as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Long getCid(String attribute);

	/**
	 * Return a named field from the asset as a string.
	 * 
	 * @param name
	 * @return
	 */
	public String getFieldString(String name);

	/**
	 * Return a named field from the asset as a date.
	 * 
	 * @param name
	 * @return
	 */
	public Date getFieldDate(String name);

	/**
	 * Return a named field from the asset as an int.
	 * 
	 * @param name
	 * @return
	 */
	public int getFieldInt(String name);

	/**
	 * Return a named field from the asset as a long.
	 * 
	 * @param name
	 * @return
	 */
	public long getFieldLong(String name);	

	/**
	 * Return the related asset pointed by the attribute of the given type.
	 * 
	 * Specify the dependency type you are going to use when accessing this
	 * asset.
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Asset getAsset(String attribute, String type,
			AssetDeps logdep);

	/**
	 * Return the related asset pointed by the attribute of the given type.
	 * 
	 * It does not log any dependencies - use only if you use to generate urls
	 * or retrieve other assets.
	 * 
	 */
	public abstract Asset getAsset(String attribute, String type);

	/**
	 * Return the related asset pointed by the nth attribute of the given type.
	 * 
	 * It does not log any dependencies - use only if you use to generate urls
	 * or retrieve other assets.
	 */
	public abstract Asset getAsset(String attribute, String type, int i);

	/**
	 * Return the related asset pointed by the nth attribute of the given type.
	 * 
	 * Since you are accessing another asset it is mandatory to specify the
	 * dependency type you are going to use.
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Asset getAsset(String attribute, int i, String type,
			AssetDeps logdep);

	/**
	 * String get blob url of the first attribute, with optional args
	 * 
	 */
	public abstract String getBlobUrl(String attribute, Arg... args);

	/**
	 * String get blob url of the first attribute, with optional args
	 * 
	 */
	public abstract String getBlobUrl(String attribute, String mimeType,
			Arg... args);

	/**
	 * String get blob url of the nth attribute, with optional args
	 */
	public abstract String getBlobUrl(String attribute, int pos,
			String mimeType, Arg... args);

	/**
	 * Return the nth attribute of the named attribute as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Long getCid(String attribute, int n);

	/**
	 * Return the first attribute of the the named attribute as a string, or
	 * null if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract String getString(String attribute);

	/**
	 * Return the nth named attribute as a string, or null if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract String getString(String attribute, int n);

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Integer getInt(String attribute);

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Integer getInt(String attribute, int n);

	/**
	 * Return the first attribute of the the attribute list as a long, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Long getLong(String attribute);

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Long getLong(String attribute, int n);

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Date getDate(String attribute);

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public abstract Date getDate(String attribute, int n);

	/**
	 * Return an iterable of the attribute list
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract Iterable<Integer> getRange(String attribute);

	/**
	 * Return the URL to render this asset
	 */
	public abstract String getUrl(Arg... args);

	/**
	 * Does the attribute exist?
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract boolean exists(String attribute);

	/**
	 * Does the nth attribute exist?
	 * 
	 * @param attribute
	 * @param pos
	 * @return
	 */
	public abstract boolean exists(String attribute, int pos);

	/**
	 * Render the attribute as an editable string in insite mode.
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract String editString(String attribute);

	/**
	 * Render the nth attribute as an editable string in insite mode.
	 * 
	 * @param attribute
	 * @param n
	 * @return
	 */
	public abstract String editString(String attribute, int n);

	/**
	 * Edit the n-th element of the given attribute, using the given parameters.
	 * 
	 * @param attribute
	 * @param n
	 * @param args
	 * @return
	 */
	public abstract String editString(String attribute, int n, String params,
			Arg... args);

	/**
	 * Edit (or return if not insite) the nth named attribute as a string, or
	 * null if not found and pass additional parameters using the CK editor
	 * 
	 * @param asset
	 * @return
	 */
	public abstract String editText(String attribute, int n, String params);

	/**
	 * Edit (or return if not insite) the first named attribute as a string, or
	 * null if not found and pass additional parameters
	 * 
	 * @param asset
	 * @param args
	 * @return
	 */
	public abstract String editString(String attribute, String params,
			Arg... args);

	/**
	 * Edit (or return if not insite) the first named attribute as a string, or
	 * null if not found using the CK editor
	 * 
	 * @param asset
	 * @return
	 */
	public abstract String editText(String attribute, String params);

	/**
	 * Invoke the template using the current asset as current asset, optionally
	 * passing a set of parameters
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public abstract String call(String name, Arg... args);

	/**
	 * Render a list of slots pointed by the asset field using the the specified
	 * template.
	 * 
	 * @param field
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	public abstract String slotList(String field, String type, String template,
			Arg... args) throws IllegalArgumentException;

	/**
	 * Render an empty slot.
	 */
	public abstract String slotEmpty(String attribute, String type,
			String template, String emptyText) throws IllegalArgumentException;

	/**
	 * Render a single slot pointed by the i-th asset field using the the
	 * specified template.
	 * 
	 * 
	 * @param attribute
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	public abstract String slot(String attribute, int i, String type,
			String template, String emptyText, Arg... args)
			throws IllegalArgumentException;

	/**
	 * Render a single slot pointed by the first asset field using the the
	 * specified template.
	 * 
	 * 
	 * @param attribute
	 * @param template
	 *            type
	 * @param template
	 * @param args
	 * @return
	 */
	public abstract String slot(String attribute, String type, String template,
			String emptyText, Arg... args) throws IllegalArgumentException;

}