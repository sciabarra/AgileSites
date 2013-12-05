package wcs.java;

import java.util.Date;

import wcs.core.Id;

/**
 * Main asset abstraction. You get an asset from an Env then use this class to
 * generate code.
 * 
 * @author msciab
 * 
 */
public abstract class AssetBase {

	// private static Log log = Log.getLog(AssetCore.class);

	protected Id id;
	protected String c;
	protected Long cid;
	protected String name;
	protected String description;
	protected String subtype;
	protected String site;
	protected String filename;
	protected String path;
	protected Date startDate;
	protected Date endDate;
	protected String template;

	/**
	 * Create an asset with a given type, subtype and name.
	 * 
	 * @param type
	 * @param subtype
	 * @param name
	 */
	public AssetBase(String type, String subtype, String name) {
		this.name = name;
		this.description = name;
		this.c = type;
		this.subtype = subtype == null ? "" : subtype;
	}

	
	protected AssetBase() { }
	
	
	/**
	 * Retun current site
	 * 
	 * @return
	 */
	public String getSite() {
		return site;
	}

	/**
	 * Return the id of the asset
	 */
	public Id getId() {
		if (id == null)
			id = new Id(c, cid);
		return id;
	}

	/**
	 * Return the c (type) of the asset
	 */
	public String getC() {
		return c;
	}

	/**
	 * Return the cid of the asset
	 */
	public Long getCid() {
		return cid;
	}

	/**
	 * Return the template of the asset
	 * 
	 * @return
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Return the subtype of the asset
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
	 * Return the name of the asset
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the description of the asset
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		if (description == null)
			this.description = name;
		else
			this.description = description;
	}

	/**
	 * Return the filename of the asset
	 * 
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Return the path of the asset
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Return the start date of the asset
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Return the end date of the asset
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
}
