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
public class Summary extends Element {

	final static Log log = Log.getLog(Summary.class);

	public static AssetSetup setup() {
		return new Template("Page", "Summary", Template.INTERNAL, // change
				"Demo_Content", demo.element.page.Summary.class) //
				.cache("false", "false") // change caching here
                .cacheCriteria("d")
				.description("Template Summary for type Page ");
	}

	@Override
	public String apply(Env e) {
		if (log.debug())
			log.debug("Demo ContentSeeAlso");

		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#related");
		html.replace("#related-title", a.getString("Title"));
		html.replace("#related-body", a.getString("Summary"));
		html.removeAttrs("*[id^=related]", "id");
		return html.outerHtml();
	}

    public String applyTouch(Env e) {
        if (log.debug())
            log.debug("Demo ContentSeeAlso");

        Asset a = e.getAsset();
        Picker html = Picker.load("/blueprint/template_mobile.html", "#related");
        html.replace("#related-title", a.getString("Title"));
        html.replace("#related-body", a.getString("Summary"));
        html.removeAttrs("*[id^=related]", "id");
        return html.outerHtml();
    }

}
