package scalawcs.template

import wcs.scala._

class SwLayout extends Element with PickerDSL {

  def apply(e: Env) = pick("/index.html") {
    select("head") {
      "link" -> "href" %= "/cs/css/default.css"
    }
    "#sidebar" %= call("DJFooter", 'name -> "Mike")
  }

}