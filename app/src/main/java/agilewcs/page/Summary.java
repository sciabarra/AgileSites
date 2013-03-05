package agilewcs.page;

import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Summary extends Element {

	@Override
	public String apply(Env env) {

		// pick the recent post colums
		Picker p = new Picker("/agilewcs/index.html", "#recent-posts");
		
		// select a single instance
		p.single(".post");
		
		// return the html of the selected block (excluding the wrapper)
		return p.html();
		
		// return "I am the summary";

	}

}
