package demo.element;

import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.util.AddIndex;
import wcs.core.Log;

@AddIndex("demo/elements.txt")
public class Error extends Element {

	final static Log log = Log.getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement(137502363001l, "DmError", demo.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		log.trace("Demo Error");
		
		Picker html = Picker.load("/demo/simple.html", "#content");
		html.replace("#title", "Error");
		html.replace("#subtitle", e.getString("error"));
		return html/*.dump(log)*/.html();
	}
}
