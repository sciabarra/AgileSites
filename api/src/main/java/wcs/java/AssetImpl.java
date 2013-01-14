package wcs.java;

import COM.FutureTense.Interfaces.ICS;

import static wcs.java.Element.scheduleCall;
import static wcs.java.Element.arg;

import static wcs.java.util.Util.toDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wcs.java.util.Arg;
import wcs.java.util.Log;
import wcs.java.util.Util;

import wcs.java.tag.AssetTag;
import wcs.java.tag.AssetsetTag;
import wcs.java.tag.RenderTag;

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

	private Long cid;
	private String c;

	public AssetImpl(Env env, String c, Long cid) {
		this.e = env;
		this.i = e.ics;
		this.c = c;
		this.cid = cid;
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
			AssetsetTag.setasset(as, getType(), cid.toString()).run(i);
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
		log.debug("extracting attribute " + attribute);
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
			AssetTag.children(assocList).type(getType())
					.assetid(cid.toString()).code(assoc).order("nrank").run(i);
		}
		return assocList;
	}

	/**
	 * Return the id of the asset
	 */
	@Override
	public Long getId() {
		return cid;
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
	@Override
	public Iterable<Integer> getRange(String attribute) {
		return e.getRange(at(attribute));
	}

	/**
	 * Return the number of elements in the attribute
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
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
	@Override
	public Iterable<Integer> getAssocRange(String assoc) {
		return e.getRange(ass(assoc));
	}

	/**
	 * Id of the first associated asset
	 */
	@Override
	public Long getAssocId(String assoc) {
		return e.getLong(ass(assoc), "oid");
	}

	/**
	 * Id of the nth associated asset
	 */
	@Override
	public Long getAssocId(String assoc, int pos) {
		return e.getLong(ass(assoc), pos, "oid");
	}

	/**
	 * Type of the first associated asset
	 */
	@Override
	public String getAssocType(String assoc) {
		return e.getString(ass(assoc), "otype");
	}

	/**
	 * Type of the nth associated asset
	 */
	@Override
	public String getAssocType(String assoc, int pos) {
		return e.getString(ass(assoc), pos, "otype");
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
	 * 
	 */
	@Override
	public String getBlobUrl(String attribute, String mimeType, Arg... args) {
		return getBlobUrl(attribute, 1, mimeType, args);
	}

	/**
	 * String get blob url of the nth attribute
	 */
	@Override
	public String getBlobUrl(String attribute, int pos, String mimeType, Arg... args) {
		String tmp = Util.tmpVar();
		Config cfg = e.getConfig();
		Long blobWhere = this.getId(attribute, pos);
		// invoke tag
		RenderTag.Getbloburl tag = RenderTag.getbloburl(tmp)
				.blobtable(cfg.getBlobTable()).blobcol(cfg.getBlobUrl())
				.blobkey(cfg.getBlobId()).blobwhere(blobWhere.toString());
		// set mime type
		if(mimeType!=null & mimeType.trim().length()>0)
			tag.blobheader(mimeType);
			
		// pass parameters
		for(Arg arg: args)  {
			tag.set(arg.name.toUpperCase(), arg.value);
		}
		// run the tag
		tag.run(i);
		String res = i.GetVar(tmp);
		i.RemoveVar(tmp);

		return res;
	}

	/**
	 * Call the template by name with current c/cid and extra some optional args
	 * 
	 */
	@Override
	public String call(String name, Arg... args) {
		List<Arg> list = new ArrayList<Arg>();
		list.add(arg("SITE", i.GetVar("site")));
		list.add(arg("C", c));
		list.add(arg("CID", cid.toString()));
		list.add(arg("TNAME", name));

		list.add(arg("TTYPE", i.GetVar("tid") != null //
		? "Template"
				: "CSElement"));
		list.add(arg("TID", i.GetVar("tid") != null //
		? i.GetVar("tid")
				: i.GetVar("eid")));

		// TODO: use the slot properly
		list.add(arg("SLOTNAME", name.replace('/', '_')));

		// copy additional args
		for (Arg arg : args)
			list.add(arg);
		return scheduleCall("!RCT", list.toArray(new Arg[0]));
	}

	/**
	 * Return the URL to render this asset using the configured default template
	 */
	@Override
	public String getUrl(Arg... args) {
		return getUrl(e.getConfig().getDefaultTemplate(c), args);
	}

	/**
	 * Return the URL to render this asset using a specified template
	 */
	@Override
	public String getUrl(String template, Arg... args) {

		String outstr = Util.tmpVar();
		String tid = i.GetVar("tid");
		String ttype = "Template";
		if (tid == null) {
			tid = i.GetVar("eid");
			ttype = "CSElement";
		}

		RenderTag.gettemplateurl(outstr, template).c(c).cid(cid.toString())
				.site(i.GetVar("site")).tid(tid).ttype(ttype).run(i);

		String res = getString(outstr);
		log.debug("getUrl: outstr="+res);
		i.RemoveVar(outstr);
		return res;
	}
}
