package wcs.java;

import static wcs.Api.arg;
import static wcs.Api.ifn;
import static wcs.Api.nn;
import static wcs.Api.tmp;
import static wcs.Api.toDate;
import static wcs.Api.toInt;
import static wcs.Api.toLong;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import wcs.Api;
import wcs.api.Arg;
import wcs.api.AssetDeps;
import wcs.api.Call;
import wcs.api.Log;
import wcs.core.WCS;
import wcs.core.tag.AssetTag;
import wcs.core.tag.AssetsetTag;
import wcs.core.tag.BlobserviceTag;
import wcs.core.tag.RenderTag;
import wcs.java.util.Util;

import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

public class Asset extends AssetBase implements wcs.api.Asset, wcs.api.Content {

	private String prefix = "";

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

	boolean insite = false;

	public Asset(Env env, String c, Long cid) {
		this.e = env;
		this.i = e.ics;
		this.c = c;
		this.cid = cid;
		insite = env.isInsite();
		String site = i.GetVar("site");
		prefix = site + "_";
		init(site);
		AssetTag.load().name(a).type(c).objectid(cid.toString()).run(i);
		String subtype = AssetTag.getsubtype().name(a).eval(i, "OUTPUT");
		this.subtype = subtype == null ? "" : subtype;
	}

