package wcs.java;

import wcs.java.tag.BlobserviceTag;
import COM.FutureTense.Interfaces.ICS;

/**
 * Base class for configuration, to be extended site by site
 * 
 * @author msciab
 * 
 */
public class Config implements wcs.core.Config {

	// private static Log log = new Log(Config.class);

	private String blobId;
	private String blobUrl;
	private String blobTable;
	private ICS ics;

	/**
	 * Create an unitialized config - init must be called afterwards to complete
	 * initalization
	 */
	public Config() {
	}

	/**
	 * Create and initalize a config
	 * 
	 * @param ics
	 */
	public Config(ICS ics) {
		init(ics);
	}

	/**
	 * Initialize a config
	 * 
	 * @param ics
	 */
	@Override
	public void init(ICS ics) {
		this.ics = ics;
		blobId = BlobserviceTag.getidcolumn().eval(ics, "VARNAME");
		blobUrl = BlobserviceTag.geturlcolumn().eval(ics, "VARNAME");
		blobTable = BlobserviceTag.gettablename().eval(ics, "VARNAME");
	}

	/**
	 * Get property from agilewcs config or from the system.ini
	 * 
	 */
	public String getProperty(String name) {
		String val = wcs.core.WCS.getProperty(name);
		if (val != null)
			return val;
		return ics.GetProperty(name);
	}

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public String getBlobId() {
		return blobId;
	}

	/**
	 * Return blob id field
	 * 
	 * @return
	 */
	public String getBlobUrl() {
		return blobUrl;
	}

	/**
	 * Return blob table
	 * 
	 * @return
	 */
	public String getBlobTable() {
		return blobTable;
	}

	/**
	 * Return the attribute type for a given type.
	 * 
	 * @param type
	 * @return
	 */
	public String getAttributeType(String type) {
		throw new RuntimeException(
				"Invoking default config - you need to define a Config class for your site");
	}

	/**
	 * Return the default template to be used when rendering a site URL
	 */
	public String getDefaultTemplate(String type) {
		throw new RuntimeException(
				"Invoking default config - you need to define a Config class for your site");
	}
}
