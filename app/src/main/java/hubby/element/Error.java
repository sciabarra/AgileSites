package hubby.element;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.api.Env;
import wcs.java.Element;
import wcs.java.Picker;
import wcs.java.AssetSetup;
import wcs.java.CSElement;

@Index("hubby/elements.txt")
public class Error extends Element {
	final static Log log = getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement("Error", hubby.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		log.error("Hubby Error: ", e.getString("error"));
		Picker p = Picker.load("/hubby/simple.html", "#content");
		return p.innerHtml(model(arg("Title", "Error"),
				        arg("Text", e.getString("error")), 
				        arg("Footer","")));
	}
}
