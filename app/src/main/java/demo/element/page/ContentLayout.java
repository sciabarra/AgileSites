package demo.element.page;

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

		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#content");

		html.prefixAttrs("img", "src", "/cs/blueprint/");

		html.replace("#title", a.editString("Title"));
		html.replace("#subtitle", a.editString("Subtitle"));
		html.replace("#summary", a.editString("Summary"));
		html.replace("#detail", a.editText("Detail", ""));

		html.replace("#teaser-title1", a.editString("TeaserTitle", 1, "{noValueIndicator: \"Enter Teaser Title\"}"));
		html.replace("#teaser-body1", a.editString("TeaserText", 1, "{noValueIndicator: \"Enter Teaser Text\"}"));
		html.replace("#teaser-title2", a.editString("TeaserTitle", 2, "{noValueIndicator: \"Enter Teaser\"}"));
		html.replace("#teaser-body2", a.editString("TeaserText", 2, "{noValueIndicator: \"Enter Teaser Text\"}"));

		html.remove("div.related");
		html.append("#related-container",
				a.slotList("Related", "Page", "DmSummary"));
		html.append("#related-container", a.slotEmpty("Related", "Page",
				"DmSummary", "Drag a Page here. Save to add more."));

		String image = a.getBlobUrl("Image");
		if (image == null)
			html.remove("#image-main");
		else
			html.attr("#image-main", "src", image);

		html.replace("#seealso1", a.slot("SeeAlso", 1, "Page",
				"DmContentSeeAlso", "Drag a Page Here"));
		html.replace("#seealso2", a.slot("SeeAlso", 2, "Page",
				"DmContentSeeAlso", "Drag a Page Here"));
		html.replace("#seealso3", a.slot("SeeAlso", 3, "Page",
				"DmContentSeeAlso", "Drag a Page Here"));

		html.replace("#tree", e.call("DmTree"));
		html.replace("#topmenu", e.call("DmTopmenu"));
		html.replace("#breadcrump", e.call("DmBreadcrump",//
				arg("c", a.getC()), arg("cid", a.getCid().toString())));

		return html.html();
	}
}
