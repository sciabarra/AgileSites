package wcs.java;

import com.fatwire.assetapi.data.AssetData;
import static wcs.java.Util.id;

public class Template extends Asset {

	public Template(Long id, String name, String description, String element,
			String cscache, String sscache) {
		super(id("Template", id), name, description);
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
	AssetData data() {
		// TODO Auto-generated method stub
		return null;
	}

}
