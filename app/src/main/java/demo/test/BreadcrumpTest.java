package demo.test;
import org.junit.Before;
import org.junit.Test;
import wcs.api.Index;
import wcs.api.Log;
import wcs.java.util.TestElement;
import demo.element.Breadcrump;

// this test must be run by AgileSites TestRunnerElement
@Index("demo/tests.txt")
public class BreadcrumpTest extends TestElement {

	static final Log log = Log.getLog(BreadcrumpTest.class);
	Breadcrump it;

	@Before
	public void setUp() {
		it = new Breadcrump();
	}

	@Test
	public void testHome() {
		parse(it.apply(env("")));
		//dump(log);
		assertText("b", "Home");
		//assertText("h1", "Breadcrump");
	}
	
	@Test
	public void testSection() {
		parse(it.apply(env("/Section")));
		dump(log);
		//assertText("")
		assertText("a", "Home");
		assertText("b", "Section");
	}

	@Test
	public void testArticle() {
		parse(it.apply(env("/Article")));
		//log.debug(text("a"));
		assertText("a:eq(0)", "Home");
		assertText("a:eq(1)", "Section");
		assertText("b", "Article");
		//assertText("")
		//assertText("h1", "Breadcrump");
	}

}
