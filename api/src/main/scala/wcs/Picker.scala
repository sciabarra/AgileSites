package wcs

import wcs.{ Element => _ }
import org.jsoup._
import org.jsoup.nodes.Document
import org.jsoup.nodes.{ Element => SoupElement }
import javax.lang.model.util.Elements
import collection.JavaConversions._
import scala.xml.Elem
import org.jsoup.parser.Tag

/**
 * Html Picker
 */
trait Picker {

  private var stack: List[SoupElement] = Nil

  val base: String

  private def push(el: SoupElement) { stack = el :: stack }
  private def pop = { val head = top; stack = stack.tail; head }
  private def top = stack.head

  /**
   *
   *
   * ``pick(__resource__) { /* body */ }``
   *
   * Load an html from resources then apply the replacements in the body.
   *
   * Result is a string. You can nest multiple picks and use the string resulting from the pick in another pick.
   *
   * ``pick(__resource__, _cssquery__) { /* ... */ }``
   *
   * Load an html resources, then restrict application to the block selected with a cssquery.
   *
   * The returned html is the result of the restriction with the cssquery
   *
   * Example:
   *
   * <pre>
   * pick("/index.html", "#body") { /* ... */ }
   * </pre>
   */
  def pick(src: String, cssquery: String = "/")(body: => SoupElement): String = {
    val in = getClass.getResourceAsStream(src)
    val doc: SoupElement = Jsoup.parse(in, null, base).select(cssquery).get(0)
    push(doc)
    pop.html()
  }

  /**
   * ``current``
   *
   * Return the current selected or picked node
   *
   * Useful for inspecting without changing it.
   *
   */
  def current = top

  /**
   * ``select("#body") { /* ... */ }``
   *
   * Restrict application of replacements (the ``%`` operators) to a subset of the current html.
   *
   * It does not return nothing
   *
   */
  def select(cssquery: String)(body: => Unit) {
    // TODO
  }

  /**
   * ``__cssquery__ % __body__``
   *
   * Replace the css query with the body.
   *
   * Example:
   *
   * <pre>
   * "h1" % <h1>{a("Title")}</h1>
   * </pre>
   *
   * - If the __body__ is a ``scala.xml.Node`` it is implicitly converted in a Jsoup element.
   * - If  if the body is a Sequence, the replacement is iterated and the output appended.
   * - If the __cssquery__ matches more than one element, the replacement is performed for all of them, and iterated if the body is a sequence.
   *
   *
   */
  def %(cssq: String, element: SoupElement) {

  }
  /**
   * ``__cssquery__ %! __body_``
   *
   * Replace the css query with the body eliminating duplicates.
   *
   * Works like the ``%`` operator except that if the __cssquery__ matches more than one element, multiple elements are removed.
   *
   */
  def %!(cssq: String, element: SoupElement) {

  }

  /**
   * ``__cssquery__ %> __body_``
   *
   * Works like ``%`` except the insertion is done in the inner body of the selected element.
   *
   * In case of multiple matches and replacement is a sequence the behavior is the same as ``%``.
   *
   */
  def %>(cssq: String, element: SoupElement) {

  }

  /**
   * ``__cssquery__ %!> __body_``.
   *
   * It combines ``%|`` and ``%>``.
   *
   * Works like ``%`` except the insertion is done in the inner body of the selected element and duplicates matching the ``__cssquery__`` are removed.
   *
   */
  def %!>(cssq: String, element: SoupElement) {

  }

  /**
   * ``! __cssquery__ ``
   *
   * Remove everything matched by the __cssquery__
   *
   */
  def unary_!(cssq: String) {

  }

  /**
   * Implicit conversion of a soup to a jquery
   */
  implicit def elem2element(elem: Elem): SoupElement = Jsoup.parseBodyFragment(elem.toString)

}


