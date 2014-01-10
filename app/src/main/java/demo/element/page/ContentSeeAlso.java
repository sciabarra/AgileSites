package demo.element.page;
import wcs.api.Asset;
import wcs.api.Env;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.Template;

@Index("demo/elements.txt")
public class ContentSeeAlso extends Element {

	final static Log log = Log.getLog(ContentSeeAlso.class);

	public static AssetSetup setup() {

		return new Template("Page", "ContentSeeAlso", Template.INTERNAL,
				"Demo_Content", demo.element.page.ContentSeeAlso.class) //
				.cache("false", "false") // change caching here
                .cacheCriteria("d")
				.description("Template ContentSeeAlso for type Page subtype Content");
	}

	@Override
	public String apply(Env e) {
		if (log.debug())
			log.debug("Demo ContentSeeAlso");

		Picker html = Picker.load("/blueprint/template.html", "#seealso1");
		Asset a = e.getAsset();
		html.replace("#seealso-title1", a.getString("Title"));
		html.replace("#seealso-text1", a.getString("Summary"));
		html.attr("#seealso-link1", "href", a.getUrl());
		html.removeAttrs("*[id^=seealso]", "id");
		return html.html();
	}

    public String applyTouch(Env e) {
        Picker html = Picker.load("/blueprint/template_mobile.html", "#seealso1");
        Asset a = e.getAsset();
        html.replace("#seealso-title1", a.getString("Title"));
        html.replace("#seealso-text1", a.getString("Summary"));
        html.attr("#seealso-link1", "href", a.getUrl());
        html.removeAttrs("*[id^=seealso]", "id");
        return html.html();
    }
}
