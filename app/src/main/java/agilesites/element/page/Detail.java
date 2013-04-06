package agilesites.element.page;

import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.Template;

public class Detail extends Element {

	public static AssetSetup setup() {
		return new Template("Page", "AwDetail",//
				Template.INTERNAL, Detail.class)//
				.cache("false", "false") //
				.description("Page Detail (Java)"); //
	}

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		Picker p = Picker
				.load("/agilesites/index.html", "#latest-post");
		p.replace("h1.title", a.getString("Title"));
		p.select("p.meta small").empty() //
				.append("by " + a.getString("Author")).up();
		p.select("div.entry").empty()//
				.append(a.call("AwImage")).append(a.getString("Body")).up();
		return p.html();
	}
}
