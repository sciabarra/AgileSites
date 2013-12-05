
package $site;format="normalize"$.element;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.Model;
import wcs.java.Picker;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.util.AddIndex;

@AddIndex("$site;format="normalize"$/elements.txt")
public class Error extends Element {

	final static Log log = Log.getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement("$site$_Error", $site;format="normalize"$.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		log.error("$site$ Error: ", e.getString("error"));
		Picker p = Picker.load("/$site;format="normalize"$/simple.html", "#content");
		Model m = new Model(arg("Title", "Error"),
				            arg("Text", e.getString("error")) );
		return p/*.dump(log)*/.html(m);
	}
}
