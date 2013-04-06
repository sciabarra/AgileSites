package agilesites.element.page;

import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;

public class Summary extends Element {

	public static AssetSetup setup() {
		return new Template("Page", "AwSummary",//
				Template.INTERNAL, Summary.class) //
				.cache("false", "false") //
				.description("Page Summary (Java)");

	}

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		Picker p = Picker.load("/agilesites/index.html",
				"#recent-posts");
		// select a single instance
		p.single(".post");
		p.replace("h2.title", a.getString("Title"));
		// return the html of the selected block
		return p.html();
	}
}
