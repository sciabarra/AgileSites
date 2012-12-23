package demo.cselement

import wcs.scala.Element
import wcs.scala.Env

class DSWrapper extends Element {

  def apply(env: Env): String = { "DSWrapper" }

}