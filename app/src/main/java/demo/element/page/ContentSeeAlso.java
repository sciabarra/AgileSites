package demo.element.page;

import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.Template;
import wcs.java.AssetSetup;
import wcs.core.Log;

public class ContentSeeAlso extends Element {

	Log log = Log.getLog(demo.element.page.ContentSeeAlso.class);

	public static AssetSetup setup() {

		return new Template("Page", "Content/DmSeeAlso", Template.INTERNAL, // change
				demo.element.page.ContentSeeAlso.class) //
				.cache("false", "false") // change caching here
				.description("Template DmSeeAlso for type Page Content");
	}

	@Override
	public String apply(Env e) {
		log.trace(">>>>>>ContentSeeAlso: %s/%d", e.getC(), e.getCid());

		Picker html = Picker.load("/blueprint/template.html", "#seealso1");
		if(e.getCid()==0)
			return html.html();
		
		Asset a = e.getAsset();
		html.replace("#seealso-title1", a.getString("Title"));
		html.replace("#seealso-text1", a.getString("Summary"));

		return html.html();

	}

}
