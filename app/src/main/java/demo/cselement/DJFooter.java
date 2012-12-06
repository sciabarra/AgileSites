package demo.cselement;

import wcs.java.Element;
import wcs.java.Env;

public class DJFooter extends Element {

	@Override
	public String apply(Env env) {
		String name = env.getVar("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
