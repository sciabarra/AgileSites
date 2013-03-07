package agilesites.typeless;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;

public class Wrapper extends Element {

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset(e.getC(), e.getCid());
		return a.call(a.getTemplate());
	}

}
