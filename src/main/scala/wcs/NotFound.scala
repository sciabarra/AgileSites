package wcs

import scala.xml.NodeSeq

class ElementNotFound extends Element {

  def apply(x: X): NodeSeq = <h1>Not Found</h1>

}