package demo.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Component;
import wcs.java.util.TestElement;
import wcs.core.Log;
import demo.element.Breadcrump;

import javax.annotation.Resource;

// this test must be run by AgileSites TestRunnerElement
@Component
public class BreadcrumpTest extends TestElement {

	static final Log log = Log.getLog(BreadcrumpTest.class);

    @Resource
    Breadcrump breadcrump;

	@Before
	public void setUp() {
        breadcrump = new Breadcrump();
	}

	@Test
	public void testHome() {
		parse(breadcrump.apply());
		//dump(log);
		assertText("b", "Home");
		//assertText("h1", "Breadcrump");
	}

	@Test
	public void testSection() {
		//parse(it.apply(env("/Section")));
        parse(breadcrump.apply());
		dump(log);
		//assertText("")
		assertText("a", "Home");
		assertText("b", "Section");
	}

	@Test
	public void testArticle() {
		//parse(it.apply(env("/Article")));
        parse(breadcrump.apply());
		//log.debug(text("a"));
		assertText("a:eq(0)", "Home");
		assertText("a:eq(1)", "Section");
		assertText("b", "Article");
		//assertText("")
		//assertText("h1", "Breadcrump");
	}

}
