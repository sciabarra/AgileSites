package agilesites.element;

import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;

public class Json extends Element {

	public static AssetSetup setup() {
		return new CSElement("MyJson", agilesites.element.Json.class);
	}

	@Override
	public String apply(Env e) {
		return "{ 'args': '"+e.getString("args")+"' }";
	}

}
