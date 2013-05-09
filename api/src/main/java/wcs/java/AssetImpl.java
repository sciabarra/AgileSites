package wcs.java;

import static wcs.core.Common.arg;
import static wcs.core.Common.tmp;
import static wcs.java.util.Util.toDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wcs.core.Arg;
import wcs.core.Call;
import wcs.core.Common;
import wcs.core.Id;
import wcs.core.Log;
import wcs.core.tag.AssetTag;
import wcs.core.tag.AssetsetTag;
import wcs.core.tag.RenderTag;
import wcs.java.util.IfNull;
import wcs.java.util.Util;
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
	// the config
	private Config cfg;

	private Long cid;
	private String c;
	private Id id;

	boolean insite = false;

	public AssetImpl(Env env, String c, Long cid) {
		this.e = env;
		this.i = e.ics;
		this.c = c;
		this.cid = cid;
		this.id = new Id(c, cid);
		this.cfg = e.getConfig();

		AssetTag.load().name(a).type(c).objectid(cid.toString()).run(i);
		String subtype = AssetTag.getsubtype().name(a).eval(i, "OUTPUT");
		setTypeSubtype(c, subtype);

		String rendermode = i.GetVar("rendermode");
		insite = rendermode != null && rendermode.equals("insite");
	}

	/**
	 * Return the assetset name, lazily loading all the attributes on the first
	 * request
	 * 
	 * @return
	 */
	private String as() {
		if (as == null) {
			as = tmp()+"_";
			AssetsetTag.setasset().name(as).type(getC()).id(cid.toString())
					.run(i);
		}
		return as;
	}

	/**
	 * Return the asset list name associated to an attribute
	 * 
	 * @param attribute
	 * @return
	 */

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
			String attrType = e.getConfig().getAttributeType(getC());
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
			AssetTag.children().code(assocList).type(getC())
					.assetid(cid.toString()).code(assoc).order("nrank").run(i);
		}
		return assocList;
	}

	/**
	 * Return the id of the asset
	 */
	@Override
	public Id getId() {
		return id;
	}

	/**
	 * Return the cid of the asset
	 */
	@Override
	public Long getCid() {
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
	 * Check if the attribute exist
	 * 
	 * @param asset
	 * @return
	 */
	public boolean isAttribute(String attribute) {
		return e.isList(at(attribute));
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
	public Long getCid(String attribute) {
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
	public Long getCid(String attribute, int n) {
		return e.getLong(at(attribute), n, "value");
	}

	
	/**
	 * Return the related asset pointed by the attribute of the given type
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Asset getAsset(String attribute, String type) {
		return e.getAsset(type, getCid(attribute));		
	}

	/**
	 * Return the related asset pointed by the nth attribute of the given type
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public Asset getAsset(String attribute, int i, String type) {
		return e.getAsset(type, getCid(attribute, i));		
	}

	
	/**
	 * Return the first attribute of the the attribute rib as a string, or the
	 * null if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public String getString(String attribute) {
		return e.getString(at(attribute), "value");
	}
	
	/**
	 * Edit (or return if not insite) the first named attribute as a string, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String editString(String attribute) {
		if (insite)
			return edit(attribute, 1);
		return getString(attribute);
	}
	
	/**
	 * Return the nth attribute of the the attribute rib as a string, or the
	 * void string if not found
	 * 
	 * @param asset
	 * @return
	 */
	@Override
	public String getString(String attribute, int n) {
		return e.getString(at(attribute), n, "value");
	}

	
	/**
	 * Edit (or return if not insite) the first named attribute as a string, or null
	 * if not found
	 * 
	 * @param asset
	 * @return
	 */
	public String editString(String attribute, int n) {
		if (insite)
			return edit(attribute, n);
		return getString(attribute, n);
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

		Long blobWhere = this.getCid(attribute, pos);
		if (blobWhere == null)
			return null;

		Config cfg = e.getConfig();
		Config.BlobConfig bcfg = cfg.getBlobConfig(e.ics);

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
	 * Invoke the actual slot call
	 * 
	 * if pos <0 it is a list otherwise it is a slot call.
	 * 
	 * 
	 * @param template
	 * @param attribute
	 * @param n
	 * @param args
	 * @return
	 * @throws IllegalArgumentException
	 */
	private String insiteCall(String type, String template, String attribute, int n,
			Arg... args) {

		try {
			// let's start with the common parameters
			List<Arg> list = new ArrayList<Arg>();
			list.add(arg("SITE", i.GetVar("site")));
			list.add(arg("TNAME", template));

			list.add(arg("TTYPE", //
					i.GetVar("tid") != null ? "Template" : "CSElement"));
			list.add(arg("TID", //
					i.GetVar("tid") != null ? i.GetVar("tid") : i.GetVar("eid")));

			list.add(arg("ASSETTYPE", c));
			list.add(arg("ASSETID", cid.toString()));
			list.add(arg("FIELD", attribute));
			
			// copy additional args
			for (Arg arg : args)
				list.add(arg);

			list.add(arg("CHILDTYPE", type));
			if (n < 0) {
				list.add(arg("LISTNAME", at(attribute)));
				return Common.call("INSITE:CALLTEMPLATELOOP", list);
			} else {
				long icid = IfNull.ifn(getCid(attribute, n), 0l);
				list.add(arg("CHILDID", Long.toString(icid)));
				list.add(arg("INDEX", Integer.toString(n)));
				return Common.call("INSITE:CALLTEMPLATE", list);
			}
		} catch (Exception ex) {
			log.error(ex, "exception in insiteCall");
			return "ERROR " + ex.getMessage();
		}
	}

	/**
	 * Call the template by name with current c/cid, specifiying a slot name and
	 * eventually some extra optional args.
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	@Override
	public String call(String template, Arg... args) {
		// let's start with the common parameters
		List<Arg> list = new ArrayList<Arg>();
		list.add(arg("SITE", i.GetVar("site")));
		list.add(arg("TNAME", template));
		list.add(arg("C", c));
		list.add(arg("CID", cid.toString()));
		list.add(arg("TTYPE", i.GetVar("tid") != null ? "Template"
				: "CSElement"));
		list.add(arg("TID",
				i.GetVar("tid") != null ? i.GetVar("tid") : i.GetVar("eid")));
		return Common.call("RENDER:CALLTEMPLATE", list);
	}

	/**
	 * Render a list of slots pointed by the asset field using the the specified
	 * template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param field
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	@Override
	public String slotList(String attribute, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return insiteCall(type, template, attribute, -1, args);
	}

	/**
	 * Render a single slot pointed by the i-th asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param field
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	@Override
	public String slot(String attribute, int i, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return insiteCall(type, template, attribute, i, args);
	}

	/**
	 * Render a single slot pointed by the first asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param field
	 * @param template
	 * @param args
	 * @return
	 */
	@Override
	public String slot(String attribute, String type, String template, Arg... args)
			throws IllegalArgumentException {
		return insiteCall(type, template, attribute, 1, args);
	}

	/**
	 * Return the URL to render this asset
	 */
	@Override
	public String getUrl(Arg... args) {
		return e.getUrl(getId(), args);
	}

	/**
	 * 
	 * Edit a field - extra parameters read from the config
	 * 
	 * @param attribute
	 * @param index
	 * @param args
	 * @return
	 */
	private String edit(String attribute, int index, Arg... args) {

		// read a call or create a new call with no parameters
		String value = e.getString(at(attribute), index, "value");
		Call call = Util.readAttributeConfig(attribute, cfg);
		if (call == null)
			call = new Insite();

		call.addArg("ASSETTYPE", e.getC());
		call.addArg("ASSETID", e.getCid().toString());
		call.addArg("FIELD", attribute);
		call.addArg("VALUE", value);
		call.addArg("INDEX", Integer.toString(index));

		// additional parameters
		for (Arg arg : args)
			call.addArg(arg.name, arg.value);

		log.trace("edit: %s", call.encode());

		return call.encode();
	}
}
