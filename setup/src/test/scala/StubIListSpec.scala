import org.specs2.mutable._
import wcs.test.StubIList

class StubIListSpec extends Specification {

  val l0 = new StubIList("l0", Map())
  val l1 = new StubIList("l1", Map("a" -> List("1")))
  val l3 = new StubIList("l3", Map("b" -> List("1", "2", "3")))
  val l22 = new StubIList("l22", Map("a" -> List("1", "2"), "b" -> List("10", "20", "30")))

  "StubIList void should" in {
    "be zero-length" in {
      l0.numColumns must_== 0
    }
    "no data" in { l0.hasData must_== false }

    "always null data" in {
      l0.getValue("any-value") must_== null
    }
  }

  "StubIList one row should " in {
    "be 1 length" in {
      l1.numColumns must_== 1
    }
    "has data" in { l1.hasData must_== true }
    "found key" in { l1.getValue("a") must_== "1" }
    "not found key" in { l1.getValue("b") must_== null }
  }

  "StubIList 3 rows should " in {
    "be 3 long" in {
      l3.numRows must_== 3
    }

    "be 1 large" in {
      l3.numColumns() must_== 1
    }

    "has data" in { l3.hasData must_== true }
    "not found key" in { l3.getValue("a") must_== null }
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
    
    "l22(c)=null" in { l22.getValue("c") must_== null }
      
    "goto second row " in { l22.moveTo(2) must_== true } 
    
    "l22(a)=2" in { l22.getValue("a") must_== "2" }
    
    "l22(b)=20" in { l22.getValue("b") must_== "20" }
    
    "goto third row " in { l22.moveTo(3) must_== false } 
    
    "first column name" in { l22.getColumnName(1) must_== "a" }
    
    "second column name" in { l22.getColumnName(2) must_== "b" }
     
  }

}