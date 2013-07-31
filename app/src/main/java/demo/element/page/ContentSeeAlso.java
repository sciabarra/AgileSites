package demo.element.page;

import org.springframework.stereotype.Component;
import wcs.java.Asset;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.Template;
import wcs.java.AssetSetup;
import wcs.core.Log;

import javax.annotation.Resource;

@Component
public class ContentSeeAlso extends Element {

	final static Log log = Log.getLog(ContentSeeAlso.class);

    @Resource
    Env env;

	public static AssetSetup setup() {

		return new Template("Page", "DmContentSeeAlso", Template.INTERNAL,
				demo.element.page.ContentSeeAlso.class) //
				.cache("false", "false") // change caching here
				.description("Template DmContentSeeAlso for type Page Content");
	}

	@Override
	public String apply() {
		Picker html = Picker.load("/blueprint/template.html", "#seealso1");
		Asset a = env.getAsset();
		html.replace("#seealso-title1", a.getString("Title"));
		html.replace("#seealso-text1", a.getString("Summary"));
		html.attr("#seealso-link1", "href", a.getUrl());
		html.removeAttrs("*[id^=seealso]", "id");
		return html.html();
	}

}
