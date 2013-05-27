package wcs.java;

import static wcs.java.util.Util.attrStructKV;

import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.MutableAssetData;

public class SiteEntry extends AssetSetup {

	// private String element;
	private String elementname;

	private boolean wrapper;

	/**
	 * Create a site entry, specifying if it is a wrapper or not
	 * 
	 * @param name
	 * @param wrapper
	 */
	public SiteEntry(String name, boolean wrapper) {
		this(name, wrapper, (String)null);
	}

	/**
	 * Create a site entry, specifying if it is a wrapper or not
	 * 
	 * @param name
	 * @param wrapper
	 */
	public SiteEntry(String name, boolean wrapper, AssetSetup nextSetup) {
		this(name, wrapper, (String)null);
		setNextSetup(nextSetup);
	}

	/**
	 * Create a site entry, specifying if it is a wrapper and the element it
	 * actually calls (otherwise it is automatically calculated from the name)
	 * 
	 * @param name
	 * @param wrapper
	 * @param elementname
	 */
	public SiteEntry(String name, boolean wrapper, String elementname) {
		super("SiteEntry", "", name);
		this.elementname = elementname;
		this.wrapper = wrapper;
	}

	public String getElement() {
		return null;
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

		String elementname = //
		(this.elementname == null) //
		? (getSite() + "/" + getName()) //
				: this.elementname;

		// root element
		data.getAttributeData("category").setData("");
		data.getAttributeData("pagename").setData(getName());
		data.getAttributeData("rootelement").setData(elementname);
		data.getAttributeData("cs_wrapper").setData(wrapper ? "y" : "n");

		// data.getAttributeData("cselement_id").setData(
		// "CSElement:" + csElementId);

		data.getAttributeData("acl").setData("");
		data.getAttributeData("cscacheinfo").setData("false");
		data.getAttributeData("sscacheinfo").setData("false");
		data.getAttributeData("csstatus").setData("live");
		data.getAttributeData("pageletonly").setData("false");

		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context",/* "p",*/"rendermode",
						"site", /*"sitepfx",*/ "ft_ss"));

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
	public AssetSetup description(String description) {
		setDescription(description);
		return this;
	}
}
