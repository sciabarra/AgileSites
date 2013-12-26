package demo.element;

import static wcs.Api.arg;
import static wcs.Api.model;
import wcs.api.Env;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Picker;

@Index("demo/elements.txt")
public class Error extends Element {

	final static Log log = Log.getLog(Error.class);

	public static AssetSetup setup() {
		return new CSElement("Error", demo.element.Error.class);
	}

	@Override
	public String apply(Env e) {
		if (log.debug())
			log.debug("Demo Error");

		return Picker.load("/demo/simple.html", "#content")
				.html(model(arg("Title", "Error"),
						arg("Text", e.getString("error"))));
	}
}
