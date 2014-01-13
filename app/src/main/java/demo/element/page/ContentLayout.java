package demo.element.page;
import static wcs.Api.*;
import wcs.api.Asset;
import wcs.api.Env;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.Template;

@Index("demo/elements.txt")
public class ContentLayout extends Element {

	Log log = Log.getLog(ContentLayout.class);

	public static AssetSetup setup() {
		return new Template("Page", "ContentLayout", Template.LAYOUT,
				"Demo_Content", demo.element.page.ContentLayout.class) //
				.cache("false", "false") // change caching here
                .cacheCriteria("d")
				.description("Layout for type Page Content");
	}

	@Override
	public String apply(Env e) {
		if (log.debug())
			log.debug("Demo ContentLayout");

		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#content");

		html.prefixAttrs("img", "src", "/cs/blueprint/");

		html.replace("#title", a.editString("Title"));
		html.replace("#subtitle", a.editString("Subtitle"));
		html.replace("#summary", a.editString("Summary"));
		html.replace("#detail", a.editText("Detail", ""));

		html.replace("#teaser-title1", a.editString("TeaserTitle", 1,
				"{noValueIndicator: \"Enter Teaser Title\"}"));
		html.replace("#teaser-body1", a.editString("TeaserText", 1,
				"{noValueIndicator: \"Enter Teaser Text\"}"));
		html.replace("#teaser-title2", a.editString("TeaserTitle", 2,
				"{noValueIndicator: \"Enter Teaser\"}"));
		html.replace("#teaser-body2", a.editString("TeaserText", 2,
				"{noValueIndicator: \"Enter Teaser Text\"}"));

		html.remove("div.related");
		html.append("#related-container",
				a.slotList("Related", "Page", "Summary"));
		html.append("#related-container", a.slotEmpty("Related", "Page",
				"Summary", "Drag a Page here. Save to add more."));

		String image = a.getBlobUrl("Image");
		if (image == null)
			html.remove("#image-main");
		else
			html.attr("#image-main", "src", image);

		html.replace("#seealso1", a.slot("SeeAlso", 1, "Page",
				"ContentSeeAlso", "Drag a Page Here"));
		html.replace("#seealso2", a.slot("SeeAlso", 2, "Page",
				"ContentSeeAlso", "Drag a Page Here"));
		html.replace("#seealso3", a.slot("SeeAlso", 3, "Page",
				"ContentSeeAlso", "Drag a Page Here"));

		html.replace("#topmenu", e.call("Topmenu"));
		html.replace(
				"#breadcrump",
				e.call("Breadcrump", arg("c", a.getC()),
						arg("cid", a.getCid().toString())));
		html.replace("#tree", e.call("Tree"));

		return html.dump(log).html();
	}


    public String applyTouch(Env e) {
        if (log.debug())
            log.debug("Demo ContentLayout");

        Asset a = e.getAsset();
        Picker html = Picker.load("/blueprint/template_mobile.html", "#content");

        html.prefixAttrs("img", "src", "/cs/blueprint/");

        html.replace("#title", a.editString("Title"));
        html.replace("#subtitle", a.editString("Subtitle"));
        html.replace("#summary", a.editString("Summary"));
        html.replace("#detail", a.editText("Detail", ""));

        html.replace("#teaser-title1", a.editString("TeaserTitle", 1,
                "{noValueIndicator: \"Enter Teaser Title\"}"));
        html.replace("#teaser-body1", a.editString("TeaserText", 1,
                "{noValueIndicator: \"Enter Teaser Text\"}"));
        html.replace("#teaser-title2", a.editString("TeaserTitle", 2,
                "{noValueIndicator: \"Enter Teaser\"}"));
        html.replace("#teaser-body2", a.editString("TeaserText", 2,
                "{noValueIndicator: \"Enter Teaser Text\"}"));
        html.replace("#teaser-title3", a.editString("TeaserTitle", 3,
                "{noValueIndicator: \"Enter Teaser\"}"));
        html.replace("#teaser-body3", a.editString("TeaserText", 3,
                "{noValueIndicator: \"Enter Teaser Text\"}"));

        html.remove("div.related");
        html.append("#related-container",
                a.slotList("Related", "Page", "Summary"));
        html.append("#related-container", a.slotEmpty("Related", "Page",
                "Summary", "Drag a Page here. Save to add more."));

        String image = a.getBlobUrl("Image");
        if (image == null)
            html.remove("#image-main");
        else
            html.attr("#image-main", "src", image);

        html.replace("#seealso1", a.slot("SeeAlso", 1, "Page",
                "ContentSeeAlso", "Drag a Page Here"));
        html.replace("#seealso2", a.slot("SeeAlso", 2, "Page",
                "ContentSeeAlso", "Drag a Page Here"));
        html.replace("#seealso3", a.slot("SeeAlso", 3, "Page",
                "ContentSeeAlso", "Drag a Page Here"));

        html.replace("#topmenu", e.call("Topmenu"));
        html.replace(
                "#breadcrump",
                e.call("Breadcrump", arg("c", a.getC()),
                        arg("cid", a.getCid().toString())));
        html.replace("#tree", e.call("Tree"));
        return html.dump(log).html();
    }
}
