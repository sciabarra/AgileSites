package scalawcs

import scalawcs.template._

class ScalaSetup extends wcs.scala.Setup {

  import TemplateType._

  def site = Site("ScalaWCS")

  // template & cselement
  def assets: List[Asset] =
    Template("", "SwLayout", LAYOUT, classOf[scalawcs.template.SwLayout], description = "ScalaWCS Layout (Scala)") ::
      Template("", "SwHeader", INTERNAL, classOf[scalawcs.template.SwHeader], description = "ScalaWCS Header (Scala)") ::
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

