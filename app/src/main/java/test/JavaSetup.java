package test;

import wcs.java.Asset;
import wcs.java.Setup;
import wcs.java.Site;
import wcs.java.Template;

public class JavaSetup extends Setup {

	@Override
	public Site getSite() {
		return new Site("Test");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] { //
		new Template("", "JavaTests", "Java Tests", "test.template.JavaTests")
				.setCache("false", "false") };

	}
}
