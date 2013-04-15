package wcs.java;

import java.io.IOException;
import java.util.Properties;

import wcs.core.WCS;
import wcs.core.tag.BlobserviceTag;
import COM.FutureTense.Interfaces.ICS;

/**
 * Base class for configuration, to be extended site by site
 * 
 * @author msciab
 * 
 */
abstract public class Config {

	static class BlobConfig {

		private String blobId;
		private String blobUrl;
		private String blobTable;

		/**
		 * Initialize a config
		 * 
		 * @param ics
		 */
		BlobConfig(ICS ics) {
			blobId = BlobserviceTag.getidcolumn().eval(ics, "VARNAME");
			blobUrl = BlobserviceTag.geturlcolumn().eval(ics, "VARNAME");
			blobTable = BlobserviceTag.gettablename().eval(ics, "VARNAME");
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
	}

	private BlobConfig blobConfig;

	/**
	 * Return the blob config - ICS needed to get this properly
	 * 
	 * @param type
	 * @return
	 */
	public BlobConfig getBlobConfig(ICS ics) {
		if (blobConfig == null)
			blobConfig = new BlobConfig(ics);
		return blobConfig;
	}

	private Properties properties = null;

	/**
	 * Get property from AgileSites config
	 * 
	 */
	public String getProperty(String name) {
		if (properties == null) {
			properties = new Properties();
			try {
				properties.load(Config.class
						.getResourceAsStream("/agilesites.properties"));
				System.out.println(properties.toString());
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return properties.getProperty(name);
	}

	// // STATIC PART ////

	/**
	 * Return a config, eventually cached. You can use both the site name or his
	 * normalized name for to get the config.
	 * 
	 * 
	 * @param site
	 * @return
	 */
	public static Config getConfig(String site) {
		try {
			return (Config) Class.forName(
					WCS.normalizeSiteName(site) + ".Config").newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// // ABSTRACT PART ////

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
