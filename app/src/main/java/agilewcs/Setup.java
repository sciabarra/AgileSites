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

				/*
				 * new Template("", "AwLayout", Template.EXTERNAL,
				 * agilewcs.typeless.Layout.class)//
				 * .description("Header (Java)"),
				 */

				new Template("", "AwHeader", Template.INTERNAL,
						agilewcs.typeless.Header.class)//
						.description("Header (Java)"),

				new Template("", "AwLink", Template.INTERNAL,
						agilewcs.typeless.Link.class)//
						.description("Link (Java)"),

				new Template("Page", "AwLayout", Template.LAYOUT,
						agilewcs.page.Layout.class)//
						.cache("false", "false") //
						.description("Page Layout (Java)"), //

				new Template("Page", "AwLayout/Home", Template.LAYOUT,
						agilewcs.page.LayoutHome.class)//
						.cache("false", "false") //
						.description("Home Page Layout (Java)"), //

				new Template("Page", "AwLayout/Accordion", Template.LAYOUT,
						agilewcs.page.LayoutAccordion.class)//
						.cache("false", "false") //
						.description("Accordion Page Layout (Java)"), //

				new Template("Page", "AwSummary", Template.INTERNAL,
						agilewcs.page.Summary.class)//
						.cache("false", "false") //
						.description("Article Summary (Java)"), //

				new CSElement("AwFooter", agilewcs.typeless.Footer.class)
						.description("Footer (Java)"), //

				new Template("Agile_Article", "AwLayout", Template.LAYOUT,
						agilewcs.article.Layout.class)//
						.cache("false", "false") //
						.description("Article Layout (Java)"), //

				new Template("Agile_Article", "AwSummary", Template.INTERNAL,
						agilewcs.article.Summary.class)//
						.cache("false", "false") //
						.description("Article Summary (Java)"), //

				new Template("Agile_Article", "AwDetail", Template.INTERNAL,
						agilewcs.article.Detail.class)//
						.cache("false", "false") //
						.description("Article Detail (Java)"), //

				// test runner
				new CSElement("JUnitRunner", agilewcs.tests.JUnitRunner.class), //
				new SiteEntry("JUnitRunner", false) };
	}
}
