package wcs.java;

import wcs.java.util.Log;
import wcs.java.tag.BlobserviceTag;
import COM.FutureTense.Interfaces.ICS;

/**
 * Base class for configuration, to be extended site by site
 * 
 * @author msciab
 * 
 */
public class Config implements wcs.core.Config {

	private static Log log = new Log(Config.class);

	private String blobId;
	private String blobUrl;
	private String blobTable;

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
	 * INitialize a config
	 * 
	 * @param ics
	 */
	public void init(ICS ics) {
		String tmp = "_TMP_" + System.currentTimeMillis();
		BlobserviceTag.getidcolumn(tmp + "id").run(ics);
		BlobserviceTag.geturlcolumn(tmp + "url").run(ics);
		BlobserviceTag.gettablename(tmp + "tbl").run(ics);
		blobId = ics.GetVar(tmp + "id");
		blobUrl = ics.GetVar(tmp + "url");
		blobTable = ics.GetVar(tmp + "tbl");
		ics.RemoveVar(tmp + "id");
		ics.RemoveVar(tmp + "url");
		ics.RemoveVar(tmp + "tbl");
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
