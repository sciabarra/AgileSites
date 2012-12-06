package wcs.java;

import java.util.List;

import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;

/**
 * CSElement definition class
 * 
 * @author msciab
 * 
 */
public class CSElement extends Asset {

	private String element;

	public CSElement(String name, String element) {
		super("CSElement", "", name);
		this.element = element;
	}

	public String getElement() {
		return element;
	}

	public List<String> getAttributes() {
		return Util.listString("name", "description", "elementname",
				"rootelement", "url", "resdetails1", "resdetails2");
	}

	private String template(String clazz) {
		return "<%@ taglib prefix=\"cs\" uri=\"futuretense_cs/ftcs1_0.tld\"\n"
				+ "%><%@ taglib prefix=\"asset\" uri=\"futuretense_cs/asset.tld\"\n"
				+ "%><%@ taglib prefix=\"ics\" uri=\"futuretense_cs/ics.tld\"\n"
				+ "%><%@ taglib prefix=\"render\" uri=\"futuretense_cs/render.tld\"\n"
				+ "%><%@ page import=\"wcs.core.WCS\"\n"
				+ "%><cs:ftcs><ics:if condition='<%=ics.GetVar(\"seid\")!=null%>'><ics:then><render:logdep\ncid='<%=ics.GetVar(\"seid\")%>' c=\"SiteEntry\"/></ics:then></ics:if>"
				+ "<ics:if condition='<%=ics.GetVar(\"eid\")!=null%>'><ics:then><render:logdep\ncid='<%=ics.GetVar(\"eid\")%>' c=\"CSElement\"/></ics:then></ics:if>"
				+ "<%\nString r = WCS.dispatch(ics, \"" + clazz
				+ "\");\nif(r!=null) ics.StreamText(r); %></cs:ftcs>";
	}

	void setData(MutableAssetData data) {

		// data.getAttributeData("createdby").setData("agilewcs");
		// data.getAttributeData("createddate").setData(new Date());

		String elementname = getSite() + "." + getName();

		// element name
		data.getAttributeData("elementname").setData(elementname);
		// addAttribute(data, "elementname", element);

		// root element
		data.getAttributeData("rootelement").setData(elementname);
		// addAttribute(data, "rootelement", element);

		data.getAttributeData("resdetails1").setData(
				"eid=" + data.getAssetId().getId());
		data.getAttributeData("resdetails2").setData("agilewcs=1");

		// blob
		byte[] bytes = template(element).getBytes();
		BlobObject blob = new BlobObjectImpl(element + ".jsp", "AgileWCS",
				bytes);

		data.getAttributeData("url").setData(blob);
		// data.getAttributeData("Mapping").setData(new ArrayList());
		// data.getAttributeData("Mapping").setData(new AttributeMan HashMap());
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
