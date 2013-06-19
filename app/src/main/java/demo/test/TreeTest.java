package demo.test;

import org.junit.Before;
import org.junit.Test;

import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.Tree;

// this test must be run by AgileSites TestRunnerElement
public class TreeTest extends TestElement {

	final static Log log = Log.getLog(TreeTest.class);
	
	Tree it;
	
	@Before
	public void setUp() {
		it = new Tree();
	}

	@Test
	public void test() {
		parse(it.apply(env("")));
		odump(log);		
		// TODO: test the resuls
	}

}
