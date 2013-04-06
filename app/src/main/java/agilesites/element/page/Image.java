package agilesites.element.page;

import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Template;

public class Image extends Element {
	public static AssetSetup setup() {
		return new Template("Page", "AwImage", Template.INTERNAL, Image.class) //
				.cache("false", "false") //
				.description("Page Image (Java)");

	}

	@Override
	public String apply(Env e) {
		// return "Hello!";
		return "[[" + e.getAsset().getBlobUrl("Image") + "]]";
	}

}
