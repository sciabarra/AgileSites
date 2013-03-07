package agilesites.page;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Summary extends Element {

	@Override
	public String apply(Env e) {

		Asset a = e.getAsset();
		Picker p = new Picker("/agilesites/index.html", "#recent-posts");

		// select a single instance
		p.single(".post");
		p.replace("h2.title", a.getString("Title"));

		// return the html of the selected block (excluding the wrapper)
		return p.html();

	}
}
