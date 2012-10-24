package wcs.java;

import static wcs.java.Util.id;

import com.fatwire.assetapi.data.AssetData;

public class SiteEntry extends Asset {

	public SiteEntry(Long id, String name, String description, String element,
			boolean wrapper) {
		super(id("SiteEntry", id), name, description);
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
	AssetData data() {
		// TODO Auto-generated method stub
		return null;
	}

}
