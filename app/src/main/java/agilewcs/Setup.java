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
				new Template("Page", "AwHomeLayout", Template.LAYOUT,
						agilewcs.page.HomeLayout.class)//
						.cache("false", "false") //
						.description("Page Layout (Java)"), //

				new Template("Page", "AwAccordioLayout", Template.LAYOUT,
						agilewcs.page.AccordionLayout.class)//
						.cache("false", "false") //
						.description("Page Layout (Java)"), //

				new Template("", "AwHeader", Template.INTERNAL,
						agilewcs.typeless.Header.class)//
						.description("Header (Java)"),

				new CSElement("AwFooter", agilewcs.typeless.Footer.class)
						.description("Footer (Java)"), //

				new Template("", "AwLink", Template.INTERNAL,
						agilewcs.typeless.Link.class)//
						.description("Link (Java)"),

				new Template("Agile_Article", "AwLayout", Template.LAYOUT,
						agilewcs.article.Layout.class)//
						.cache("false", "false") //
						.description("Article Layout (Java)"), //

				new Template("Agile_Article", "AwDetail", Template.LAYOUT,
						agilewcs.article.Detail.class)//
						.cache("false", "false") //
						.description("Article Detail (Java)"), //

				new Template("Agile_Article", "AwSummary", Template.LAYOUT,
						agilewcs.article.Summary.class)//
						.cache("false", "false") //
						.description("Article Summary (Java)"), //

				// test runner
				new CSElement("JUnitRunner", agilewcs.tests.JUnitRunner.class), //
				new SiteEntry("JUnitRunner", true) };
	}
}
