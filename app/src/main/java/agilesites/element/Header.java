package agilesites.element;

import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;

public class Header extends Element {

	public static AssetSetup setup() {
		// typeless
		return new Template("", "AwHeader", Template.INTERNAL, Header.class)
				.description("Header (Java)"); //

	}

	@Override
	public String apply(Env e) {

		Picker p = Picker.load("/agilesites/index.html");
		p.select("head") //
				.attr("link", "href", "/agilesites/cs/css/default.css")//
				.up();

		p.replace("#footer", e.call("AwFooter", //
				arg("name", e.getString("name"))));

		return p.html();
	}
}
