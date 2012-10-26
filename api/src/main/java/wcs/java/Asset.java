package wcs.java;

import java.sql.Date;
import java.util.List;
import wcs.java.Util.Qid;
import com.fatwire.assetapi.data.AssetData;

public abstract class Asset {

	private Qid qid;
	private String name;
	private String description;

	public Asset() {
	}

	public Asset(Qid qid, String name, String description) {
		this.qid = qid;
		this.name = name;
		this.description = description;
	}

	/**
	 * Return this asset id
	 * 
	 * @return
	 */
	public Qid getQid() {
		return qid;
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
		// TODO
		return null;
	}

	/**
	 * Define asset data for this asset
	 * 
	 * @return
	 */
	abstract void setData(AssetData data);

	public String toString() {
		return name + "@" + qid;
	}

}