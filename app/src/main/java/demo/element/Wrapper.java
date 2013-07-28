package demo.element;

import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.util.AddIndex;
import wcs.core.Log;

@AddIndex("demo/elements.txt")
public class Wrapper extends Element {

	private final static Log log = Log.getLog(Wrapper.class);

	public static AssetSetup setup() {
		return new CSElement(137502363006l, "DmWrapper",
				demo.element.Wrapper.class, new SiteEntry(137502363007l,
						"demo", true, "Demo/DmWrapper"));
	}

	@Override
	public String apply(Env e) {
		log.trace("Demo Wrapper");

		Picker html = Picker.load("/blueprint/template.html");

		// change relative references to absolute
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/blueprint/");
		html.prefixAttrs("script[id=js-import]", "src", "/cs/blueprint/");
		html.replace("#js-base", "var base='/cs/blueprint/'");

		// handle errors
		if (e.isVar("error"))
			return html.replace("#content", e.call("DmError",//
					arg("error", e.getString("error"))))//
					/* .dump(log) */.outerHtml();

		Asset a = e.getAsset();
		if (a == null)
			return html.replace("#content", e.call("DmError",//
					arg("error", "Asset not found")))//
					/* .dump(log) */.outerHtml();

		// render the asset using his default template
		html.replace("title", a.getName());
		html.attr("meta[name=description]", "content", a.getDescription());
		html.replace("#content", a.call(a.getTemplate()));

		return html/* .dump(log) */.outerHtml();
	}
}
