package iui;

import wcs.java.Asset;
import wcs.java.Site;
import wcs.java.SiteEntry;
import wcs.java.CSElement;
import wcs.java.Template;

public class Setup extends wcs.java.Setup {

	long b = 2000000000000l;

	@Override
	public Site getSite() {
		return new Site("iUI");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] {
				new Template(b, "ILayout", "iUI Layout",
						"iui.template.ILayout", "false", "false"),
				new CSElement(b + 1, "IRedirect", "iUI Redirector",
						"iui.template.IRedirect"),
				new SiteEntry(b + 2, "iUI", "", "IRedirect", true, b + 1) };
	}

}
