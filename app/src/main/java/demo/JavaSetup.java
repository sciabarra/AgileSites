package demo;

import wcs.java.Asset;
import wcs.java.CSElement;
import wcs.java.Site;
import wcs.java.Template;

public class JavaSetup extends wcs.java.Setup {

	@Override
	public Site getSite() {
		return new Site("Demo");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] {
				new Template("DJLayout", demo.template.DJLayout.class)//
						.cache("false", "false") //
						.description("Demo Layout (Java)"), //
				new Template("DJHeader", demo.template.DJHeader.class)//
						.cache("false", "false")//
						.description("Demo Header Java"),
				new CSElement("DJFooter", demo.cselement.DJFooter.class), //
				new CSElement("DJWrapper", demo.cselement.DJWrapper.class) //
		};
	}
}
