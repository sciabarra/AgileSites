package demo.element.page;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Picker;

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
		log.debug("c/cid=%s/%s", e.getC(), e.getCid().toString());		
		Asset a = e.getAsset();	
		log.debug("asset="+a);
		Picker html = Picker.load("/demo/simple.html" , "#content");
		log.debug("picker ok");
		
		html.replace("#title", a.getName());
		html.replace("#subtitle", a.getDescription());
		return html.dump(log).html();
	}

}
