package demo.element.page;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Picker;
import static wcs.java.util.IfNull.*;

public class ContentLayout extends Element {

	Log log = Log.getLog(ContentLayout.class);
	
	public static AssetSetup setup() {
		
		return new Template("Page", "DmContentLayout", 
			Template.LAYOUT, // change template type here
			demo.element.page.ContentLayout.class) //
			.cache("false", "false") // change caching here
			.description("Layout for type Page Content");
	}

	@Override
	public String apply(Env e) {
		
		log.trace("c/cid=%s/%s", e.getC(), e.getCid().toString());		
		Asset a = e.getAsset();	
		Picker html = Picker.load("/blueprint/template.html" , "#content");
		
		html.prefixAttrs("img", "src", "/cs/blueprint/");
		html.replace("#title", a.getString("Title"));
		html.replace("#subtitle", a.getString("Subtitle"));
		
		html.replace("#teaser-title1", ifn(a.getString("TeaserTitle", 1), ""));
		html.replace("#teaser-body1", ifn(a.getString("TeaserText", 1), ""));

		html.replace("#teaser-title2", ifn(a.getString("TeaserTitle", 2), ""));
		html.replace("#teaser-body2", ifn(a.getString("TeaserText", 2),""));
		return html.dump(log).html();
	}

}
