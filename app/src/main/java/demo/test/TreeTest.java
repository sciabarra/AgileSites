package demo.test;

import org.junit.Before;
import org.junit.Test;

import wcs.java.util.TestElement;
import demo.element.Tree;

// this test must be run by AgileSites TestRunnerElement
public class TreeTest extends TestElement {

	Tree it;
	
	@Before
	public void setUp() {
		it = new Tree();
	}

	@Test
	public void test() {
		parse(it.apply(env()));
		assertText("h1", "Tree");
	}

}
