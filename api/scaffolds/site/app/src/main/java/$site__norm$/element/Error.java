package $site;format="normalize"$.element;
import static wcs.core.Common.*;
import static wcs.core.Log.*;
import wcs.core.Log;
import wcs.core.Model;
import wcs.core.Index;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;

@Index("$site;format="normalize"$/elements.txt")
public class Error extends Element {
	final static Log log = getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement("$site$_Error", $site;format="normalize"$.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		log.error("$site$ Error: ", e.getString("error"));
		Picker p = Picker.load("/$site;format="normalize"$/simple.html", "#content");
		Model m = model(arg("Title", "Error"),
				        arg("Text", e.getString("error")), 
				        arg("Footer","") );
		return p.innerHtml(m);
	}
}
