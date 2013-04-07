package wcs.java;

import static wcs.core.Common.arg;
import static wcs.core.Common.tmp;
import static wcs.java.util.Util.toDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wcs.core.Arg;
import wcs.core.Common;
import wcs.core.Log;
import wcs.java.tag.AssetTag;
import wcs.java.tag.AssetsetTag;
import wcs.java.tag.RenderTag;
import COM.FutureTense.Interfaces.ICS;

class AssetImpl extends wcs.java.Asset {

	private static Log log = Log.getLog(Env.class);

	// the name of the asset
	private String a = tmp();
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
		AssetTag.load().name(a).type(c).objectid(cid.toString()).run(i);
		String subtype = AssetTag.getsubtype().name(a).eval(i, "OUTPUT");
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
			as = tmp();
			AssetsetTag.setasset().name(as).type(getType()).id(cid.toString())
					.run(i);
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
			AssetsetTag.getattributevalues().name(as).attribute(attribute)
					.listvarname(attrList).typename(attrType).run(i);
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
			AssetTag.children().code(assocList).type(getType())
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
		return AssetTag.get().name(a).field("name").eval(i, "output");
	}

	/**
	 * Return the description field of the asset
	 */
	@Override
	public String getDescription() {
		return AssetTag.get().name(a).field("description").eval(i, "output");
	}

	/**
	 * Return the description field of the asset
	 */
	@Override
	public String getTemplate() {
		return AssetTag.get().name(a).field("template").eval(i, "output");
	}

	/**
	 * Return the file field of the asset
	 */
	@Override
	public String getFilename() {
		return AssetTag.get().name(a).field("filename").eval(i, "output");
	}

	/**
	 * Return the path field of the asset
	 */
	@Override
	public String getPath() {
		return AssetTag.get().name(a).field("path").eval(i, "output");
	}

	/**
	 * Return the start date field of the asset
	 */
	@Override
	public Date getStartDate() {
		return toDate(AssetTag.get().name(a).field("startdate")
				.eval(i, "output"));
	}

	/**
	 * Return the end date field of the asset
	 */
	@Override
	public Date getEndDate() {
		return toDate(AssetTag.get().name(a).field("enddate").eval(i, "output"));
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

	/**
	 * String get blob url of the first attribute
	 * 
	 */
	@Override
	public String getBlobUrl(String attribute, Arg... args) {
		return getBlobUrl(attribute, 1, "application/octet-stream", args);
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
	public String getBlobUrl(String attribute, int pos, String mimeType,
			Arg... args) {

		Config cfg = e.getConfig();
		Config.BlobConfig bcfg = cfg.getBlobConfig(e.ics);
		Long blobWhere = this.getId(attribute, pos);

		// invoke tag
		RenderTag.Getbloburl tag = RenderTag.getbloburl()
				.blobtable(bcfg.getBlobTable()).blobcol(bcfg.getBlobUrl())
				.blobkey(bcfg.getBlobId()).blobwhere(blobWhere.toString());
		// set mime type
		if (mimeType != null & mimeType.trim().length() > 0)
			tag.blobheader(mimeType);

		// pass parameters
		for (Arg arg : args) {
			tag.set(arg.name.toUpperCase(), arg.value);
		}

		// run the tag
		return tag.eval(i, "outstr");
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
		return Common.call("RENDER:CALLTEMPLATE", list);
	}

	/**
	 * Return the URL to render this asset using the configured default template
	 */
	@Override
	public String getUrl(Arg... args) {
		return "TODO";
		// return getUrl(e.getConfig().getDefaultTemplate(c), args);
	}

	/**
	 * Return the URL to render this asset using a specified template
	 */
	@Override
	public String getUrl(String template, Arg... args) {

		String tid = i.GetVar("tid");
		String ttype = "Template";
		if (tid == null) {
			tid = i.GetVar("eid");
			ttype = "CSElement";
		}

		String res = RenderTag.gettemplateurl().tname(template).c(c)
				.cid(cid.toString()).site(i.GetVar("site")).tid(tid)
				.ttype(ttype).eval(i, "outstr");

		log.debug("getUrl: outstr=" + res);
		return res;
	}
}
