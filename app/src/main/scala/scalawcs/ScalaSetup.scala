package scalawcs

import scalawcs.template._

class ScalaSetup extends wcs.scala.Setup {

  def site = Site("ScalaWCS")

  def assets: List[Asset] =
    // template & cselement
    Template("SwLayout", classOf[scalawcs.template.SwLayout], description = "ScalaWCS Layout (Scala)") ::
      Template("SwHeader", classOf[scalawcs.template.SwHeader], description = "ScalaWCS Header (Scala)") ::
      CSElement("SwFooter", classOf[scalawcs.cselement.SwFooter], description = "ScalaWCS Footer (Scala)") ::
      // test runner
      CSElement("SpecRunner", classOf[scalawcs.specs.SpecRunner], description = "Spec Runner (Scala)") ::
      SiteEntry("SpecRunner", true, description = "Spec Runner (Scala)") ::
      Nil

     /*
      CSElement("TestAreaEditor", classOf[scalawcs.cselement.TestAreaEditor], element = "OpenMarket/Gator/AttributeTypes/TESTAREA") ::
      AttrTypes("TestAreaEditor",
        <PRESENTATIONOBJECT NAME="TESTAREA">
          <TESTAREA XSIZE="40" YSIZE="10" WRAPSTYLE="SOFT" PERMISSION="Administrators"></TESTAREA>
        </PRESENTATIONOBJECT>) ::
        */

}

