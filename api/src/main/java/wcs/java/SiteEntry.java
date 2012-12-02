package wcs.java;

import static wcs.java.Util.attrStructKV;

import java.util.List;

import com.fatwire.assetapi.data.MutableAssetData;

public class SiteEntry extends Asset {

	private String element;
	private String elementname;

	private boolean wrapper;

	public SiteEntry(String name, boolean wrapper,
			String elementname, String element) {
		super("SiteEntry", "", name);
		this.element = element;
		this.elementname = elementname;
		this.wrapper = wrapper;
	}

	public String getElement() {
		return element;
	}

	public boolean isWrapper() {
		return wrapper;
	}

	public List<String> getAttributes() {
		return Util.listString("name", "description", "category", "pagename",
				"cs_wrapper", "cselement_id", "acl", "cscacheinfo",
				"sscacheinfo", "csstatus", "defaultarguments", "pagecriteria",
				"rootelement", "pageletonly");
	}

	@Override
	void setData(MutableAssetData data) {
		// root element
		data.getAttributeData("category").setData("");
		data.getAttributeData("pagename").setData(elementname);
		data.getAttributeData("cs_wrapper").setData(wrapper ? "y" : "n");

		// data.getAttributeData("cselement_id").setData(
		// "CSElement:" + csElementId);

		data.getAttributeData("acl").setData("");
		data.getAttributeData("cscacheinfo").setData("false");
		data.getAttributeData("sscacheinfo").setData("false");
		data.getAttributeData("csstatus").setData("live");
		data.getAttributeData("rootelement").setData(element);
		data.getAttributeData("pageletonly").setData("false");

		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", "p", "rendermode",
						"site", "sitepfx", "ft_ss"));

		data.getAttributeData("defaultarguments").setDataAsList(
				Util.list(
						attrStructKV("seid", "" + data.getAssetId().getId()), //
						attrStructKV("site", getSite()),
						attrStructKV("rendermode", "live")));

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
