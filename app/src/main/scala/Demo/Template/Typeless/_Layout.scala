package Demo.view.Template.Typeless

import wcs.scala.Element
import wcs.scala.Env

class _Layout extends Element {
  def apply(e: Env) = Seq(
    <h1>Hello</h1>).toString
}