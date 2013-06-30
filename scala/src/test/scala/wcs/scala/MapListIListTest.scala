package wcs.scala

import org.scalatest.WordSpec
import wcs.scala.util.MapListIList

class MapListIListSpec extends WordSpec with Log {

  //sequential

  /*
  error("error ")
  warn("warn ")
  info("info ")
  debug("debug ")
  trace("trace ")
  dump("dump ")
  */

  val l0 = new MapListIList("l0", Map())
  val l1 = new MapListIList("l1", Map("a" -> List("1")))
  val l3 = new MapListIList("l3", Map("b" -> List("1", "2", "3")))
  val l22 = new MapListIList("l22", Map("a" -> List("1", "2"), "b" -> List("10", "20", "30")))

  "MapListIList void should" in {
    
    "one column but unnamed" in {
      assert(l0.numColumns() === 1)

      assert(l0.getColumnName(1) === null)
    }
    
    "no data and zero rows" in {
      assert(l0.hasData === false)
      assert(l0.numRows === 0)
    }

    //ics.GetList("l0").numColumns() === 1 /* not zero! */                
    //ics.GetList("l0").getColumnName(1) === null /* not zero! */

    "no such field" in {
      intercept[java.lang.NoSuchFieldException] {
        l0.getValue("any-value")
      }
    }
  }

  "MapListIList one row should " in {
    "be 1 length" in {
      assert(l1.numColumns === 1)
    }
    "has data" in { assert(l1.hasData === true) }
    "found key" in { assert(l1.getValue("a") === "1") }
    "not found key" in {
      intercept[java.lang.NoSuchFieldException] {
        l1.getValue("b")
      }
    }
  }

  "MapListIList 3 rows should " in {
    "be 3 long" in {
      assert(l3.numRows === 3)
    }

    "be 1 large" in {
      assert(l3.numColumns() === 1)
    }

    "has data" in { assert(l3.hasData === true) }

    "not found key" in {
      intercept[java.lang.NoSuchFieldException] {
        l3.getValue("a")
      }
    }

    "found key" in { assert(l3.getValue("b") != null) }
  }

  "MapListIList 2x2 rows should" in {
    "be 2 long" in {
      assert(l22.numRows === 2)
    }

    "be 2 large" in {
      assert(l22.numColumns() === 2)
    }

    "has data" in { assert(l22.hasData === true) }

    "l22(a)=1" in { assert(l22.getValue("a") === "1") }

    "l22(b)=10" in { assert(l22.getValue("b") === "10") }

    "l22(c) nosuchfield" in {
      intercept[java.lang.NoSuchFieldException] {
        l22.getValue("c")
      }
    }

    "goto second row " in { assert(l22.moveTo(2) === true) }

    "l22(a)=2" in { assert(l22.getValue("a") === "2") }

    "l22(b)=20" in { assert(l22.getValue("b") === "20") }

    "goto third row " in { assert(l22.moveTo(3) === false) }

    "first column name" in { assert(l22.getColumnName(0) === "a") }

    "second column name" in { assert(l22.getColumnName(1) === "b") }

  }

}