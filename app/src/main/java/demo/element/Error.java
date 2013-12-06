package demo.element;

import wcs.core.Index;
import wcs.core.Log;
import wcs.java.Picker;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;

@Index("demo/elements.txt")
public class Error extends Element {

	final static Log log = Log.getLog(Error.class);
	
	public static AssetSetup setup() {
		return new CSElement("DmError", demo.element.Error.class);
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
