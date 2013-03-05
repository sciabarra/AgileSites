package agilewcs.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Layout extends Element {

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();

		// select the resource
		Picker p = new Picker("/agilewcs/index.html");

		// change the css as appropriate
		p.select("head").attr("link", "href", "/cs/agilewcs/css/default.css")
				.up();

		// replace the detail
		p.replace("#latest-post", a.call("AwDetail"));

		// replace multiple posts
		p.empty("#recent-posts");
		p.append("#recent-posts", a.call("AwSummary"));
		p.append("#recent-posts", a.call("AwSummary"));

		// return the html including the selected node
		return p.outerHtml();
	}
}
