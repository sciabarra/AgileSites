package wcs.java;

import com.fatwire.assetapi.data.AssetData;
import static wcs.java.Util.id;

public class CSElement extends Asset {

	public CSElement(Long id, String name, String description, String element) {
		super(id("CSElement", id), name, description);
		this.element = element;
	}

	private String element;

	public String getElement() {
		return element;
	}

	AssetData data() {
		return null;
	}

}
