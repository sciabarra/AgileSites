package wcs.api;

import COM.FutureTense.Interfaces.ICS;

public interface Config {

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public abstract String getBlobId(ICS ics);

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public abstract String getBlobUrl(ICS ics);

	/**
	 * Return blob table
	 * 
	 * @return
	 */
	public abstract String getBlobTable(ICS ics);

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