package wcs.util

import wcs.Env

import scala.xml.NodeSeq

class ElementNotFound extends wcs.Element {

  def apply(e: Env): NodeSeq = <h1>Element Not Found</h1>

}