package docs

import docs.template._

class Setup extends wcs.scala.Setup {

  val n = 2200000000000l;

  def site = Site("Docs")

  def assets: List[Asset] =
    Template(n, "Layout", "Layout", "docs.template.Layout") ::
      Nil

}

