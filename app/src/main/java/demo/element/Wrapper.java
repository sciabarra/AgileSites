package demo.element;

import static wcs.Api.*;
import wcs.api.Asset;
import wcs.api.Env;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.SiteEntry;

@Index("demo/elements.txt")
public class Wrapper extends Element {

	private final static Log log = Log.getLog(Wrapper.class);

	public static AssetSetup setup() {
		return new CSElement("Wrapper", demo.element.Wrapper.class,
				new SiteEntry("", true, "Demo/Demo_Wrapper"));
	}

	@Override
	public String apply(Env e) {
		if (log.debug())
			log.trace("Demo Wrapper");

		Picker html = Picker.load("/blueprint/template.html");

		// change relative references to absolute
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/blueprint/");
		html.prefixAttrs("script[id=js-import]", "src", "/cs/blueprint/");
		html.replace("#js-base", "var base='/cs/blueprint/'");

		// handle errors
		if (e.isVar("error"))
			return html.replace("#content",
					e.call("Error", arg("error", e.getString("error"))))
					.outerHtml();

		Asset a = e.getAsset();
		if (a == null)
			return html.replace("#content",
					e.call("Error", arg("error", "Asset not found")))
					.outerHtml();

		// render the asset using his default template
		html.replace("title", a.getName());
		html.attr("meta[name=description]", "content", a.getDescription());
		html.replace("#content", a.call(a.getTemplate()));

		return html.outerHtml();
	}
}
