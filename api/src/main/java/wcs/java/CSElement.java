package wcs.java;

import static wcs.java.Util.id;

import java.util.List;

import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;

public class CSElement extends Asset {

	public CSElement(Long id, String name, String description, String element) {
		super(id("CSElement", id), "", name, description);
		this.element = element;
	}

	private String element;

	public String getElement() {
		return element;
	}

	public List<String> getAttributes() {
		return Util.listString("name", "description", "elementname",
				"rootelement", "url", "resdetails1", "resdetails2");
	}

	void setData(MutableAssetData data) {

		//data.getAttributeData("createdby").setData("agilewcs");
		//data.getAttributeData("createddate").setData(new Date());

		// element name
		data.getAttributeData("elementname").setData(element);
		// addAttribute(data, "elementname", element);

		// root element
		data.getAttributeData("rootelement").setData(element);
		// addAttribute(data, "rootelement", element);

		data.getAttributeData("resdetails1").setData(
				"eid=" + data.getAssetId().getId());
		data.getAttributeData("resdetails2").setData("agilewcs=1");


		// blob
		byte[] bytes = ("<i>" + element + "</i>").getBytes();
		BlobObject blob = new BlobObjectImpl(element, "", bytes);

		data.getAttributeData("url").setData(blob);

		// data.getAttributeData("Mapping").setData(new ArrayList());

		// data.getAttributeData("Mapping").setData(new AttributeMan HashMap());

		/*
		 * 
		 * List<AttributeData> mappingArray = new ArrayList<AttributeData>();
		 * AttributeData adata = new AttributeDataImpl(null, null,
		 * AttributeTypeEnum.STRUCT, mappingArray); HashMap map = new HashMap();
		 * AttributeData ad = new AttributeDataImpl(); map.put("key", new
		 * AttributeData());
		 * 
		 * for (int i=0; i<mappingArray.size(); i++) { } %> { HashMap mappingMap
		 * = (HashMap)mappingArray.get(i).getData(); String key =
		 * (String)((AttributeData)mappingMap.get("key")).getData(); String type
		 * = (String)((AttributeData)mappingMap.get("type")).getData(); String
		 * value = (String)((AttributeData)mappingMap.get("value")).getData();
		 * String siteid =
		 * (String)((AttributeData)mappingMap.get("siteid")).getData();
		 * out.println("Mapping Entry #"+String.valueOf(i+1));
		 * out.println("<br/>"); out.println("Key: "+key);
		 * out.println("Type: "+type); out.println("Value: "+value);
		 * out.println("Siteid: "+siteid); out.println("<br/>"); }
		 */
		// addAttribute(data, "url", blob);

	}
}
