import org.specs2.mutable._
import wcs.test.StubIList

class MockSpec extends Specification {

  val l0 = new StubIList("l0", Map())
  val l1 = new StubIList("l1", Map("a" -> List("1")))
  val l3 = new StubIList("l3", Map("b" -> List("1","2","3")))  

  "MockIList void should" in {
    "be zero-length" in {
      l0.numColumns must_== 0
    }
    "no data" in { l0.hasData must_== false }

    "always null data" in {
      l0.getValue("any-value") must_== null
    }
  }

  "MockIList one row should " in {
    "be 1 length" in {
      l1.numColumns must_== 1
    }
    "has data" in { l1.hasData must_== true }
    "found key" in { l1.getValue("a") must_== "1" }
    "not found key" in { l1.getValue("b") must_== null }
  }
  
   "MockIList 3 rows should " in {
    "be 3 length" in {
      l3.numRows must_== 3
    }
    "has data" in { l3.hasData must_== true }
    "not found key" in { l3.getValue("a") must_== null }
    "found key" in { l3.getValue("b") must_!= null }
  }
  
}