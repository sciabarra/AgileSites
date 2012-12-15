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

	// private final static Log log = new Log(Template.class);

	private String element;

	private String cscache;

	private String sscache;

	private String myname;

	/**
	 * Create a typeless template with a given top element
	 * 
	 */
	public Template(String name, Class<?> elementClass) {
		this("", name, elementClass);
	}

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
	public Template(String subtype, String name, Class<?> elementClass) {
		super("Template", subtype, name);
		this.element = elementClass.getCanonicalName();

		if (subtype != null && subtype.trim().length() == 0)
			myname = name;
		else
			myname = subtype + "/" + name;

		cache("false", "flase");
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

	@Override
	public String getName() {
		return myname;
	}

	public String getElement() {
		return element;
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
		return "<%@ taglib prefix=\"cs\" uri=\"futuretense_cs/ftcs1_0.tld\"\n"
				+ "%><%@ taglib prefix=\"asset\" uri=\"futuretense_cs/asset.tld\"\n"
				+ "%><%@ taglib prefix=\"ics\" uri=\"futuretense_cs/ics.tld\"\n"
				+ "%><%@ taglib prefix=\"render\" uri=\"futuretense_cs/render.tld\"\n"
				+ "%><%@ page import=\"wcs.core.WCS\"\n"
				+ "%><cs:ftcs><ics:if condition='<%=ics.GetVar(\"tid\")!=null%>'><ics:then><render:logdep\ncid='<%=ics.GetVar(\"tid\")%>' c=\"Template\"/></ics:then></ics:if>"
				+ "<%\nString r = WCS.dispatch(ics, \"" + clazz
				+ "\");\nif(r!=null) ics.StreamText(r); %></cs:ftcs>";
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void setData(MutableAssetData data) {

		// log.info(Util.dump(data));

		// String rootelement = getSubtype() + "/" + getName();

		final String folder = getSubtype().equals("") ? "Typeless"
				: getSubtype();
		final String file = element + ".jsp";
		final String body = template(element);

		final AttributeData blob = attrBlob("url", folder, file, body);

		HashMap mapElement = new HashMap<String, Object>();

		mapElement.put("elementname", attrString("elementname", element));
		mapElement.put("description", attrString("description", element));
		mapElement.put("resdetails1",
				attrString("resdetails1", "tid=" + data.getAssetId().getId()));
		mapElement.put(
				"resdetails2",
				attrString("resdetails2",
						"agilewcs=" + System.currentTimeMillis()));
		mapElement.put("csstatus", attrString("csstatus", "live"));
		mapElement.put("cscacheinfo", attrString("cscacheinfo", "false"));
		mapElement.put("sscacheinfo", attrString("sscacheinfo", "false"));
		mapElement.put("url", blob);

		HashMap mapSiteEntry = new HashMap<String, Object>();
		mapSiteEntry.put("pagename", attrString("pagename", element));
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

		data.getAttributeData("rootelement").setData(element);

		data.getAttributeData("element").setData(
				Util.list(attrStruct("Structure Element", mapElement)));

		data.getAttributeData("ttype").setData("x");

		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", "p", "rendermode",
						"site", "sitepfx", "ft_ss"));

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
