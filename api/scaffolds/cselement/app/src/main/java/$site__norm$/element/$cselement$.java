package $site;format="normalize"$.element;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.api.Env;
import wcs.java.Picker;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;

@Index("$site;format="normalize"$/elements.txt")
public class $cselement$ extends Element {
	final static Log log = getLog($cselement$.class);
	public static AssetSetup setup() {
		return new CSElement("$cselement$", $site;format="normalize"$.element.$cselement$.class);
	}

	@Override
	public String apply(Env e) {
		//log.debug("$cselement$");
		Picker html = Picker.load("/$site;format="normalize"$/simple.html" , "#footer");
		return html.html(model(arg("Footer","by Sciabarra.com ltd")));
	}
}
