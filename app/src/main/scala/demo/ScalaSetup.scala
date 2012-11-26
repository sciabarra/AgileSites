package demo

import demo.template._

class ScalaSetup extends wcs.scala.Setup {

  def site = Site("Demo")

  def assets: List[Asset] =
    Template("", "DyLayout", "Demo Yaml Layout", classOf[demo.template.DyLayout]) ::
      Nil

}

