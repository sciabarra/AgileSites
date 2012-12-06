package demo.template

import wcs.scala._

class DSHeader extends Element {

  def apply(e: Env) =
    <h1>Hello world, from Scala</h1>

}