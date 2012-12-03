package wcs.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Stack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Build picking
 * 
 * @author msciab
 * 
 */
public class Picker {
	private static Log log = new Log(Picker.class);

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
	 * Create a picker for a given resource
	 * 
	 * @param resource
	 */
	public Picker(String resource, String cssq) {
		InputStream is = Picker.class.getResourceAsStream(resource);
		Element elem = null;
		Document doc = null;

		// parse
		try {
			log.debug("parsing " + resource);
			doc = Jsoup.parse(is, "UTF-8", baseUrl);
			System.out.println(doc.toString());
			// stack.push(doc.body());
		} catch (IOException e) {
			log.warn("Cannot parse " + resource);
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
	 */
	public Picker select(String where) {
		push(top.select(where).first());
		return this;
	}

	/**
	 * Return to the precedent selected element
	 * 
	 * @param where
	 * @return
	 */
	public Picker unselect() {
		pop();
		return this;
	}

	/**
	 * Create a picker for a given resource selecting a specific subset
	 * 
	 * @param resource
	 * @param cssq
	 */
	public Picker(String resource) {
		this(resource, null);
	}

	/**
	 * Replace where indicated with the specified html
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker replace(String where, String what) {
		select(where);
		top.html(what);
		unselect();
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
	 * Print the current selected as a string
	 */
	public String toString() {
		// log.debug(doc.toString());
		return top.toString();
	}

	/**
	 * Return as a string the whole
	 */
	public String html() {
		// log.debug(doc.toString());
		return bottom.toString();
	}

	/**
	 * Append
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker append(String where, String what) {
		top.select(where).parents().first().append(what);
		return this;
	}

	/**
	 * Prepend
	 * 
	 * @param where
	 * @param what
	 * @return
	 */
	public Picker prepend(String where, String what) {
		top.select(where).parents().first().prepend(what);
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
	 * Set attribute
	 */
	public Picker addClass(String where, String what) {
		top.select(where).addClass(what);
		return this;
	}

}