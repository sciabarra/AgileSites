package $site;format="normalize"$.element;

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
		return new CSElement("$prefix$Wrapper", mysite.element.Wrapper.class);
	}

	@Override
	public String apply(Env e) {
		log.trace("$site$ Wrapper");

		Picker html = Picker.load("/$site;format="normalize"$/index.html");

		// change relative references to absolute
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/");
		html.prefixAttrs("script", "src", "/cs/");

		// handle errors
		if (e.isVar("error"))
			return html.replace("#content", //
					e.call("$prefix$Error", arg("error", e.getString("error"))))//
					/*.dump(log)*/.outerHtml();

		Asset a = e.getAsset();
		if (a == null)
			return html
					.replace("#content",
							e.call("$prefix$Error", arg("error", "Asset not found")))//
					/*.dump(log)*/.outerHtml();

		// render a page using the template and the asset
		html.replace("#content", a.call("$prefix$Detail"));
		html.replace("title", a.getName());
		html.attr("meta[name=description]", "content", a.getDescription());

		return html/*.dump(log)*/.outerHtml();
	}
}
