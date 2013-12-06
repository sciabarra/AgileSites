package wcs.java.util;

import wcs.core.Arg;
import wcs.core.Content;
import wcs.core.Model;
import wcs.java.Env;
import COM.FutureTense.Interfaces.ICS;

/**
 * Testable env - it can be built with a content who can override variables and
 * lists
 * 
 * @author msciab
 * 
 */
public class TestEnv extends Env implements Content {

	private Model content;
	private ICS ics;
	private String site;

	public TestEnv(ICS ics, String site, Arg... args) {
		super(ics, site);
		this.ics = ics;
		this.site = site;
		this.content = new Model(args);
	}
	
	public TestEnv(TestEnv env, Arg...args) {
		super(env.ics, env.site);
		this.content = new Model(content, args);
	}
	
	@Override
	public boolean exists(String attribute) {
		return content.exists(attribute) || super.exists(attribute);
	}

	@Override
	public boolean exists(String attribute, int pos) {
		return content.exists(attribute, pos) || super.exists(attribute, pos);
	}

	@Override
	public String getString(String attribute) {
		String s = content.getString(attribute);
		return s == null ? super.getString(attribute) : s;
	}

	@Override
	public String getString(String attribute, int n) {
		String s = content.getString(attribute, n);
		return s == null ? super.getString(attribute, n) : s;
	}
}
