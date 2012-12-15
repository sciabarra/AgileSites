package test

import test.cselement._

class Setup extends wcs.scala.Setup {

  def site = Site("Test")

  def assets: List[Asset] =
    CSElement("SpecRunner", classOf[test.cselement.SpecRunner], description = "Spec Runner (Scala)") ::
      SiteEntry("SpecRunner", true, description = "Spec Runner (Scala)") ::
      CSElement("TestAreaEditor", classOf[test.cselement.TestAreaEditor], element = "OpenMarket/Gator/AttributeTypes/TESTAREA") ::
      AttrTypes("TestAreaEditor",
        <PRESENTATIONOBJECT NAME="TESTAREA">
          <TESTAREA XSIZE="40" YSIZE="10" WRAPSTYLE="SOFT" PERMISSION="Administrators"></TESTAREA>
        </PRESENTATIONOBJECT>) ::
        Nil

}

