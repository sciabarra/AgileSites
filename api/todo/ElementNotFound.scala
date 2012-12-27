package wcs.util

import wcs.scala.Element
import wcs.scala.Env

class ElementNotFound extends Element {

  def apply(e: Env) =
    <h1>Element Not Found</h1>.toString

}