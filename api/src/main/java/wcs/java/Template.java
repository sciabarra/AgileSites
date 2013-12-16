package wcs.java;

import static wcs.java.util.Util.attrArray;
import static wcs.java.util.Util.attrBlob;
import static wcs.java.util.Util.attrString;
import static wcs.java.util.Util.attrStruct;
import static wcs.java.util.Util.attrStructKV;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public class Template extends AssetSetup {

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
	private String forSubtype;
	private List<String> cacheCriteria = Util.listString("c", "cid", "context",
			"p", "rendermode", "site", /* "sitepfx", */"ft_ss");

	public Template(String subtype, String name, char ttype,
			Class<?> elementClass) {
		this(subtype, name, ttype, null, elementClass);
	}

	/**
	 * Create a template with given subtype, name, and top element, applying to
	 * given subtypes.
	 * 
	 * Description defaults to the name, cache defaults to false/false
	 * 
	 * @param subtype
	 * @param name
	 * @param description
	 * @param element
	 */
	public Template(String subtype, String name, char ttype, String forSubtype,
			Class<?> elementClass) {

		super("Template", subtype, name);
		this.clazz = elementClass.getCanonicalName();
		this.subtype = subtype;
		this.name = name;
		this.ttype = ttype;
		if (forSubtype == null || forSubtype.trim().length() == 0)
			this.forSubtype = "*";
		else
			this.forSubtype = forSubtype;
		cache("false", "false");
	}

	@Override
	public void init(String site) {
		super.init(site);
		if (subtype == null || subtype.trim().length() == 0) {
			subtype = "";
			rootelement = "/" + getName();
			folderelement = "Typeless";
		} else {
			subtype = subtype.trim();
			rootelement = subtype + "/" + getName();
			folderelement = subtype;
		}
		fileelement = getName() + "_" + clazz + ".jsp";
	}

	/**
	 * Setup a template with a chained setup
	 * 
	 * @param subtype
	 * @param name
	 * @param ttype
	 * @param elementClass
	 * @param nextSetup
	 */
	public Template(String subtype, String name, char ttype,
			Class<?> elementClass, AssetSetup nextSetup) {
		this(subtype, name, ttype, elementClass);
		setNextSetup(nextSetup);
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

	public Template cacheCriteria(String criteria) {
		this.cacheCriteria.add(criteria);
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
		return Util.getResource("/Streamer.jsp").replaceAll("%CLASS%", clazz);
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
		mapElement.put("cscacheinfo", attrString("cscacheinfo", cscache));
		mapElement.put("sscacheinfo", attrString("sscacheinfo", sscache));
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

		// default page criteria
		Collections.sort(cacheCriteria);
		data.getAttributeData("pagecriteria").setDataAsList(cacheCriteria);

		data.getAttributeData("acl").setDataAsList(Util.listString(""));

		data.getAttributeData("ttype").setData(
				ttype == UNSPECIFIED ? null : "" + ttype);

		data.getAttributeData("applicablesubtypes").setData(forSubtype);
	}

	/**
	 * Fluent description setter
	 * 
	 * @param description
	 * @return
	 */
	public AssetSetup description(String description) {
		setDescription(description);
		return this;
	}
}
