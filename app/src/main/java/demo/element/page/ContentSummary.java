package demo.element.page;

import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;

public class ContentSummary extends Element {
	Log log = Log.getLog(ContentSummary.class);

	public static AssetSetup setup() {

		return new Template("Page", "Content/DmSummary", Template.INTERNAL, // change
				demo.element.page.ContentSummary.class) //
				.cache("false", "false") // change caching here
				.description("Template DmSummary for type Page Content");
	}

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#related");
		html.replace("#related-title", a.getString("Title"));
		html.replace("#related-body", a.getString("Summary"));
		html.removeAttrs("*[id^=related]", "id");
		return html.outerHtml();
	}

}
