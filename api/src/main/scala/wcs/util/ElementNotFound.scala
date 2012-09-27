package wcs.util

import wcs.X

import scala.xml.NodeSeq

class ElementNotFound extends wcs.Element {

  def apply(x: X): NodeSeq = <h1>Element Not Found</h1>

}