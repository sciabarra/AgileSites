package agilesites.element.page;

import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Template;

public class Footer extends Element {

	public static AssetSetup setup() {
		return new Template("Page", "AwFooter", //
				Template.INTERNAL, Footer.class)//
				.cache("false", "false") //
				.description("Footer for Page (Java)"); //
	}

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Ciao, " + (name == null ? "World" : name);
	}

}
