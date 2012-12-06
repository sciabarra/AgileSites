package demo

import demo.template._

class ScalaSetup extends wcs.scala.Setup {

  def site = Site("Demo")

  def assets: List[Asset] =
    Template("DSLayout", classOf[demo.template.DSLayout], description = "Demo Layout (Scala)") ::
      Template("DSHeader", classOf[demo.template.DSHeader], description = "Demo Header (Scala)") ::
      CSElement("DSFooter", classOf[demo.cselement.DSFooter], description = "Demo Footer (Scala)") ::
      Nil

}

