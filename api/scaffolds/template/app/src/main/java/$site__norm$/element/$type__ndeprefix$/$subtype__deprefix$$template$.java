package $site;format="normalize"$.element.$type;format="ndeprefix"$;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.api.Asset;
import wcs.api.Env;
import wcs.java.Picker;
import wcs.java.Element;
import wcs.java.Template;
import wcs.java.AssetSetup;

@Index("$site;format="normalize"$/elements.txt")
public class $subtype;format="deprefix"$$template$ extends Element {
	final static Log log = getLog($subtype;format="deprefix"$$template$.class);
	public static AssetSetup setup() {	
		return new Template("$type$", "$subtype;format="deprefix"$$template$", 
			Template.INTERNAL, // change template type here
			"$subtype$", $site;format="normalize"$.element.$type;format="normalize"$.$subtype;format="deprefix"$$template$.class) //
			.cache("false", "false") // change caching here
			.description("Template $subtype$$template$ for type $type$ $subtype$");
	}

	@Override
	public String apply(Env e) {
		Picker html = Picker.load("/$site;format="normalize"$/simple.html" , "#detail");
		Asset a = e.getAsset();
		return html.html(a, e, model(arg("Text",a.getString("$site$_Text"))));
	}
}
