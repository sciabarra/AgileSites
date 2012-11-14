package iui.template;

import wcs.java.Element;
import wcs.java.Env;

public class ILayout extends Element {

	@Override
	public String apply(Env env) {
		return "<h1>Hello, world, from Java</h1>";
	}

}
