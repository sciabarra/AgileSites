package wcs.java.util;

import wcs.api.Arg;
import wcs.api.Content;
import wcs.api.Model;
import wcs.java.Env;
import COM.FutureTense.Interfaces.ICS;

/**
 * Testable env - it can be built with a content that can override variables and
 * lists
 * 
 * @author msciab
 * 
 */
public class TestEnv extends Env implements Content {

	private Model content;
	private ICS ics;

	/**
	 * Create a test env using ics and additional arguments
	 * 
	 * @param ics
	 * @param args
	 */
	public TestEnv(ICS ics, Arg... args) {
		super(ics);
		this.ics = ics;
		this.content = new Model(args);
	}

	/**
	 * Create a test env using anothers env and additional arguments
	 * 
	 * @param ics
	 * @param args
	 */
	public TestEnv(TestEnv env, Arg... args) {
		super(env.ics);
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
