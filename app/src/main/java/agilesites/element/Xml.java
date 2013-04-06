package agilesites.element;

import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;

public class Xml extends Element {

	public static AssetSetup setup() {
		return new CSElement("MyXml", agilesites.element.Xml.class);
	}

	@Override
	public String apply(Env e) {
		return "<args>"+e.getString("args")+"</args>";
	}

}
