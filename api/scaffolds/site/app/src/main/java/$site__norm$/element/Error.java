package $site;format="normalize"$.element;

import wcs.core.Log;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Error extends Element {

	final static Log log = Log.getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement("$prefix$Error", $site;format="normalize"$.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		log.trace("$site$ Error");
		
		Picker html = Picker.load("/$site;format="normalize"$/simple.html", "#content");
		html.replace("#title", "Error");
		html.replace("#subtitle", e.getString("error"));
		return html/*.dump(log)*/.html();
	}
}