	/**
	 * Return the assetset name, lazily loading all the attributes on the first
	 * request
	 * 
	 * @return
	 */
	private String as() {
		if (as == null) {
			as = tmp() + "_";
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
		if (log.trace())
			log.trace("extracting attribute " + attribute);

		if (!attribute.startsWith(prefix))
			attribute = prefix + attribute;

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
	 * Return a named field from the asset as a string.
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public String getFieldString(String name) {
		return AssetTag.get().name(a).field(name).eval(i, "output");
	}

	/**
	 * Return a named field from the asset as a date.
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public Date getFieldDate(String name) {
		return toDate(AssetTag.get().name(a).field(name).eval(i, "output"));
	}

	/**
	 * Return a named field from the asset as an int.
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public int getFieldInt(String name) {
		return toInt(AssetTag.get().name(a).field(name).eval(i, "output"));
	}

	/**
	 * Return a named field from the asset as a long.
	 * 
	 * @param name
	 * @return
	 */
	@Override
	public long getFieldLong(String name) {
		return toLong(AssetTag.get().name(a).field(name).eval(i, "output"));
	}

	/**
	 * Return the name field of the asset
	 */
	@Override
	public String getName() {
		if (name == null)
			name = getFieldString("name");
		return name;
	}

	/**
	 * Return the description field of the asset
	 */
	@Override
	public String getDescription() {
		if (description == null)
			description = getFieldString("description");
		return description;
	}

	/**
	 * Return the description field of the asset
	 */
	@Override
	public String getTemplate() {
		if (template == null)
			template = getFieldString("template");
		return template;
	}

	/**
	 * Return the file field of the asset
	 */
	@Override
	public String getFilename() {
		if (filename == null)
			filename = getFieldString("filename");
		return filename;
	}

	/**
	 * Return the path field of the asset
	 */
	@Override
	public String getPath() {
		if (path == null)
			path = getFieldString("path");
		return path;
	}

	/**
	 * Return the start date field of the asset
	 */
	@Override
	public Date getStartDate() {
		if (startDate == null)
			startDate = getFieldDate("startdate");
		return startDate;
	}

	/**
	 * Return the end date field of the asset
	 */
	@Override
	public Date getEndDate() {
		if (endDate == null)
			endDate = getFieldDate("enddate");
		return endDate;
	}

	/**
	 * Check if the attribute exist
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isAttribute(String attribute) {
		return e.isList(at(attribute)) && e.getSize(at(attribute)) > 0;
	}

	/**
	 * Check if the attribute at the given position exists
	 * 
	 * @param attribute
	 * @return
	 */
	public boolean isAttribute(String attribute, int n) {
		return e.isList(at(attribute)) && e.getSize(at(attribute)) >= n;
	}

	/**
	 * Return if we are in insite editing
	 */
	public boolean isInsite() {
		return insite;
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
	 * @param attribute
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
	 * @param attribute
	 * @return
	 */
	@Override
	public Long getCid(String attribute, int n) {
		return e.getLong(at(attribute), n, "value");
	}

	/**
	 * Return the specified asset. It does not log any dependencies - use this
	 * when you just need to get an url.
	 * 
	 */
	public wcs.api.Asset getAsset(String attribute, String type) {
		return e.getAsset(type, getCid(attribute));
	}

	/**
	 * Return the specified asset at the nth position. It does not log any
	 * dependencies - use this when you just need to get an url.
	 */
	public wcs.api.Asset getAsset(String attribute, String type, int i) {
		return e.getAsset(type, getCid(attribute, i));
	}

	/**
	 * Return the related asset pointed by the attribute of the given type if
	 * not found
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public wcs.api.Asset getAsset(String attribute, String type, AssetDeps deps) {
		Long cid = getCid(attribute);
		e.addDependency(type, cid, deps);
		return e.getAsset(type, cid);
	}

	/**
	 * Return the related asset pointed by the nth attribute of the given type
	 * if not found
	 * 
	 * Since you are accessing another asset it is mandatory to specify the
	 * dependency type you are going to use.
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public wcs.api.Asset getAsset(String attribute, int i, String type,
			AssetDeps deps) {
		long cid = getCid(attribute, i);
		e.addDependency(type, cid);
		return e.getAsset(type, cid);
	}

	/**
	 * Return the first attribute of the the attribute rib as a string, or the
	 * null if not found
	 * 
	 * Since you are accessing another asset it is mandatory to specify the
	 * dependency type you are going to use.
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public String getString(String attribute) {
		return e.getString(at(attribute), "value");
	}

	/**
	 * Return the nth attribute of the the attribute as a string, or the void
	 * string if not found
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public String getString(String attribute, int n) {
		return e.getString(at(attribute), n, "value");
	}

	/**
	 * Edit (or return the value if not insite) the first named attribute as a
	 * string, or null if not found and pass additional parameters
	 * 
	 */
	@Override
	public String editString(String attribute, int n, String params,
			Arg... args) {
		if (insite)
			return edit(attribute, n, params, args);
		return getString(attribute, n);
	}

	/**
	 * Edit (or return the value if not insite) the attribute as a string, or
	 * 
	 */
	public String editString(String attribute) {
		return editString(attribute, 1);
	}

	/**
	 * Edit (or return the value if not insite) the nth attribute as a string,
	 * or
	 * 
	 */
	public String editString(String attribute, int n) {
		return editString(attribute, n, "");
	}

	/**
	 * Edit (or return if not insite) the first named attribute as a string, or
	 * null if not found and pass additional parameters
	 * 
	 * @param attribute
	 * @param args
	 * @return
	 */
	public String editString(String attribute, String params, Arg... args) {
		return editString(attribute, 1, params, args);
	}

	/**
	 * Edit (or return if not insite) the nth named attribute as a string, or
	 * null if not found and pass additional parameters using the CK editor
	 * 
	 * @param attribute
	 * @return
	 */
	public String editText(String attribute, int n, String params) {
		return editString(attribute, n, nn(params), arg("editor", "ckeditor"));
	}

	/**
	 * Edit (or return if not insite) the named attribute as a string, or null
	 * if not found and pass additional parameters using the CK editor
	 * 
	 * @param attribute
	 * @return
	 */
	public String editText(String attribute, String params) {
		return editString(attribute, 1, nn(params), arg("editor", "ckeditor"));
	}

	/**
	 * Return the first attribute of the the attribute list as an int, or null
	 * if not found
	 * 
	 * @param attribute
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
	 * @param attribute
	 * @return
	 */
	@Override
	public Integer getInt(String attribute, int n) {
		return e.getInt(at(attribute), n, "value");
	}

	/**
	 * Return the first attribute of the the attribute list as a long, or null
	 * if not found
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public Long getLong(String attribute) {
		throw new RuntimeException("this is not a bound asset");
	}

	/**
	 * Return the nth attribute of the the attribute list as an int, or null if
	 * not found
	 * 
	 * @param attribute
	 * @return
	 */
	@Override
	public Long getLong(String attribute, int n) {
		throw new RuntimeException("this is not a bound asset");
	}

	/**
	 * Return the first attribute of the the attribute list as a date, or null
	 * if not found
	 * 
	 * @param attribute
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
	 * @param attribute
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
		return getBlobUrl(attribute, 1, null, args);
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

		wcs.api.Config bcfg = e.getConfig();

		// read the filename
		String filename = getBlobFilename(attribute, pos);

		// invoke tag
		RenderTag.Getbloburl tag = RenderTag.getbloburl()
				.blobtable(bcfg.getBlobTable(e.ics()))
				.blobcol(bcfg.getBlobUrl(e.ics()))
				.blobkey(WCS.normalizeSiteName(bcfg.getSite()))
				.blobwhere(blobWhere.toString());
		// if the mymetype is not passed it is read from the mimetypes table
		// using the filename extension
		if (mimeType == null || mimeType.trim().length() == 0)
			mimeType = getBlobMimetype(filename);

		tag.blobheader(mimeType);
		tag.set(new Arg("blobheadername1", "Content-Disposition"), new Arg(
				"blobheadervalue1", "attachment; filename=" + filename));

		// pass parameters
		for (Arg arg : args) {
			tag.set(arg.name.toUpperCase(), arg.value);
		}
		// run the tag
		String blobUrl = tag.eval(i, "outstr");
		return blobUrl + "/" + filename;
	}

	private String getBlobFilename(String attribute, int pos) {
		Long blobWhere = this.getCid(attribute, pos);
		if (blobWhere == null)
			return null;
		// invoke tag
		BlobserviceTag.readdata().id(blobWhere.toString())
				.listvarname("outlist").run(i);
		IList blobData = i.GetList("outlist");
		String filename;
		try {
			filename = blobData.getValue("urldata");
		} catch (NoSuchFieldException e1) {
			return null;
		}
		return normalizeFilename(filename);
	}

	private String normalizeFilename(String filepath) {
		filepath = filepath.replace("\\", "/");
		String filename = filepath;
		if (filepath.lastIndexOf("/") > 0) {
			filename = filepath.substring(filepath.lastIndexOf("/") + 1,
					filepath.length());
		}
		int version = filename.lastIndexOf(",");
		int extensionPos = filename.lastIndexOf(".", filename.length());
		if (version > 0) {
			String extension = "";
			if (extensionPos > 0)
				extension = filename.substring(extensionPos, filename.length());
			return filename.substring(0, version) + extension;
		}
		return filename;
	}

	private String getBlobMimetype(String filename) {
		if (filename == null || filename.length() < 3)
			return null;
		String mimeType = null;
		String ext = filename.substring(filename.lastIndexOf('.') + 1);
		i.SetVar("extension", ext);
		wcs.core.tag.IcsTag.selectto().table("mimetype").what("mimetype")
				.listname("types").where("extension").run(i);
		IList mimetypes = i.GetList("types");
		if (mimetypes != null && mimetypes.hasData()) {
			try {
				mimeType = mimetypes.getValue("mimetype");
			} catch (NoSuchFieldException e1) {
				return null;
			}
		}
		return mimeType;
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
	private String insiteCall(String type, String template, String attribute,
			int n, String emptyText, Arg... args) {

		if (!attribute.startsWith(prefix))
			attribute = prefix + attribute;

		if (!template.startsWith(prefix))
			template = prefix + template;

		try {
			// let's start with the common parameters
			List<Arg> list = new ArrayList<Arg>();
			String site = i.GetVar("site");
			list.add(arg("SITE", site));
			list.add(arg("TNAME", template));

			list.add(arg("TTYPE", //
					i.GetVar("tid") != null ? "Template" : "CSElement"));
			list.add(arg("TID", //
					i.GetVar("tid") != null ? i.GetVar("tid") : i.GetVar("eid")));

			list.add(arg("ASSETTYPE", c));
			list.add(arg("ASSETID", cid.toString()));
			list.add(arg("FIELD", attribute));

			if (emptyText != null)
				list.add(arg("EMPTYTEXT", emptyText));

			// copy additional args
			for (Arg arg : args) {
				// System.out.println(arg.toString());
				list.add(arg);
			}

			list.add(arg("CHILDTYPE", type));
			if (n < 0) {
				list.add(arg("LISTNAME", at(attribute)));
				return Api.call("INSITE:CALLTEMPLATELOOP", list);
			} else {
				Long icid = (Long) ifn(getCid(attribute, n), 0l);
				list.add(arg("CHILDID", icid.toString()));
				list.add(arg("INDEX", Integer.toString(n)));
				return Api.call("INSITE:CALLTEMPLATE", list);
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
	 * @param template
	 * @param args
	 * @return
	 */
	@Override
	public String call(String template, Arg... args) {
		// let's start with the common parameters
		String tid = i.GetVar("tid") != null ? i.GetVar("tid") : i
				.GetVar("eid");
		String ttype = i.GetVar("tid") != null ? "Template" : "CSElement";
		log.trace("ttype/tid=", ttype, tid);
		List<Arg> list = new ArrayList<Arg>();
		list.add(arg("SITE", i.GetVar("site")));
		list.add(arg("TNAME", Util.normalizedName(site, template)));
		list.add(arg("C", c));
		list.add(arg("CID", cid.toString()));
		list.add(arg("TTYPE", ttype));
		list.add(arg("TID", tid));
		return Api.call("RENDER:CALLTEMPLATE", list);
	}

	/**
	 * Render a list of slots pointed by the asset field using the the specified
	 * template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param attribute
	 * @param template
	 * @param type
	 * @param template
	 * @param args
	 * @return
	 */
	@Override
	public String slotList(String attribute, String type, String template,
			Arg... args) throws IllegalArgumentException {
		return insiteCall(type, template, attribute, -1, null, args);
	}

	/**
	 * Render an empty slot to drag additional content to a list.
	 * 
	 * @param attribute
	 * @param template
	 * @param type
	 * @param template
	 * @param emptyText
	 * @return
	 */
	@Override
	public String slotEmpty(String attribute, String type, String template,
			String emptyText) throws IllegalArgumentException {
		return insite ? insiteCall(type, template, attribute,
				getSize(attribute) + 1, emptyText) : "";
	}

	/**
	 * Render a single slot pointed by the i-th asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param attribute
	 * @param template
	 * @param type
	 * @param i
	 * @param args
	 * @return
	 */
	@Override
	public String slot(String attribute, int i, String type, String template,
			String emptyText, Arg... args) throws IllegalArgumentException {
		return insiteCall(type, template, attribute, i, emptyText, args);
	}

	/**
	 * Render a single slot pointed by the first asset field using the the
	 * specified template.
	 * 
	 * Slot type is configured in Config. You need a field of the same name of
	 * the field specifying the type as parameter "c"
	 * 
	 * @param attribute
	 * @param template
	 * @param args
	 * @return
	 */
	@Override
	public String slot(String attribute, String type, String template,
			String emptyText, Arg... args) throws IllegalArgumentException {
		return insiteCall(type, template, attribute, 1, emptyText, args);
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
	 * Edit a field with parameters
	 * 
	 * @param attribute
	 * @param index
	 * @param args
	 * @return
	 */
	private String edit(String attribute, int index, String params, Arg... args) {

		if (!attribute.startsWith(prefix))
			attribute = prefix + attribute;

		// read a call or create a new call with no parameters
		String value = e.getString(at(attribute), index, "value");

		Call call = new Call("INSITE:EDIT");

		call.addArg("ASSETTYPE", e.getC());
		call.addArg("ASSETID", e.getCid().toString());
		call.addArg("FIELD", attribute);
		call.addArg("VALUE", value);
		call.addArg("INDEX", Integer.toString(index));

		if (params != null)
			call.addArg("PARAMS", params);

		for (Arg arg : args)
			call.addArg(arg.name.toUpperCase(), arg.value);

		log.trace("edit: %s", call.encode());

		return call.encode();
	}

	@Override
	public boolean exists(String attribute) {
		return isAttribute(attribute);
	}

	@Override
	public boolean exists(String attribute, int pos) {
		return isAttribute(attribute, pos);
	}

	/**
	 * Dump the core fields of the asset
	 */
	@Override
	public String dump() {
		return super.dump();
	}

	/**
	 * Dump the attribute with the given name
	 */
	@Override
	public String dump(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=");
		for (int i : getRange(name))
			sb.append(getString(name, i)).append(",");
		sb.setCharAt(sb.length() - 1, '\n');
		return sb.toString();
	}
}