package agilewcs.page;

import wcs.java.Element;
import wcs.java.Env;

public class Detail extends Element {

	@Override
	public String apply(Env env) {
		String name = env.getString("name");
		return "Hello, " + (name == null ? "World" : name);
	}

}
