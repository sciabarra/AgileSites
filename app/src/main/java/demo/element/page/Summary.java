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
public class Summary extends Element {

	final static Log log = Log.getLog(Summary.class);

    @Resource
    Env env;

	public static AssetSetup setup() {

		return new Template("Page", "DmSummary", Template.INTERNAL, // change
				demo.element.page.Summary.class) //
				.cache("false", "false") // change caching here
				.description("Template DmSummary for type Page ");
	}

	@Override
	public String apply() {
		Asset a = env.getAsset();
		Picker html = Picker.load("/blueprint/template.html", "#related");
		html.replace("#related-title", a.getString("Title"));
		html.replace("#related-body", a.getString("Summary"));
		html.removeAttrs("*[id^=related]", "id");
		return html.outerHtml();
	}

}
