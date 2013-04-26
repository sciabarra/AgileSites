package demo.element.page;

import static wcs.java.util.IfNull.ifn;
import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;

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

		// log.trace("c/cid=%s/%s", e.getC(), e.getCid().toString());

		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#content");

		html.prefixAttrs("img", "src", "/cs/blueprint/");
		html.replace("#title", a.getString("Title"));
		html.replace("#subtitle", a.getString("Subtitle"));
		html.replace("#summary", a.getString("Summary"));
		html.replace("#detail", a.getString("Detail"));

		html.replace("#teaser-title1", ifn(a.getString("TeaserTitle", 1), ""));
		html.replace("#teaser-body1", ifn(a.getString("TeaserText", 1), ""));

		html.replace("#teaser-title2", ifn(a.getString("TeaserTitle", 2), ""));
		html.replace("#teaser-body2", ifn(a.getString("TeaserText", 2), ""));

		html.remove("div.related");
		html.append("#related-container",
				a.getSlotList("Related", "Content/DmSummary"));

		String image = a.getBlobUrl("Image");
		if (image == null)
			html.remove("#image-main");
		else
			html.attr("#image-main", "src", image);

		html.replace("#seealso1", a.getSlot("SeeAlso", 1, "Content/DmSeeAlso"));
		html.replace("#seealso2", a.getSlot("SeeAlso", 2, "Content/DmSeeAlso"));
		html.replace("#seealso3", a.getSlot("SeeAlso", 3, "Content/DmSeeAlso"));

		html.replace("#tree", e.call("DmTree"));
		html.replace("#topmenu", e.call("DmTopmenu"));
		html.replace("#breadcrump", e.call("DmBreadcrump",//
				arg("c", a.getC()), arg("cid", a.getCid().toString())));

		return html.html();
	}
}
