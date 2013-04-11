package demo.element;

import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Wrapper extends Element {
	
	private final static Log log = Log.getLog(Wrapper.class);

	public static AssetSetup setup() {
		return new CSElement("DmWrapper", demo.element.Wrapper.class);
	}

	@Override
	public String apply(Env e) {
		log.trace("Demo Wrapper");

		Picker html = Picker.load("/demo/page.html");

		// change relative references to absolute
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/demo/");
		html.prefixAttrs("script", "src", "/cs/demo/");

		// handle errors
		if (e.isVar("error"))
			return html.replace("#content", //
					e.call("DmError", arg("error", e.getString("error"))))//
					/*.dump(log)*/.outerHtml();

		Asset a = e.getAsset();
		if (a == null)
			return html
					.replace("#content",
							e.call("DmError", arg("error", "Asset not found")))//
					/*.dump(log)*/.outerHtml();

		// render a page using the template and the asset
		html.replace("#content", a.call("DmDetail"));
		html.replace("title", a.getName());
		html.attr("meta[name=description]", "content", a.getDescription());

		return html/*.dump(log)*/.outerHtml();
	}
}
