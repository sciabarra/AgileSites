package wcs

import scala.xml.NodeSeq

class ElementNotFound extends Element {

  def apply(ics: ICS): NodeSeq = <h1>Not Found</h1>

}