package test

import test.cselement._

class Setup extends wcs.scala.Setup {

  def site = Site("Test")

  def assets: List[Asset] =
    CSElement("SpecRunner", classOf[test.cselement.SpecRunner], description = "Spec Runner (Scala)") ::
      SiteEntry("SpecRunner", true, description = "Spec Runner (Scala)") ::
      CSElement("TestEditor", classOf[test.cselement.TestEditor], element = "OpenMarket/Gator/AttributeTypes/TestEditor") ::
      SiteEntry("TestEditor", element = "OpenMarket/Gator/AttributeTypes/TestEditor") ::
      Nil

}

