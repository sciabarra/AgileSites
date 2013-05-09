package wcs.java;

import java.util.Date;

import wcs.core.Arg;
import wcs.core.Id;
import wcs.core.Log;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public abstract class Asset {

	private static Log log = Log.getLog(Asset.class);

	private String name;
	private String description;
	private String type;
	private String subtype;
	private String site;
	private String filename;
	private String path;
	private Date startDate;
	private Date endDate;
	private String template;

	public Asset() {
	}

	/**
	 * Create an asset with a given type, subtype and name.
	 * 
	 * @param type
	 * @param subtype
	 * @param name
	 */
	public Asset(String type, String subtype, String name) {
		this.name = name;
		this.description = name;
		setTypeSubtype(type, subtype);
	}

	protected void setTypeSubtype(String type, String subtype) {
		this.type = type;
		this.subtype = subtype;
	}

	/**
	 * Return the current site name
	 * 
	 * @return
	 */
	public String getSite() {
		return site;
	}

	/**
	 * Set the current site
	 * 
	 * @param site
	 */
	void setSite(String site) {
		this.site = site;
	}

	/**
	 * The current asset id
	 * 
	 * @return
	 */
	public Id getId() {
		return null;
	}

	/**
	 * The current asset type
	 * 
	 * @return
	 */
	public String getC() {
		return type;
	}

	/**
	 * The current id, or null if undefined
	 * 
	 * @return
	 */
	public Long getCid() {
		return null;
	}

	/**
	 * The current template or null if undefined
	 * 
	 * @return
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Range of an asset association
	 */
	public Iterable<Integer> getAssocRange(String assoc) {
		return new wcs.core.Range(0);
	}

	/**
	 * Id of the first associated asset
	 */
	public Long getAssocId(String assoc) {
		return null;
	}

	/**
	 * Id of the nth associated asset
	 */
	public Long getAssocId(String assoc, int pos) {
		return null;
	}

	/**
	 * Type of the first associated asset
	 */
	public String getAssocType(String assoc) {
		return null;
	}

	/**
	 * Type of the nth associated asset
	 */
	public String getAssocType(String assoc, int pos) {
		return null;
	}

	/**
	 * The current asset subtype, or the void string if no subtype
	 * 
	 * @return
	 */
	public String getSubtype() {
		if (subtype == null)
			return "";
		else
			return subtype;
	}

	/**
	 * The current asset name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * The current asset description, or the name if the description is
	 * undefined
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null)
			this.description = name;
		else
			this.description = description;
	}

	/**
	 * Current asset file
	 * 
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Current asset path
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Current asset start date or null if undefined
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Current asset end date or null if undefined
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * Check if the attribute exist
	 * 
	 * @param asset
	 * @return
	 */
	public boolean isAttribute(String attribute) {
		return false;
	}

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public int getSize(String attribute) {
		return 0;
	}
	
	/**
	 * Return the first attribute of the attribute list as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Long getCid(String attribute) {
		return null;
	}
	
	/**
	 * Return the related asset pointed by the attribute of the given type
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Asset getAsset(String attribute, String type) {
		return null;
	}

	/**
	 * Return the related asset pointed by the nth attribute of the given type
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Asset getAsset(String attribute, int i, String type) {
		return null;
	}

	
	/**
	 * String get blob url of the first attribute, with optional args
	 * 
	 */
	public String getBlobUrl(String attribute, Arg... args) {
		return null;
	}

	/**
	 * String get blob url of the first attribute, with optional args
	 * 
	 */
	public String getBlobUrl(String attribute, String mimeType, Arg... args) {
		return null;
	}

	/**
	 * String get blob url of the nth attribute, with optional args
	 */
	public String getBlobUrl(String attribute, int pos, String mimeType,
			Arg... args) {
		return null;
	}

	/**
	 * Return the nth attribute of the named attribute as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Long getCid(String attribute, int n) {
		return null;
	}

	/**
	 * Return the first attribute of the the named attribute as a string, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String getString(String attribute) {
		return null;
	}
	
	/**
	 * Return the nth named attribute as a string, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public String getString(String attribute, int n) {
		return null;
	}

	/**
	 * Edit (or return if not insite) the first named attribute as a string, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String editString(String attribute) {
		return null;
	}

	/**
	 * Edit (or return if not insite) the nth named attribute as a string, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public String editString(String attribute, int n) {
		return null;
	}

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Integer getInt(String attribute) {
		return null;
	}

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public Integer getInt(String attribute, int n) {
		return null;
	}

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public Date getDate(String attribute) {
		return null;
	}

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	public Date getDate(String attribute, int n) {
		return null;
	}

	/**
	 * Return an iterable of the attribute list
	 * 
	 * @param attribute
	 * @return
	 */
	public Iterable<Integer> getRange(String attribute) {
		return new wcs.core.Range(0);
	}

	/**
	 * Edit the attribute
	 * 
	 * @param asset
	 * @return
	 * 
	 *         public String edit(String attribute, Arg...args) { return ""; }
	 */

	/**
	 * Call the template by name with current c/cid and extra some optional args
	 * 
	 */
	public String call(String name, Arg... args) {
		return "";
	}

	/**
	 * Render a list of slots pointed by the asset field using the the specified
	 * template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param field
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	public String slotList(String field, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return "";
	}

	/**
	 * Render a single slot pointed by the i-th asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param field
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	public String slot(String attribute, int i, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return "";
	}

	/**
	 * Render a single slot pointed by the first asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param attribute
	 * @param template type
	 * @param template
	 * @param args
	 * @return
	 */
	public String slot(String attribute, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return "";
	}

	
	/**
	 * Return the URL to render this asset 
	 */
	public String getUrl(Arg... args) {
		return null;
	}

	/**
	 * Print it
	 */
	public String toString() {
		return name
				+ "("
				+ type
				+ ((subtype != null && subtype.trim().length() > 0) ? "/"
						+ subtype : "") + ")";
	}

	/**
	 * Return the attribute
	 * 
	 * @param data
	 * @param key
	 * @param value
	 */
	void addAttribute(MutableAssetData data, String key, Object value) {
		log.debug(key + "=" + value);
		AttributeData attr = data.getAttributeData(key);
		if (attr != null)
			attr.setData(value);
		else {
			log.warn("no attribute for " + key);
		}
	}

}