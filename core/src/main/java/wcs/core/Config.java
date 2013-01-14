package wcs.core;

import COM.FutureTense.Interfaces.ICS;

/**
 * Configuration by site
 * 
 * @author msciab
 * 
 */
public interface Config {

	/**
	 * initialize a config
	 * 
	 * @param ics
	 */
	public void init(ICS ics);

	/**
	 * Return the attribute type for a type
	 * 
	 * @param type
	 * @return
	 */
	public String getAttributeType(String type);

	/**
	 * Return the default template for urls
	 * 
	 * @param type
	 * @return
	 */
	public String getDefaultTemplate(String type);

	/**
	 * Get property
	 * 
	 */
	public String getProperty(String name);

}
