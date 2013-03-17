package agilesites1.page;

import wcs.java.Element;
import wcs.java.Env;

public class Footer extends Element {

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Ciao, " + (name == null ? "World" : name);
	}

}
