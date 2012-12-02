package test

import test.cselement._

class Setup extends wcs.scala.Setup {

  def site = Site("Test")

  def assets: List[Asset] =
    CSElement("Runner", classOf[test.cselement.Runner], description = "Runner (Scala)") ::
      SiteEntry("Runner", true, "Runner", classOf[test.cselement.Runner], description = "Tester (Scala)") ::
      Nil

}

