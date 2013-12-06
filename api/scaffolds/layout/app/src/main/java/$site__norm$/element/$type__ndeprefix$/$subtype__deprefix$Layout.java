package $site;format="normalize"$.element.$type;format="normalize"$;

import static wcs.core.Common.*;
import wcs.core.Log;
import wcs.core.Index;
import wcs.core.Asset;
import wcs.core.Model;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;
import wcs.java.Picker;

@Index("$site;format="normalize"$/elements.txt")
public class $subtype;format="deprefix"$Layout extends Element {
	
	final static Log log = Log.getLog($subtype;format="deprefix"$Layout.class); 

	public static AssetSetup setup() {		
		return new Template("$type$", "$subtype$Layout", 
			Template.LAYOUT, // change template type here
			"$subtype$", $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$Layout.class) //
			.cache("false", "false") // change caching here
			.description("Layout for type $type$ $subtype$");
	}

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		Picker html = Picker.load("/$site;format="normalize"$/simple.html" , "#content");	    
	    //html.replace("#detail", a.call("$site$_Detail"));
	    //html.replace("#footer", e.call("$site$_Footer"));
		return html
				/*dump(log)*/.html(a, e);
	}
}
