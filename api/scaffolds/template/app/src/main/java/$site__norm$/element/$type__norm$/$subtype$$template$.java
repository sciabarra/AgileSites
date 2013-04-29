package $site;format="normalize"$.element.$type;format="normalize"$;

import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;
import wcs.core.Log;

public class $subtype$$template$ extends Element {

	final static Log log = Log.getLog($template$.class); 
			
	public static AssetSetup setup() {
		
		return new Template("$type$", "$subtype$/$prefix$$template$", 
			Template.INTERNAL, // change template type here
			$site;format="normalize"$.element.$type;format="normalize"$.$subtype$$template$.class) //
			.cache("false", "false") // change caching here
			.description("Template $prefix$$template$ for type $type$ $subtype$");
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
