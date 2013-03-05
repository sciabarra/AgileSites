package agilewcs.typeless;

import static wcs.core.Common.arg;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Picker;

public class Header extends Element {

	@Override
	public String apply(Env env) {

		Picker p = new Picker("/agilewcs/index.html");
		p.select("head") //
				.attr("link", "href", "/agilewcs/cs/css/default.css")//
				.up();

		p.replace("#footer", call("AwFooter", //
				arg("name", env.getString("name"))));

		return p.html();
	}
}
