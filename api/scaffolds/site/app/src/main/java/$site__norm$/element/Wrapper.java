package $site;format="normalize"$.element;

import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.util.AddIndex;

@AddIndex("$site;format="normalize"$/elements.txt")
public class Wrapper extends Element {
	
	private final static Log log = Log.getLog(Wrapper.class);

	public static AssetSetup setup() {
		return new CSElement("$site$_Wrapper", $site;format="normalize"$.element.Wrapper.class,
				new SiteEntry("$site;format="normalize"$", true, "$site$/$site$_Wrapper"));
	}

	@Override
	public String apply(Env e) {
		log.trace("$site$ Wrapper");

		Picker html = Picker.load("/$site;format="normalize"$/simple.html");

		// change relative references to absolute
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/$site;format="normalize"$/");
		html.prefixAttrs("script", "src", "/cs/$site;format="normalize"$/");

		// handle errors
		if (e.isVar("error"))
			return html.replace("#content", e.call("$site$_Error",// 
							arg("error", e.getString("error"))))//
					/*.dump(log)*/.outerHtml();

		Asset a = e.getAsset();
		if (a == null)
			return html.replace("#content", e.call("$site$_Error",//
				    		arg("error", "Asset not found")))//
					/*.dump(log)*/.outerHtml();

		// render the asset using his default template
		html.replace("title", a.getName());
		html.attr("meta[name=description]", "content", a.getDescription());	
		html.replace("#content", a.call(a.getTemplate()));

		return html/*.dump(log)*/.outerHtml();
	}
}
