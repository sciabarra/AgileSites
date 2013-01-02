package wcs.java;

import static wcs.java.util.Util.toDate;

import java.util.Date;
import java.util.List;

import wcs.java.tag.AssetTag;
import wcs.java.tag.AssetsetTag;
import wcs.java.util.Util;
import COM.FutureTense.Interfaces.ICS;

import com.fatwire.assetapi.data.MutableAssetData;

class AssetImpl extends wcs.java.Asset {

	// the name of the asset
	private String a = Util.tmpVar();
	// name of the assetset (and the list prefix) initially null - set on request
	private String as = null;
	// the env
	private Env e;
	// the ICS from the env
	private ICS i;

	private Long id;

	public AssetImpl(Env env, String c, Long cid) {
		this.e = env;
		this.i = e.ics;
		this.id = cid;
		AssetTag.load(a, c).objectid(cid.toString()).run(i);
		String subtype = AssetTag.getsubtype().name(a).run(i, "output");
		setTypeSubtype(c, subtype);
	}

	/**
	 * Return the assetset name, lazily loading all the attributes on the first
	 * request
	 * 
	 * @return
	 */
	private String as() {
		if (as == null) {
			as = Util.tmpVar();
			AssetsetTag.setasset(as, getType(), id.toString()).run(i);
			AssetsetTag.getmultiplevalues(as, as).run(i);
		}
		return as;
	}

	/**
	 * Return the id of the asset
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Return the name field of the asset
	 */
	@Override
	public String getName() {
		return AssetTag.get(a, "name").run(i, "output");
	}

	/**
	 * Return the description field of the asset
	 */
	@Override
	public String getDescription() {
		return AssetTag.get(a, "description").run(i, "output");
	}

	/**
	 * Return the file field of the asset
	 */
	@Override
	public String getFile() {
		return AssetTag.get(a, "file").run(i, "output");
	}

	/**
	 * Return the path field of the asset
	 */
	@Override
	public String getPath() {
		return AssetTag.get(a, "path").run(i, "output");
	}

	/**
	 * Return the start date field of the asset
	 */
	@Override
	public Date getStartDate() {
		return toDate(AssetTag.get(a, "startdate").run(i, "output"));
	}

	/**
	 * Return the end date field of the asset
	 */
	@Override
	public Date getEndDate() {
		return toDate(AssetTag.get(a, "enddate").run(i, "output"));
	}

	/**
	 * Return an iterable of the attribute list
	 * 
	 * @param attribute
	 * @return
	 */
	public Iterable<Integer> range(String attribute) {
		return e.range(as() + ":" + attribute);
	}

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public int getSize(String attribute) {
		return e.getSize(as() + ":" + attribute);
	}

	/**
	 * Return the first attribute of the attribute list as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Long getId(String attribute) {
		return e.getLong(as() + ":" + attribute, "value");
	}

	/**
	 * Return the nth attribute of the attribute list as an id (long), or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Long getId(String attribute, int n) {
		return e.getLong(as() + ":" + attribute, n, "value");
	}

	/**
	 * Return the first attribute of the the attribute rib as a string, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public String getString(String attribute) {
		return e.getString(as() + ":" + attribute, "value");
	}

	/**
	 * Return the nth attribute of the the attribute rib as a string, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public String getString(String attribute, int n) {
		return e.getString(as() + ":" + attribute, n, "value");

	}

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Integer getInt(String attribute) {
		return e.getInt(as() + ":" + attribute, "value");
	}

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Integer getInt(String attribute, int n) {
		return e.getInt(as() + ":" + attribute, n, "value");
	}

	/**
	 * Return the first attribute of the the attribute list as a date, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Date getDate(String attribute) {
		return e.getDate(as() + ":" + attribute, "value");
	}

	/**
	 * Return the nth attribute of the the attribute list as a date, or null if
	 * not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Date getDate(String attribute, int n) {
		return e.getDate(as() + ":" + attribute, n, "value");
	}

	@Override
	List<String> getAttributes() {
		throw new RuntimeException(
				"should not be called here - reserved for setup");
	}

	@Override
	void setData(MutableAssetData data) {
		throw new RuntimeException(
				"should not be called here - reserved for setup");
	}

}
