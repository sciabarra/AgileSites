package demo.element.page;
import static wcs.core.Common.*;
import static wcs.core.Log.*;
import wcs.core.Log;
import wcs.core.Index;
import wcs.core.Asset;
import wcs.java.Picker;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;

@Index("demo/elements.txt")
public class Summary extends Element {

	final static Log log = Log.getLog(Summary.class);

	public static AssetSetup setup() {

		return new Template("Page", "DmSummary", Template.INTERNAL, // change
				demo.element.page.Summary.class) //
				.cache("false", "false") // change caching here
				.description("Template DmSummary for type Page ");
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
