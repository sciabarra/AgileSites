package agilesites1.page;

import wcs.java.Element;
import wcs.java.Env;

public class Link extends Element {

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
