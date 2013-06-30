package demo.test.api;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.util.TestElement;
import demo.element.Export;

// this test must be run by AgileSites TestRunnerElement
public class EnvTest extends TestElement {

	final static Log log = Log.getLog(EnvTest.class);

	Export it;

	@Before
	public void setUp() {
		it = new Export();
	}

	@Test
	public void testSql() {
		Env e = env();
		String res = e.sql("select from SystemUsers");
		boolean found = false;
		for (int i : e.getRange(res)) {
			String username = e.getString(res, i, "username");
			found = found || username.equals("fwadmin");
			log.trace(username);
		}
		assertTrue(found);

		assertNull(e.sql("select from SystemUsersError"));
	}
	

}
