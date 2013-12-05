package wcs.core;

public interface Config {

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public abstract String getBlobId();

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public abstract String getBlobUrl();

	/**
	 * Return blob table
	 * 
	 * @return
	 */
	public abstract String getBlobTable();

	/**
	 * Get property from AgileSites config
	 * 
	 */
	public abstract String getProperty(String name);

	/**
	 * Return the attribute type for a given type.
	 * 
	 * @param type
	 * @return
	 */
	abstract public String getAttributeType(String type);

	/**
	 * Return the full site name
	 * 
	 * @param type
	 * @return
	 */
	abstract public String getSite();

}