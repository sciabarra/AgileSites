package agilesites1.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Detail extends Element {

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		Picker p = new Picker("/agilesites/index.html", "#latest-post");
		p.replace("h1.title", a.getString("Title"));
		p.select("p.meta small").empty() //
				.append("by " + a.getString("Author")).up();
		p.select("div.entry").empty()//
				.append(a.call("AwImage")).append(a.getString("Body")).up();
		return p.html();
	}
}
