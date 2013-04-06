package agilesites.element;

import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;

public class Footer extends Element {

	public static AssetSetup setup() {
		return new CSElement("AwFooter", Footer.class)
				.description("Footer (Java)"); //
	}

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
