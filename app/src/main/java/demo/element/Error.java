package demo.element;

import org.springframework.stereotype.Component;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.core.Log;

import javax.annotation.Resource;

@Component
public class Error extends Element {

	final static Log log = Log.getLog(Error.class);

    @Resource
    Env env;

	public static AssetSetup setup() {
		return new CSElement("DmError", demo.element.Error.class);
	}

	@Override
	public String apply() {
		log.trace("Demo Error");
		Picker html = Picker.load("/demo/simple.html", "#content");
		html.replace("#title", "Error");
		html.replace("#subtitle", env.getString("error"));
		return html/*.dump(log)*/.html();
	}
}
