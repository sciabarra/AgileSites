package Demo.test

import wcs._
import wcs.tag._
import wcs.util.Specification
import wcs.Env

class TagSpec(e: Env) extends Specification {
  implicit val ics = e.ics
  
  "create list with tag" in {
    import Listobject._
    create("l", "a,b")
    addrow("l", 'a := "1", 'b := "2")
    addrow("l", 'a := "2", 'b := "4")
    addrow("l", 'a := "3", 'b := "8")
    tolist("l", "l")
    ics.GetList("l").numColumns() must_== 2
    ics.GetList("l").numRows() must_== 3

  }

}