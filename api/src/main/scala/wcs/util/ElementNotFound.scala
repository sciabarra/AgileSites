package wcs.util

import wcs.Env
import wcs._

import scala.xml.Elem


class ElementNotFound extends wcs.Element {

  def apply(e: Env) = Seq {
    <h1>Element Not Found</h1>
  }

}