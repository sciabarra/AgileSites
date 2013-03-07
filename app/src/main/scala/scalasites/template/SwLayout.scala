package scalasites.template

import wcs.scala._

class SwLayout extends Element with PickerDSL {

  def apply(e: Env) =
    pick("/scalasites/index.html") {
      select("head") {
        "link" -> "href" %= "/cs/scalasites/css/default.css"
      }
     // "#sidebar" %= call("SwFooter", 'name -> "Mike")
    }

}