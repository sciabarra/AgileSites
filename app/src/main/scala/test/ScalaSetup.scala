package test

import test.cselement._

class Setup extends wcs.scala.Setup {

  def site = Site("Test")

  def assets: List[Asset] =
    CSElement("ScalaTests", "Scala Tests", classOf[test.cselement.ScalaTests]) ::
      SiteEntry("ScalaTests", "Scala Tests", "ScalaTests", true) ::
      Nil

}

