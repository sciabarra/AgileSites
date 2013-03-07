package scalasites.specs

import wcs.scala._
import wcs.scala.tag._
import wcs.scala.util.Specification

class TagSpec(e: Env) extends Specification {
  implicit val ics = e.ics

  "create list with tag" in {
    import ListobjectTag._
    create("l", "a,b")

    addrow("l", 'a := "1", 'b := "2")
    addrow("l", 'a := "2", 'b := "4")
    addrow("l", 'a := "3", 'b := "8")
    tolist("l", "l")
    ics.GetList("l").numColumns() must_== 2
    ics.GetList("l").numRows() must_== 3
    
  }

}