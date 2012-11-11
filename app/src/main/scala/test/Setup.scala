package test

import test.template._
import test.cselement._

class Setup extends wcs.scala.Setup {

  val b = 2100000000000l;

  def site = Site("Test")

  def assets: List[Asset] =
    Template(b, "Layout", "Layout", "test.template.Layout") ::
      CSElement(b + 1, "AllTests", "AllTests", "test.template.AllTests") ::
      SiteEntry(b + 2, "AllTests", "AllTests", "test.template.AllTests", true, b + 1) ::
      Nil

}

