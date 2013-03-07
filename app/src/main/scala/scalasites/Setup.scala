package scalasites

import scalasites.template._

class ScalaSetup extends wcs.scala.Setup {

  import TemplateType._

  def site = Site("ScalaSites")

  // template & cselement
  def assets: List[Asset] =
    Template("", "SwLayout", LAYOUT, classOf[scalasites.template.SwLayout], description = "ScalaSites Layout (Scala)") ::
      Template("", "SwHeader", INTERNAL, classOf[scalasites.template.SwHeader], description = "ScalaSites Header (Scala)") ::
      CSElement("SwFooter", classOf[scalasites.cselement.SwFooter], description = "ScalaSites Footer (Scala)") ::
      // test runner
      CSElement("SpecRunner", classOf[scalasites.specs.SpecRunner], description = "Spec Runner (Scala)") ::
      SiteEntry("SpecRunner", true, description = "Spec Runner (Scala)") ::
      Nil

     /*
      CSElement("TestAreaEditor", classOf[scalasites.cselement.TestAreaEditor], element = "OpenMarket/Gator/AttributeTypes/TESTAREA") ::
      AttrTypes("TestAreaEditor",
        <PRESENTATIONOBJECT NAME="TESTAREA">
          <TESTAREA XSIZE="40" YSIZE="10" WRAPSTYLE="SOFT" PERMISSION="Administrators"></TESTAREA>
        </PRESENTATIONOBJECT>) ::
      */
}

