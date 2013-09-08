package wcs.java.util;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.matchers.JUnitMatchers;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wcs.core.Call;
import wcs.core.URL;
import wcs.java.Env;
import wcs.core.Log;
import wcs.java.Router;


import javax.annotation.Resource;

/**
 * Collection of test helpers for testing elements.
 * 
 * It defines: Asset a, Env e, ICS i
 * 
 * @author msciab
 * 
 */
public class TestElement extends TestCase {

	static final Log log = Log.getLog(TestElement.class);

	protected Document doc;

	/**
	 * Parse generated html
	 * 
	 * @param html
	 */
	public void parse(String html) {
		doc = Jsoup.parse(Util.dumpStream(html));
		log.trace("parse:\n" + doc.toString());
	}

	/**
	 * Return current env not routed (so no variables coming from the router)
	 * 
	 * @return
	 */
	public TestEnv env() {
		return TestRunnerElement.getTestEnv();
	}

	/**
	 * Return the current env routed (so variables coming from router are
	 * available)
	 * 
	 * @return
	 */
	public TestEnv env(String path) {
		if (path == null)
			path = "";
		TestEnv te = env();
		try {
            Call call = Router.getRouter(te.getString("site"), te).route(
                    URL.parse(new URI(path)));
			for (String k : call.keysLeft())
				te.SetVar(k, call.getOnce(k));
		} catch (URISyntaxException e) {
			log.warn(e, "parsing %s", path);
		}
		return te;
	}
 	/**
	 * Parse a call
	 */
	public void parse(Call call) {
		parse(call.toString());
	}

	/**
	 * Create an url
	 */
	public URL url(String url) {
		URI uri;
		try {
			uri = new URI(url);
			return URL.parse(uri.getPath(), uri.getRawQuery());
		} catch (URISyntaxException e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * Create a column - use: col("name", "v1", "v2", "v3")
	 * 
	 * @param elements
	 * @return
	 */
	public List<String> col(String... elements) {
		return Util.listString(elements);
	}

	/**
	 * Return an html node suitable for Jsoup manipulation
	 */

	public Elements select(String cssq) {
		return doc.select(cssq);
	}

	/**
	 * Return the html specified by the cssquery
	 * 
	 * @param cssq
	 * @return
	 */
	public String html(String cssq) {
		return select(cssq).html();
	}

	/**
	 * Return the outer html specified by the cssquery
	 * 
	 * @param cssq
	 * @return
	 */
	public String outerHtml(String cssq) {
		return select(cssq).outerHtml();
	}

	/**
	 * Return the text html specified by the cssquery
	 * 
	 * @param cssq
	 * @return
	 */
	public String text(String cssq) {
		return select(cssq).text();
	}

	/**
	 * Return the attribute specified by the cssquery and the name
	 * 
	 * @param cssq
	 * @return
	 */
	public String attr(String cssq, String attr) {
		return select(cssq).attr(attr);
	}

	/**
	 * Check that the html at the "css" query is the expected Note that is
	 * selected only the internal of the selected node.
	 * 
	 * @param cssq
	 * @param html
	 */
	public void assertHtml(String cssq, String html) {
		String val = html(cssq);
		if (val != null)
			assertThat(val, is(html));
		else
			fail("cannot find " + cssq);
	}

	/**
	 * Check that the html at the "css" query is the expected Note that it is
	 * returned also the selected node.
	 * 
	 * of the selected node.
	 * 
	 * @param cssq
	 * @param html
	 */
	public void assertOuterHtml(String cssq, String html) {
		String val = outerHtml(cssq);
		if (val != null)
			assertThat(val, is(html));
		else
			fail("cannot find " + cssq);
	}

	/**
	 * Check the text, selected with the "css" query, has the expected value
	 * 
	 * 
	 * of the selected node.
	 * 
	 * @param cssq
	 * @param text
	 */

	public void assertText(String cssq, String text) {
		String val = text(cssq);
		if (val != null)
			assertThat(val, is(text));
		else
			fail("cannot find " + cssq);
	}

	/**
	 * Check the text selected with the "css" query, is contained as a
	 * substring.
	 * 
	 * @param cssq
	 * @param text
	 */

	public void assertTextContains(String cssq, String text) {
		String val = text(cssq);
		if (val != null)
			assertThat(val, JUnitMatchers.containsString(text));
		else
			fail("cannot find " + cssq);
	}

	/**
	 * Check the attribute of the node, selected with "css" query, has the
	 * expected value
	 * 
	 * 
	 * of the selected node.
	 * 
	 * @param cssq
	 * @param attr
     * @param value
	 */
	public void assertAttr(String cssq, String attr, String value) {
		Elements elem = doc.select(cssq);
		if (elem != null) {
			String attribute = elem.attr(attr);
			if (attribute != null)
				assertThat(attribute, is(value));
			else
				fail("cannot find attribute " + attr + " in " + cssq);
		} else
			fail("cannot find " + cssq);
	}

	/**
	 * Check the attribute of the node, selected with "css" query, contains the
	 * expected value
	 * 
	 * 
	 * of the selected node.
	 * 
	 * @param cssq
     * @param attr
     * @param value
	 */
	public void assertAttrContains(String cssq, String attr, String value) {
		Elements elem = doc.select(cssq);
		if (elem != null) {
			String attribute = elem.attr(attr);
			if (attribute != null)
				assertThat(attribute, JUnitMatchers.containsString(value));
			else
				fail("cannot find attribute " + attr + " in " + cssq);
		} else
			fail("cannot find " + cssq);
	}

	/**
	 * Dump inner html
	 * 
	 * @param log
	 */
	protected void dump(Log log) {
		log.debug(Util.dumpStream(doc.html()));
	}
	
	/**
	 * Dump selected inner html
	 * 
	 * @param log
	 */
	protected void dump(Log log, String wh) {
		log.debug(Util.dumpStream(doc.select(wh).html()));
	}


	/**
	 * Dump outer html
	 * 
	 * @param log
	 */
	protected void odump(Log log) {
		log.debug(Util.dumpStream(doc.outerHtml()));
	}
	
	/**
	 * Dump outer html of selected element 
	 * 
	 * @param log
	 */
	protected void odump(Log log, String wh) {
		log.debug(Util.dumpStream(doc.select(wh).outerHtml()));
	}

}
