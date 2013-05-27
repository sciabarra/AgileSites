package $site;format="normalize"$.element;

import static wcs.core.Common.*;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;
import wcs.java.Picker;

public class $cselement$ extends Element {

	final static Log log = Log.getLog($cselement$.class);
	
	public static AssetSetup setup() {
		return new CSElement("$prefix$$cselement$", $site;format="normalize"$.element.$cselement$.class);
	}

	@Override
	public String apply(Env e) {
		log.debug("Testing $cselement$");
		
		return "<h1>$cselement$</h1>";
		
		// sample logic with the html picker
		//Picker html = Picker.load("/$site;format="normalize"$/index.html" , "#content");
		//html.replace("#title", e.getString("Title"));
		//return html/*.dump(log)*/.html();
	}

}
