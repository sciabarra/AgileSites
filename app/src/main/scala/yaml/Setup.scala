package yaml

import yaml.template._

class Setup extends wcs.scala.Setup {

  val n = 2200000000000l;

  def site = Site("Yaml")

  def assets: List[Asset] =
    Template(n, "YLayout", "Yaml Layout", "yaml.template.YLayout") ::
      Nil

}

