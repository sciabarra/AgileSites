package agilewcs.template;

import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import static wcs.java.util.Util.*;

public class AwLayout extends Element {

	@Override
	public String apply(Env env) {

		Picker p = new Picker("/index.html");
		p.select("head") //
				.attr("link", "href", "/cs/css/default.css")//
				.unselect() //
				.replace("#sidebar", call("DJFooter", //
						arg("name", env.getString("name"))));
		return p.html();
	}
}
