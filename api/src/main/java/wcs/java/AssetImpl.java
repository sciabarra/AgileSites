package wcs.java;

import static wcs.java.util.Util.toDate;

import java.util.Date;
import java.util.List;

import wcs.java.tag.AssetTag;
import wcs.java.tag.AssetsetTag;
import wcs.java.tag.RenderTag;
import wcs.java.util.Log;
import wcs.java.util.Util;
import COM.FutureTense.Interfaces.ICS;

import com.fatwire.assetapi.data.MutableAssetData;

class AssetImpl extends wcs.java.Asset {

	private static Log log = new Log(Env.class);

	// the name of the asset
	private String a = Util.tmpVar();
	// name of the assetset (and the list prefix) initially null - set on

	// request
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
		String subtype = AssetTag.getsubtype().name(a).run(i, "OUTPUT");
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
			// System.out.println("*** assetset " + i.GetObj(as));
		}
		return as;
	}

	/**
	 * Lazily load in a list the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	private String at(String attribute) {
		log.debug("extracting attribute "+attribute);
		String attrList = as() + attribute.toUpperCase();
		if (i.GetList(attrList) == null) {
			String attrType = e.getConfig().getAttributeType(getType());
			AssetsetTag.getattributevalues(as, attribute, attrList)
					.typename(attrType).run(i);
		}
		return attrList;
	}

	/**
	 * Return the association lazily loading all the attributes on the first
	 * request request
	 * 
	 * @return
	 */
	private String ass(String assoc) {
		String assocList = as() + "_ASS_" + assoc.toUpperCase();
		if (i.GetList(assocList) == null) {
			AssetTag.children(assocList).objecttype(getType())
					.objectid(id.toString()).run(i);
		}
		return assocList;
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
	public String getFilename() {
		return AssetTag.get(a, "filename").run(i, "output");
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
	public Iterable<Integer> getRange(String attribute) {
		return e.getRange(at(attribute));
	}

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public int getSize(String attribute) {
		return e.getSize(at(attribute));
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
		return e.getLong(at(attribute), "value");
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
		return e.getLong(at(attribute), n, "value");
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
		return e.getString(at(attribute), "value");
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
		return e.getString(at(attribute), n, "value");
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
		return e.getInt(at(attribute), "value");
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
		return e.getInt(at(attribute), n, "value");
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
		return e.getDate(at(attribute), "value");
	}

	/**
	 * Range of an asset association
	 */
	public Iterable<Integer> getAssocRange(String assoc) {
		return e.getRange(ass(assoc));
	}

	/**
	 * Id of the first associated asset
	 */
	public Long getAssocId(String assoc) {
		return e.getLong(ass(assoc), "OID");
	}

	/**
	 * Id of the nth associated asset
	 */
	public Long getAssocId(String assoc, int pos) {
		return e.getLong(ass(assoc), pos, "OID");
	}

	/**
	 * Type of the first associated asset
	 */
	public String getAssocType(String assoc) {
		return e.getString(ass(assoc), "OTYPE");
	}

	/**
	 * Type of the nth associated asset
	 */
	public String getAssocType(String assoc, int pos) {
		return e.getString(ass(assoc), pos, "OTYPE");
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
		return e.getDate(at(attribute), n, "value");
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

	/**
	 * String get blob url of the first attribute
	 */
	public String getBlobUrl(String attribute) {
		return getBlobUrl(attribute, 1);
	}

	/**
	 * String get blob url of the nth attribute
	 */
	public String getBlobUrl(String attribute, int pos) {
		String tmp = Util.tmpVar();
		Config cfg = e.getConfig();
		Long blobWhere = this.getId(attribute, pos);
		RenderTag.getbloburl(tmp).blobtable(cfg.getBlobTable())
				.blobcol(cfg.getBlobUrl()).blobkey(cfg.getBlobId())
				.blobwhere(blobWhere.toString()).run(i);
		String res = i.GetVar("tmp");
		i.RemoveVar(tmp);
		return res;
	}

}
