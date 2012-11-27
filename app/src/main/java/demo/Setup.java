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
				new Template("", "DjLayout", "Demo Jquery Mobile Layout", //
						"demo.template.DjLayout").setCache("false", "false"),
				new Template("Page", "DjBody", "Demo Jquery Mobile Page Body",
						"demo.template.page.DjBody").setCache("false", "false"),
				new CSElement("DjRedirect", "Demo JqueryMobile Redirect",
						"demo.template.DjRedirect") // ,
		// new SiteEntry("DjRedirect", "Demo JqueryMobile Redirect",
		// "DjRedirect", true)
		};
	}

}
