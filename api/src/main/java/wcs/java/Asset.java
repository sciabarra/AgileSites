package wcs.java;

import java.sql.Date;
import java.util.List;

import wcs.java.util.Log;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public abstract class Asset {

	private static Log log = new Log(Asset.class);

	private String name;
	private String description;
	private String type;
	private String subtype;
	private String site;

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
		this.type = type;
		this.subtype = subtype;
		this.name = name;
		this.description = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null)
			this.description = name;
		else
			this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getSubtype() {
		if (subtype == null)
			return "";
		else
			return subtype;
	}

	public String getName() {
		return name;
	}

	public String getFile() {
		// TODO
		return null;
	}

	public String getPath() {
		// TODO
		return null;
	}

	public Date getStartDate() {
		// TODO
		return null;
	}

	public Date getEndDate() {
		// TODO
		return null;
	}

	/**
	 * Read an attribute asset and return an asset id
	 * 
	 * @param asset
	 * @return
	 */
	public String getAssetId(String asset) {
		// TODO
		return null;
	}

	/**
	 * Read an attribute asset multivalued and a list of asset id
	 * 
	 * @param asset
	 * @return
	 */
	public List<String> getAssetIds(String asset) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as string
	 * 
	 * @param attribute
	 * @return
	 */
	public String getString(String attribute) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as a list of strings
	 * 
	 * @param attribute
	 * @return
	 */
	public List<String> getStrings(String attribute) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as an int
	 * 
	 * 
	 * @param attribute
	 * @return
	 */
	public Integer getInt(String attribute) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as a list of int
	 * 
	 * @param attribute
	 * @return
	 */
	public List<Integer> getInts(String attribute) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as a date
	 * 
	 * @param attribute
	 * @return
	 */
	public Date getDate(String attribute) {
		// TODO
		return null;
	}

	/**
	 * Return an attribute as a list of dates
	 * 
	 * 
	 * @param attribute
	 * @return
	 */
	public List<Date> getDates(String attribute) {
		return null;
	}

	/**
	 * Return a list of expected attributes
	 * 
	 */
	abstract List<String> getAttributes();

	/**
	 * Define asset data for this asset
	 * 
	 * @return
	 */
	abstract void setData(MutableAssetData data);

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