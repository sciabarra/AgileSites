package wcs.java;

import static wcs.java.Util.attrArray;
import static wcs.java.Util.attrBlob;
import static wcs.java.Util.attrString;
import static wcs.java.Util.attrStruct;
import static wcs.java.Util.attrStructKV;
import static wcs.java.Util.id;

import java.util.HashMap;
import java.util.List;

import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;

public class Template extends Asset {

	// private final static Log log = new Log(Template.class);

	public Template(Long id, String name, String description, String element,
			String cscache, String sscache) {
		super(id("Template", id), "", name, description);
		this.element = element;
		this.cscache = cscache;
		this.sscache = sscache;
	}

	private String element;

	private String cscache;

	private String sscache;

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

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void setData(MutableAssetData data) {

		// log.info(Util.dump(data));

		final String body = "<b>" + element + "</b>";
		final AttributeData blob = attrBlob("url", element, body);

		HashMap mapElement = new HashMap<String, Object>();

		mapElement.put("elementname", attrString("elementname", element));
		mapElement.put("description", attrString("description", element));
		mapElement.put("resdetails1",
				attrString("resdetails1", "tid=" + getId().id));
		mapElement.put("resdetails2", attrString("resdetails2", "agilewcs=1"));
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
						attrStructKV("site", "Test"),
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

		data.getAttributeData("applicablesubtypes").setData("*");

	}
}
