package wcs.java;

import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.BlobObjectImpl;

import static wcs.java.Util.qid;

public class CSElement extends Asset {

	public CSElement(Long id, String name, String description, String element) {
		super(qid("CSElement", id), name, description);
		this.element = element;
	}

	private String element;

	public String getElement() {
		return element;
	}

	void setData(AssetData data) {
		// element name
		data.getAttributeData("elementname").setData(element);
		// root element
		data.getAttributeData("rootelement").setData(element);
		// blob
		byte[] bytes = ("<b>" + element + "</b>").getBytes();
		data.getAttributeData("url").setData(
				new BlobObjectImpl(element, "AgileWCS", bytes));
	}
}
