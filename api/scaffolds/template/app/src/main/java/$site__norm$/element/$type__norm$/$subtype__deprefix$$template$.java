package $site;format="normalize"$.element.$type;format="normalize"$;

import static wcs.core.Common.*;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;

public class $subtype;format="deprefix"$$template$ extends Element {

	final static Log log = Log.getLog($subtype;format="deprefix"$$template$.class); 
			
	public static AssetSetup setup() {	
		return new Template("$type$", "$subtype$$template$", 
			Template.INTERNAL, // change template type here
			"$subtype$", $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$$template$.class) //
			.cache("false", "false") // change caching here
			.description("Template $subtype$$template$ for type $type$ $subtype$");
	}

	@Override
	public String apply(Env e) {
		return "<h1>$subtype$$template$</h1>";

		// TODO: implement here the logic
		// sample logic with the html picker
		//Picker html = Picker.load("/$site;format="normalize"$/index.html" , "#content");
		//html.replace("#title", e.getString("Title"));
		//return html/*.dump(log)*/.html();

	}

}
