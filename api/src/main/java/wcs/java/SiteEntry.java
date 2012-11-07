package wcs.java;

import static wcs.java.Util.attrArray;
import static wcs.java.Util.attrStruct;
import static wcs.java.Util.attrStructKV;
import static wcs.java.Util.id;

import java.util.HashMap;
import java.util.List;

import wcs.java.Util;

import com.fatwire.assetapi.data.MutableAssetData;

public class SiteEntry extends Asset {

	public SiteEntry(Long id, String name, String description, String element,
			boolean wrapper, long csElementId) {
		super(id("SiteEntry", id), "", name, description);
		this.id = id;
		this.element = element;
		this.wrapper = wrapper;
		this.csElementId = csElementId;
	}

	private long id;
	private String element;
	private boolean wrapper;
	private long csElementId;

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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void setData(MutableAssetData data) {
		// root element
		data.getAttributeData("category").setData("");
		data.getAttributeData("pagename").setData(element);
		data.getAttributeData("cs_wrapper").setData(wrapper ? "y" : "n");
		data.getAttributeData("cselement_id").setData(
				"CSElement:" + csElementId);
		data.getAttributeData("acl").setData("");
		data.getAttributeData("cscacheinfo").setData("false");
		data.getAttributeData("sscacheinfo").setData("false");
		data.getAttributeData("csstatus").setData("live");
		data.getAttributeData("rootelement").setData(element);
		data.getAttributeData("pageletonly").setData("false");

		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", "p", "rendermode",
						"site", "sitepfx", "ft_ss"));

		/*
		 * HashMap mapSiteEntry = new HashMap<String, Object>();
		 * mapSiteEntry.put( "defaultarguments", // attrArray(
		 * "defaultarguments", // attrStructKV("seid", "" + id), //
		 * attrStructKV("site", "Test"), attrStructKV("rendermode", "live")));
		 * 
		 * data.getAttributeData("defaultarguments") .setData(
		 * Util.list(attrStruct("Structure defaultarguments", mapSiteEntry)));
		 */

		data.getAttributeData("defaultarguments").setDataAsList(
				Util.list(
						attrStructKV("seid", "" + id), //
						attrStructKV("site", "Test"),
						attrStructKV("rendermode", "live")));

	}
}
