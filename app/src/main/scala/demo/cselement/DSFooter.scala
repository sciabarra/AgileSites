package demo.cselement
import wcs.scala.Env
import wcs.scala.Element

class DSFooter extends Element {

  def apply(e: Env) =
    <h1>Hello world, from Scala</h1>

}