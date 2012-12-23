package demo.template

import wcs.scala._

class DSLayout extends Element with PickerDSL {

  def apply(e: Env) = pick("/index.html") {
    select("head") {
      "link" -> "href" %= "/cs/css/default.css"
    }
    "#sidebar" %= call("DJFooter", 'name -> "Mike")
  }

}