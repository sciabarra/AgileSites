package agilesites.element;

import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;

public class Wrapper extends Element {

	public AssetSetup setup() {
		return new CSElement("AwWrapper", Wrapper.class);
	}

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset(e.getC(), e.getCid());
		return a.call(a.getTemplate());
	}

}
