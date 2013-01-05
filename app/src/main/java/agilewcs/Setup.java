package agilewcs;

import wcs.java.Asset;
import wcs.java.CSElement;
import wcs.java.Site;
import wcs.java.Template;

public class Setup extends wcs.java.Setup {

	@Override
	public Site getSite() {
		return new Site("AgileWCS");
	}

	@Override
	public Asset[] getAssets() {
		return new Asset[] {
				// layout & cselements
				new Template("AwLayout", agilewcs.template.AwLayout.class)//
						.cache("false", "false") //
						.description("AgileWCS Layout (Java)"), //
				new Template("AwHeader", agilewcs.template.AwHeader.class)//
						.cache("false", "false")//
						.description("AgileWCS Header (Java)"),
				new CSElement("AwFooter", agilewcs.cselement.AwFooter.class), //
				
				// test runner
				new Template("JUnitRunner", agilewcs.tests.JUnitRunner.class), //
		};
	}
}
