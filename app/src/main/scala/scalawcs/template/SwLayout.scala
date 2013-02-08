package scalawcs.template

import wcs.scala._

class SwLayout extends Element with PickerDSL {

  def apply(e: Env) =
    pick("/scalawcs/index.html") {
      select("head") {
        "link" -> "href" %= "/cs/scalawcs/css/default.css"
      }
     // "#sidebar" %= call("SwFooter", 'name -> "Mike")
    }

}