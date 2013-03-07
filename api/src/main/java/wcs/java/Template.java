package wcs.java;

import static wcs.java.util.Util.attrArray;
import static wcs.java.util.Util.attrBlob;
import static wcs.java.util.Util.attrString;
import static wcs.java.util.Util.attrStruct;
import static wcs.java.util.Util.attrStructKV;

import java.util.HashMap;
import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public class Template extends Asset {

	public final static char UNSPECIFIED = '\0';
	public final static char INTERNAL = 'b';
	public final static char STREAMED = 'r';
	public final static char EXTERNAL = 'x';
	public final static char LAYOUT = 'l';

	// private final static Log log = new Log(Template.class);

	private String rootelement;
	private String fileelement;
	private String folderelement;

	private String clazz;

	private String cscache;

	private String sscache;

	private char ttype;

	/**
	 * Create a template with given subtype, name, and top element
	 * 
	 * Description defaults to the name, cache defaults to false/false
	 * 
	 * @param subtype
	 * @param name
	 * @param description
	 * @param element
	 */
	public Template(String subtype, String name, char ttype,
			Class<?> elementClass) {
		super("Template", subtype, //
				subtype == null || subtype.trim().length() == 0 //
				? name : subtype + "/" + name //
		);
		this.clazz = elementClass.getCanonicalName();
		if (subtype == null || subtype.trim().length() == 0) {
			subtype = "";
			rootelement = "/" + name;
			folderelement = "Typeless";
		} else {
			subtype = subtype.trim();
			rootelement = subtype + "/" + name;
			folderelement = subtype;
		}
		fileelement = name + "_" + clazz + ".jsp";

		this.ttype = ttype;
		cache("false", "false");
	}

	/**
	 * Fluent cache setter
	 * 
	 * @param cscache
	 * @param sscache
	 * @return
	 */

	public Template cache(String cscache, String sscache) {
		this.cscache = cscache;
		this.sscache = sscache;
		return this;
	}

	public String getElement() {
		return rootelement;
	}

	public String getCscache() {
		return cscache;
	}

	public String getSscache() {
		return sscache;
	}

	@Override
	List<String> getAttributes() {
		return Util.listString("name", "description", "category",
				"rootelement", "element", "ttype", "pagecriteria", "acl",
				"applicablesubtypes", "Thumbnail");
	}

	private String template(String clazz) {
		String template = new java.util.Scanner(getClass().getResourceAsStream(
				"/Streamer.jsp")).useDelimiter("\\A").next();
		return template.replaceAll("%CLASS%", clazz);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void setData(MutableAssetData data) {

		// log.info(Util.dump(data));

		// String rootelement = getSubtype() + "/" + getName();

		final String body = template(clazz);

		final AttributeData blob = attrBlob("url", folderelement, fileelement,
				body);

		HashMap mapElement = new HashMap<String, Object>();

		mapElement.put("elementname", attrString("elementname", rootelement));
		mapElement.put("description", attrString("description", rootelement));
		mapElement.put("resdetails1",
				attrString("resdetails1", "tid=" + data.getAssetId().getId()));
		mapElement.put(
				"resdetails2",
				attrString("resdetails2",
						"agilesites=" + System.currentTimeMillis()));
		mapElement.put("csstatus", attrString("csstatus", "live"));
		mapElement.put("cscacheinfo", attrString("cscacheinfo", "false"));
		mapElement.put("sscacheinfo", attrString("sscacheinfo", "false"));
		mapElement.put("url", blob);

		HashMap mapSiteEntry = new HashMap<String, Object>();
		mapSiteEntry.put("pagename", attrString("pagename", rootelement));
		mapSiteEntry.put(
				"defaultarguments", //
				attrArray(
						"defaultarguments", //
						attrStructKV("site", getSite()),
						attrStructKV("rendermode", "live")));
		mapElement.put(
				"siteentry",
				attrArray("siteentry",
						attrStruct("Structure siteentry", mapSiteEntry)));

		data.getAttributeData("category").setData("banr");

		data.getAttributeData("rootelement").setData(rootelement);

		data.getAttributeData("element").setData(
				Util.list(attrStruct("Structure Element", mapElement)));

		data.getAttributeData("ttype").setData(
				ttype == UNSPECIFIED ? null : "" + ttype);

		// default page criteria
		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", /* "p", */"rendermode",
						"site", /* "sitepfx", */"ft_ss"));

		data.getAttributeData("acl").setDataAsList(Util.listString(""));

		if (data.getAttributeData("applicablesubtypes") != null)
			data.getAttributeData("applicablesubtypes").setData("*");

	}

	/**
	 * Fluent description setter
	 * 
	 * @param description
	 * @return
	 */
	public Asset description(String description) {
		setDescription(description);
		return this;
	}
}
