package test

import test.cselement._

class Setup extends wcs.scala.Setup {

  def site = Site("Test")

  def assets: List[Asset] =
    CSElement("SpecRunner", classOf[test.cselement.Runner], description = "Spec Runner (Scala)") ::
      SiteEntry("SpecRunner", true, "SpecRunner", classOf[test.cselement.Runner], description = "Spec Runner (Scala)") ::
      Nil

}

