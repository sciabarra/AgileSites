package agilewcs.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Layout extends Element {

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		
		Picker p = new Picker("/agilewcs/index.html");
		
		p.select("head").attr("link", "href", "/cs/agilewcs/css/default.css")
				.up();

		p.replace("#latest-post", a.call("AwDetail"));
		
		p.replace("#recent-posts", a.call("AwSummary"));
		

		// .replace("#sidebar", call("AwFooter", //
		// arg("name", e.getString("name"))))
		// .replace("#latest-post", a.call("AwSummary")) //
		;
		return p.html();
	}
}
