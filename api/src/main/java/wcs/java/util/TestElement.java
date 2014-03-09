package wcs.java.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static wcs.Api.arg;
import static wcs.Api.dumpStream;

import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.hamcrest.CoreMatchers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import wcs.Api;
import wcs.api.Arg;
import wcs.api.Call;
import wcs.api.Content;
import wcs.api.Log;
import wcs.api.URL;

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
		doc = Jsoup.parse(Api.dumpStream(html));
		log.trace("parse:\n" + doc.toString());
	}

	/**
	 * Return current env not routed (so no variables coming from the router)
	 * 
	 * @return
	 */
	public TestEnv env(Arg... args) {
		TestEnv env = TestRunnerElement.getTestEnv();
		if (args.length > 0) {
			env = new TestEnv(env, args);
		}
		return env;
	}

	// only used in List.toArray conversions
	final private static Arg[] args0 = new Arg[0];

	/**
	 * Return the current env routed (so variables coming from router are
	 * available)
	 * 
	 * @return
	 */
	public TestEnv env(String path, Arg... args) {
		if (path == null)
			path = "";
		TestEnv te = env(args);
		List<Arg> list = new LinkedList<Arg>();
		for (Arg arg : args)
			list.add(arg);
		try {
			Call call = wcs.core.WCS.getRouter(te.ics()).route(te,
					URL.parse(new URI(path)));
			for (String k : call.keysLeft())
				list.add(arg(k, call.getOnce(k)));
		} catch (URISyntaxException e) {
			log.warn(e, "parsing %s", path);
		}
		return env(list.toArray(args0));
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
	 * @param html
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
	 * @param html
	 */

	public void assertTextContains(String cssq, String text) {
		String val = text(cssq);
		if (val != null)
			assertThat(val, CoreMatchers.containsString(text));
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
	 * @param html
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
	 * @param html
	 */
	public void assertAttrContains(String cssq, String attr, String value) {
		Elements elem = doc.select(cssq);
		if (elem != null) {
			String attribute = elem.attr(attr);
			if (attribute != null)
				assertThat(attribute, CoreMatchers.containsString(value));
			else
				fail("cannot find attribute " + attr + " in " + cssq);
		} else
			fail("cannot find " + cssq);
	}

	/**
	 * Dump generated outer html
	 * 
	 * @param log
	 */
	protected void dump(Log log) {
		log.debug(dumpStream(doc.outerHtml()));
	}

	/**
	 * Dump generated outer html to a print writer (System.out for example)
	 * 
	 * @param log
	 */
	protected void dump(PrintStream out) {
		out.println(dumpStream(doc.outerHtml()));
	}

	/**
	 * Dump generated outer html
	 * 
	 * @param log
	 */
	protected void dump(Log log, Content c) {
		if (c == null)
			log.debug("NULL");
		else
			log.debug(c.dump());
	}

	/**
	 * Dump generated outer html
	 * 
	 * @param log
	 */
	protected void dump(PrintStream out, Content c) {
		if (c == null)
			out.println("NULL");
		else
			out.println(c.dump());
	}

	/**
	 * Dump a content attribute in a logger
	 * 
	 * @param log
	 */
	protected void dump(Log log, Content c, String name) {
		if (c.exists(name)) {
			log.debug("%s=%s", name, c.dump(name));
		} else {
			log.debug("not found: %s", name);
		}
	}

	/**
	 * Dump a content attribute to a System.out
	 * 
	 * @param log
	 */
	protected void dump(PrintStream out, Content c, String name) {
		if (c.exists(name)) {
			out.println(name + "=" + c.dump(name));
		} else {
			out.println("not found:" + name);
		}
	}
}
