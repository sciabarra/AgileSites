package demo.test;
import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.Tree;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
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
		// dump(log);		
		// TODO: test the resuls
	}

}
