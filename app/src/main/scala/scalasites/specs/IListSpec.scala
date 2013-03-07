package scalasites.specs

import wcs.scala.tag._
import wcs.scala.util.Specification
import COM.FutureTense.Interfaces.IList
import COM.FutureTense.Interfaces.ICS
import wcs.scala.Log

/**
 * This class validates some assertions that are implemented by StubIList
 */
class IListSpec(val e: wcs.scala.Env) extends Specification with Log {
  implicit val ics = e.ics

  info("IListSpec")

  import ListobjectTag._

  //val l0 = new StubIList("l0", Map())
  create("l0", "")
  tolist("l0", "l0")
  val l0 = ics.GetList("l0")

  //val l1 = new StubIList("l1", Map("a" -> List("1")))
  create("l1", "a")
  addrow("l1", 'a := "1")
  tolist("l1", "l1")
  val l1 = ics.GetList("l1")

  //val l3 = new StubIList("l3", Map("b" -> List("1", "2", "3")))
  create("l3", "b")
  addrow("l3", 'b := "1")
  addrow("l3", 'b := "2")
  addrow("l3", 'b := "3")
  tolist("l3", "l3")
  val l3 = ics.GetList("l3")

  //val l22 = new StubIList("l22", Map("a" -> List("1", "2"), "b" -> List("10", "20", "30")))
  create("l22", "a,b")
  addrow("l22", 'a := "1", 'b := "10")
  addrow("l22", 'a := "2", 'b := "20")
  tolist("l22", "l22")
  //addrow("l22", 'b := "30")
  val l22 = ics.GetList("l22")

  "StubIList void should" in {
    " one column but unnamed" in {
      l0.numColumns() must_== 1
      l0.getColumnName(1) must_== null
    }
    "no data and zero rows" in {
      l0.hasData must_== false
      l0.numRows must_== 0
    }

    //ics.GetList("l0").numColumns() must_== 1 /* not zero! */                
    //ics.GetList("l0").getColumnName(1) must_== null /* not zero! */

    "no such field" in {
      l0.getValue("any-value") must throwA[java.lang.NoSuchFieldException]
    }
  }

  "StubIList one row should " in {
    "be 1 length" in {
      l1.numColumns must_== 1
    }
    "has data" in { l1.hasData must_== true }
    "found key" in { l1.getValue("a") must_== "1" }
    "not found key" in { l1.getValue("b") must throwA[java.lang.NoSuchFieldException] }
  }

  "StubIList 3 rows should " in {
    "be 3 long" in {
      println("ncol=" + l3.numColumns())
      l3.numRows must_== 3
    }

    "be 1 large" in {

      l3.numColumns() must_== 1
    }

    "has data" in { l3.hasData must_== true }
    "not found key" in { l3.getValue("a") must throwA[java.lang.NoSuchFieldException] }
    "found key" in { l3.getValue("b") must_!= null }
  }

  "StubIList 2x2 rows should" in {
    "be 2 long" in {
      l22.numRows must_== 2
    }

    "be 2 large" in {
      l22.numColumns() must_== 2
    }

    "has data" in { l22.hasData must_== true }

    "l22(a)=1" in { l22.getValue("a") must_== "1" }

    "l22(b)=10" in { l22.getValue("b") must_== "10" }

    "l22(c) nosuchfield" in { l22.getValue("c") must throwA[java.lang.NoSuchFieldException] }

    "goto second row " in { l22.moveTo(2) must_== true }

    "l22(a)=2" in { l22.getValue("a") must_== "2" }

    "l22(b)=20" in { l22.getValue("b") must_== "20" }

    "goto third row " in { l22.moveTo(3) must_== false }

    "first column name" in { l22.getColumnName(0) must_== "a" }

    "second column name" in { l22.getColumnName(1) must_== "b" }

  }

}