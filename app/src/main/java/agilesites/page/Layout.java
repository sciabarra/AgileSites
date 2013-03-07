package agilesites.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Layout extends Element {

	// private static Log log = new Log(Layout.class);

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();

		// select the resource
		Picker p = new Picker("/agilesites/index.html");

		// change the css as appropriate
		p.select("head").attr("link", "href", "/cs/agilesites/css/default.css")
				.up();

		// replace the detail
		p.replace("#latest-post", a.call("AwDetail"));

		// replace multiple posts
		p.empty("#recent-posts");
		p.append("#recent-posts",
				e.getAsset("Page", a.getId("Related1")).call("AwSummary"));
		p.append("#recent-posts",
				e.getAsset("Page", a.getId("Related2")).call("AwSummary"));
		p.append("#recent-posts",
				e.getAsset("Page", a.getId("Related3")).call("AwSummary"));

		p.append("#recent-posts", a.call("AwSummary"));

		// return the html including the selected node
		return p.outerHtml();
	}
}
