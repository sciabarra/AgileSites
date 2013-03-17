package agilesites1.page;

import wcs.java.Element;
import wcs.java.Env;

public class Image extends Element {

	@Override
	public String apply(Env e) {
		//return "Hello!";
		return "[[" + e.getAsset().getBlobUrl("Image") + "]]";
	}

}
