package demo;

import wcs.java.Asset;
import wcs.java.CSElement;
import wcs.java.Site;
import wcs.java.Template;

public class Setup extends wcs.java.Setup {

	@Override
	public Site getSite() {
		return new Site("Demo");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] {
				new Template("DjLayout", "demo.template.DjLayout")//
						.cache("false", "false") //
						.description("Demo Jquery Mobile Layout"), //
				new Template("Page", "DjBody", "demo.template.page.DjBody")//
						.cache("false", "false")//
						.description("Demo Jquery Mobile Page Body"),
				new CSElement("DjRedirect", "demo.template.DjRedirect")
						.description("Demo JqueryMobile Redirect") // ,
		// new SiteEntry("DjRedirect", "Demo JqueryMobile Redirect",
		// "DjRedirect", true)
		};
	}
}
