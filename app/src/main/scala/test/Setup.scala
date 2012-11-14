package test

import test.template._
import test.cselement._

class Setup extends wcs.scala.Setup {

  val b = 2100000000000l;

  def site = Site("Test")

  def assets: List[Asset] =
    Template(b, "JavaTests", "Java Tests", "test.template.JavaTests") ::
      CSElement(b + 1, "ScalaTests", "Scala Tests", "test.cselement.ScalaTests") ::
      SiteEntry(b + 2, "ScalaTests", "Scala Tests", "ScalaTests", true, b + 1) ::
      Nil

}

