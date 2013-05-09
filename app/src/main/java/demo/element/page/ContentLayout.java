package demo.element.page;

import static wcs.java.util.IfNull.ifn;
import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;
import wcs.java.util.Util;

public class ContentLayout extends Element {

	Log log = Log.getLog(ContentLayout.class);

	public static AssetSetup setup() {
		return new Template("Page", "DmContentLayout", Template.LAYOUT,
				demo.element.page.ContentLayout.class) //
				.cache("false", "false") // change caching here
				.description("Layout for type Page Content");
	}

	@Override
	public String apply(Env e) {

		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#content");
		html.replace("#version", Util.getResource("/version.txt"));
		
		html.prefixAttrs("img", "src", "/cs/blueprint/");
		html.replace("#title", a.editString("Title"));
		html.replace("#subtitle", a.editString("Subtitle"));
		html.replace("#summary", a.editString("Summary"));
		html.replace("#detail", a.editString("Detail"));

		html.replace("#teaser-title1", ifn(a.getString("TeaserTitle", 1), ""));
		html.replace("#teaser-body1", ifn(a.getString("TeaserText", 1), ""));
		html.replace("#teaser-title2", ifn(a.getString("TeaserTitle", 2), ""));
		html.replace("#teaser-body2", ifn(a.getString("TeaserText", 2), ""));

		html.remove("div.related");
		for(int i: a.getRange("Related")) {
			Asset r = e.getAsset("Page", a.getCid("Related", i)); 
			html.append("#related-container", r.call("DmSummary"));
		}

		String image = a.getBlobUrl("Image");
		if (image == null)
			html.remove("#image-main");
		else
			html.attr("#image-main", "src", image);

		html.replace("#seealso1", a.slot("SeeAlso", 1, "Page", "DmContentSeeAlso"));
		html.replace("#seealso2", a.slot("SeeAlso", 2, "Page", "DmContentSeeAlso"));
		html.replace("#seealso3", a.slot("SeeAlso", 3, "Page", "DmContentSeeAlso"));

		html.replace("#tree", e.call("DmTree"));
		html.replace("#topmenu", e.call("DmTopmenu"));
		html.replace("#breadcrump", e.call("DmBreadcrump",//
				arg("c", a.getC()), arg("cid", a.getCid().toString())));

		return html.html();
	}
}
