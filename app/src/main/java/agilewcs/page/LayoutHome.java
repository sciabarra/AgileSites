package agilewcs.page;

import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class LayoutHome extends Element {

	@Override
	public String apply(Env env) {

		Picker p = new Picker("/index.html");
		p.select("head") //
				.attr("link", "href", "/cs/css/default.css")//
				.unselect() //
				.replace("#sidebar", call("AwFooter", //
						arg("name", env.getString("name"))));
		return p.html();
	}
}
