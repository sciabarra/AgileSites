package test.cselement;

import wcs.java.Element;
import wcs.java.Env;

public class TjFooter extends Element {

	@Override
	public String apply(Env env) {
		String name = env.getVar("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
