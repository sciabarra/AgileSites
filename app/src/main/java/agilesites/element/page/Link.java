package agilesites.element.page;

import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Template;

public class Link extends Element {

	public static AssetSetup setup() {
		return new Template("Page", "AwLink", Template.LAYOUT, Link.class) //
				.cache("false", "false") //
				.description("Page Link (Java)");
	}

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
