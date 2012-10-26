package wcs.java;

import static wcs.java.Util.qid;
import com.fatwire.assetapi.data.AssetData;

public class SiteEntry extends Asset {

	public SiteEntry(Long id, String name, String description, String element,
			boolean wrapper) {
		super(qid("SiteEntry", id), name, description);
		this.element = element;
		this.wrapper = wrapper;
	}

	private String element;
	private boolean wrapper;

	public String getElement() {
		return element;
	}

	public boolean isWrapper() {
		return wrapper;
	}

	@Override
	void setData(AssetData data) {
		// root element
		// <attribute name="rootelement"><string value="fpIndex"/></attribute>
		data.getAttributeData("rootelement").setData("");
		// <attribute name="pagecriteria"><array><string
		// value="ft_ss"/></array></attribute>
		data.getAttributeData("pagecriteria").setData("");
		// <attribute name="cselement_id"><assetreference type="CSElement"
		// value="1336168702031"/></attribute>
		data.getAttributeData("cselement_id").setData("");
		// <attribute name="pagename"><string value="FatPhone"/></attribute>
		data.getAttributeData("pagename").setData("");
		// <attribute name="defaultarguments"><array>
		// <struct>
		// <field name="name"><string value="site"/></field>
		// <field name="value"><string value="FatPhone"/></field>
		// </struct>
		// </array>
		data.getAttributeData("defaultarguments").setData("");
		// <attribute name="sscacheinfo"><string value="false"/></attribute>
		data.getAttributeData("sscacheinfo").setData("");
		// <attribute name="pageletonly"><string value="F"/></attribute>
		data.getAttributeData("pageletonly").setData("");
		// <attribute name="template"><string
		// value="OpenMarket/SiteEntryTemplate"/></attribute>
		data.getAttributeData("template").setData("");
		// <attribute name="cscacheinfo"><string value="false"/></attribute>
		data.getAttributeData("cscacheinfo").setData("");
		// <attribute name="csstatus"><string value="live"/></attribute>
		data.getAttributeData("csstatus").setData("");
		// <attribute name="cs_wrapper"><string value="n"/></attribute>
		data.getAttributeData("cs_wrapper").setData("");
	}

}
