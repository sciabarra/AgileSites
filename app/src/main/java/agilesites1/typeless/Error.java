package agilesites1.typeless;

import wcs.java.Element;
import wcs.java.Env;

public class Error extends Element {

	@Override
	public String apply(Env env) {
		return "<h1>Page Not Found</h1>";
	}

}
