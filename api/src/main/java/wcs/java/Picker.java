package wcs.java;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import wcs.java.util.Log;
import wcs.java.util.Util;

/**
 * Build picking
 * 
 * @author msciab
 * 
 */
public class Picker {

	private static Log log = Log.getLog(Picker.class);

	// TODO: define a better default
	private static final String baseUrl = "http://localhost:8080/";

	private Stack<Element> stack = new Stack<Element>();

	private Element top;
	private Element bottom;

	private void push(Element elem) {
		stack.push(elem);
		top = elem;
	}

	private void pop() {
		stack.pop();
		top = stack.lastElement();
	}

	/**
	 * Get a picker form a given resource in the classpath
	 * 
	 * @param resource
	 * @param cssq
	 * @return
	 */
	public static Picker load(String resource, String cssq) {
		return new Picker(Picker.class.getResourceAsStream(resource), null,
				cssq);
	}

	/**
	 * Get a picker form a given resource in the classpath and select the given
	 * query
	 * 
	 * @param resource
	 * @param cssq
	 * @return
	 */
	public static Picker load(String resource) {
		return new Picker(Picker.class.getResourceAsStream(resource), null,
				null);
	}

	/**
	 * Get a picker from a string
	 * 
	 * @param resource
	 * @param cssq
	 * @return
	 */
	public static Picker create(String html) {
		return new Picker(null, html, null);
	}

	/**
	 * Get a picker from a string and select the given query
	 * 
	 * @param resource
	 * @param cssq
	 * @return
	 */
	public static Picker create(String html, String cssq) {
		return new Picker(null, html, cssq);
	}

	/**
	 * Create a picker for a string
	 */
	private Picker(InputStream is, String html, String cssq) {

		Element elem = null;
		Document doc = null;

		// parse
		try {

			if (is != null) {
				log.debug("parsing resource");
				doc = Jsoup.parse(is, "UTF-8", baseUrl);
			} else if (html != null) {
				log.debug("parsing string");
				doc = Jsoup.parse(html);
			}
		} catch (Exception e) {
			log.warn(e, "Cannot parse string");
		}

		// select internally
		if (doc != null) {
			if (cssq != null) {
				log.debug("selecting " + cssq);
				elem = doc.select(cssq).first();
			} else
				elem = doc;
		} else {
			log.debug("creating a void doc");
			doc = new Document(baseUrl);
			elem = doc;
		}

		// finally assign....
		bottom = elem;
		push(elem);
	}

	/**
	 * Select a new element and return a new Picker
	 * 
	 * @param where
	 * @return
	 * @throws Exception
	 */
	public Picker select(String where) {
		Elements selected = top.select(where);
		if (selected != null && selected.size() > 0)
			push(selected.first());
		else
			log.warn("cannot select " + where);
		return this;
	}

	/**
	 * Return to the precedent selected element
	 * 
	 * @param where
	 * @return
	 */
	public Picker up() {
		pop();
		return this;
	}

	/**
	 * Replace where indicated with the specified html
	 * 
	 * @param where
	 * @param what
	 * @return
	 * @throws Exception
	 */
	public Picker replace(String where, String what) {
		select(where);
		top.html(what);
		up();
		return this;
	}

	// private method to implement both single and remove
	private Picker removeOrSingle(String where, boolean keepFirst) {

		Iterator<org.jsoup.nodes.Element> it = top.select(where).iterator();

		// keep the first
		if (keepFirst)
			if (it.hasNext())
				it.next();

		// remove others
		while (it.hasNext())
			it.next().remove();

		return this;
	}

	/**
	 * Remove nodes specified
	 * 
	 * @param where
	 * @return
	 */
	public Picker remove(String where) {
		return removeOrSingle(where, false);
	}

	/**
	 * Keep only one instance of the node specified
	 * 
	 * @param where
	 * @return
	 */
	public Picker single(String where) {
		return removeOrSingle(where, true);
	}

	/**
	 * Empty the specified note
	 * 
	 * @param where
	 * @return
	 */
	public Picker empty(String where) {
		top.select(where).empty();
		return this;
	}

	/**
	 * Empty the current node
	 * 
	 * @param where
	 * @return
	 */
	public Picker empty() {
		top.empty();
		return this;
	}

	/**
	 * Print the current selected as a string
	 */
	public String toString() {
		// log.debug(doc.toString());
		return top.toString();
	}

	/**
	 * Return the inner html of the selected node
	 */
	public String html() {
		return bottom.html();
	}

	/**
	 * Return the html of the selected node including the node itself
	 */
	public String outerHtml() {
		return bottom.outerHtml();
	}

	/**
	 * Add before a given node
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker before(String where, String what) {
		// top.select(where).parents().first().append(what);
		top.select(where).before(what);
		return this;
	}

	/**
	 * Add after a given node
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker after(String where, String what) {
		// top.select(where).parents().first().prepend(what);
		top.select(where).after(what);
		return this;
	}

	/**
	 * Append the node as a children to the selected node.
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker append(String where, String what) {
		top.select(where).append(what);
		return this;
	}

	/**
	 * Append the node as a children to the current node
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker append(String what) {
		top.append(what);
		return this;
	}

	/**
	 * Set attribute
	 */
	public Picker attr(String where, String attr, String what) {
		top.select(where).attr(attr, what);
		return this;
	}

	/**
	 * Set attribute prefix for all the attributes found
	 */
	public Picker prefixAttrs(String where, String attr, String prefix) {
		for (Element el : top.select(where)) {
			el.attr(attr, prefix + el.attr(attr));
		}
		return this;
	}

	/**
	 * Add a class
	 */
	public Picker addClass(String where, String what) {
		top.select(where).addClass(what);
		return this;
	}

	/**
	 * Convenience method to dump the stream currently created
	 * 
	 * @param stream
	 * @return
	 */
	public Picker dump(Log log) {
		if (log != null)
			log.trace(Util.dumpStream(html()));
		return this;
	}

	public Picker dump() {
		return dump(null);
	}

}