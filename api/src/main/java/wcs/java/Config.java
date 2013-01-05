package wcs.java;

import java.util.HashMap;

import COM.FutureTense.Interfaces.ICS;

import wcs.java.tag.BlobserviceTag;

/**
 * Configuration class.
 * 
 * @author msciab
 * 
 */
public class Config {

	private String blobId;
	private String blobUrl;
	private String blobTable;

	public Config(ICS ics) {
		if (ics != null) {
			String tmp = wcs.java.util.Util.tmpVar();
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
	}

	/**
	 * Return blob id field
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

	// single default instance that throws exceptions when used
	private static Config bomb = new Config(null);

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

	private static HashMap<String, Config> configCache = new HashMap<String, Config>();

	/**
	 * Return the config for a specific site
	 * 
	 * @param site
	 * @return
	 * @throws Exception
	 */
	public static Config getConfigBySite(String site) {

		// no site... using a default config that will throw exception when
		// invoked
		if (site == null)
			return bomb;

		Config config = configCache.get(site);
		if (config != null)
			return config;

		try {
			config = (Config) Class.forName(site.toLowerCase() + ".Config")
					.newInstance();
			configCache.put(site, config);
			return config;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;

	}
}
