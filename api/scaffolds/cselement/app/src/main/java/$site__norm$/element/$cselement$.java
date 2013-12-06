package $site;format="normalize"$.element;
import static wcs.core.Common.*;
import wcs.core.Log;
import wcs.core.Index;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;
import wcs.java.Picker;

@Index("$site;format="normalize"$/elements.txt")
public class $cselement$ extends Element {
	final static Log log = Log.getLog($cselement$.class);
	public static AssetSetup setup() {
		return new CSElement("$site$_$cselement$", $site;format="normalize"$.element.$cselement$.class);
	}

	@Override
	public String apply(Env e) {
		//log.debug("$cselement$");
		Picker html = Picker.load("/$site;format="normalize"$/index.html" , "#footer");
		return html.html(model(arg("Footer","(c) Sciabarra ltd")));
	}
}
