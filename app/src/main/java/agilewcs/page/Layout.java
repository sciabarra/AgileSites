package agilewcs.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Layout extends Element {

	@Override
	public String apply(Env e) {

		Asset a = e.getAsset("Page", 1351275793178l);
		
		Picker p = new Picker("/agilewcs/index.html");
		p.select("head")
				//
				.attr("link", "href", "/cs/agilewcs/css/default.css")
				//
				.unselect()
				//
				.replace("#sidebar", call("AwFooter", //
						arg("name", e.getString("name"))))
				.replace("#latest-post", a.call("AwSummary")) //
		;
		return p.html();
	}
}
