package wcs.java;

import java.sql.Date;
import java.util.List;

import wcs.java.Util.Id;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public abstract class Asset {
	private static Log log = new Log(Asset.class);

	private Id id;
	private String subtype;
	private String name;
	private String description;

	public Asset() {
	}

	public Asset(Id id, String subtype, String name, String description) {
		this.id = id;
		this.subtype = subtype;
		this.name = name;
		this.description = description;
	}

	/**
	 * Return this asset typesub
	 */
	public String getSubtype() {
		return subtype;
	}

	/**
	 * Return this asset id
	 * 
	 * @return
	 */
	public Id getId() {
		return id;
	}

	/**
	 * Return this asset name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Return the file field
	 */
	public String getFile() {
		// TODO
		return null;
	}

	/**
	 * Return the path field
	 */
	public String getPath() {
		// TODO
		return null;
	}

	/**
	 * Return the start date field
	 */
	public Date getStartDate() {
		// TODO
		return null;
	}

	/**
	 * Return the end date field
	 */
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
	 * 
	 * @param attribute
	 * @return
	 */
	public String getString(String attribute) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public List<String> getStrings(String attribute) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public Integer getInt(String attribute) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public List<Integer> getInts(String attribute) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public Date getDate(String attribute) {
		// TODO
		return null;
	}

	/**
	 * 
	 * @param attribute
	 * @return
	 */
	public List<Date> getDates(String attribute) {
		return null;
	}

	/**
	 * Return a list of expected attributes
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
		return name + "@" + id;
	}

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