package demo.test;

import org.junit.Before;
import org.junit.Test;

import org.springframework.stereotype.Component;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.Tree;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
public class TreeTest extends TestElement {

	final static Log log = Log.getLog(TreeTest.class);

	Tree tree;
	
	@Before
	public void setUp() {
        tree = new Tree();
	}

	@Test
	public void test() {
		parse(tree.apply(env("")));
		odump(log);		
		// TODO: test the resuls
	}

}
