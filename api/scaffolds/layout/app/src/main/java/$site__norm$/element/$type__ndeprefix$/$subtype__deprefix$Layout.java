package $site;format="normalize"$.element.$type;format="ndeprefix"$;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.api.Env;
import wcs.api.Asset; 
import wcs.java.Picker;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;

@Index("$site;format="normalize"$/elements.txt")
public class $subtype;format="deprefix"$Layout extends Element {
	final static Log log = getLog($subtype;format="deprefix"$Layout.class); 
	public static AssetSetup setup() {		
		return new Template("$type$", "$subtype;format="deprefix"$Layout", 
			Template.LAYOUT, // change template type here
			"$subtype$", $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$Layout.class) //
			.cache("false", "false") // change caching here
			.description("Layout for type $type$ $subtype$");
	}

	@Override
	public String apply(Env e) {
		// log.trace("$subtype$Layout");
		Picker html = Picker.load("/$site;format="normalize"$/simple.html" , "#content");	    
		Asset a = e.getAsset();
	    //html.replace("#detail", a.call("ContentDetail"));
	    //html.replace("#footer", e.call("Footer"));
		return html.html(a, e, model(arg("Text",""), arg("Footer","")));
	}
}
