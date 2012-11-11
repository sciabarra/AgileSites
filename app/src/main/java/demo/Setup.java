package demo;

import wcs.java.Asset;
import wcs.java.CSElement;
import wcs.java.Site;
import wcs.java.SiteEntry;
import wcs.java.Template;

public class Setup extends wcs.java.Setup {

	long b = 2000000000000l;

	@Override
	public Site getSite() {
		return new Site("Demo");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] {
				new Template(b, "Layout", "Layout", "demo.cselement.Menu",
						"false", "false"),
				new CSElement(b + 1, "Home", "Home", "demo.template.Home"),
				new SiteEntry(b + 2, "Demo", "Demo", "demo.template.Home",
						true, b + 1) };
	}

}
