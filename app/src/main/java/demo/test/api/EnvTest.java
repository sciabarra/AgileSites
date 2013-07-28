package demo.test.api;

import org.junit.Before;
import org.junit.Test;

import wcs.core.Log;
import wcs.java.Env;
import wcs.java.util.AddIndex;
import wcs.java.util.TestElement;

// this test must be run by AgileSites TestRunnerElement
@AddIndex("demo/tests.txt")
public class EnvTest extends TestElement {

	final static Log log = Log.getLog(EnvTest.class);

	@Before
	public void setUp() {
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
