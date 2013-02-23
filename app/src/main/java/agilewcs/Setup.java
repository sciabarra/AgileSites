package agilewcs;

import wcs.java.Asset;
import wcs.java.CSElement;
import wcs.java.Site;
import wcs.java.SiteEntry;
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

				new Template("", "AwLayout", Template.EXTERNAL,
						agilewcs.typeless.Layout.class)//
						.description("Typeless Layout (Java)"),

				new CSElement("AwHeader", agilewcs.typeless.Header.class)
						.description("Header (Java)"), //

				new Template("", "AwMenuTop", Template.INTERNAL,
						agilewcs.typeless.MenuTop.class)//
						.cache("false", "false") //
						.description("Top Menu (Java)"), //

				new CSElement("AwFooter", agilewcs.typeless.Footer.class)
						.description("Footer (Java)"), //

				new Template("", "AwMenuBottom", Template.INTERNAL,
						agilewcs.page.MenuBottom.class)//
						.cache("false", "false") //
						.description("Menu Bottom (Java)"), //


				new Template("Page", "AwLayout", Template.LAYOUT,
						agilewcs.page.Layout.class)//
						.cache("false", "false") //
						.description("Page Layout (Java)"), //

				new Template("", "AwMenuTop", Template.INTERNAL,
						agilewcs.typeless.MenuTop.class)//
						.cache("false", "false") //
						.description("Menu Top (Java)"), //

						
				// test runner
				new CSElement("JUnitRunner", agilewcs.tests.JUnitRunner.class), //
				new SiteEntry("JUnitRunner", false) };
	}
}
