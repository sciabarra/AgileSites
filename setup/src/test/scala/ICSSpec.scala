import org.specs2.mutable._
import wcs.test.StubICS
import wcs.ICS

class ICSSpec extends Specification {
   
  val ics = new StubICS(Map("a" -> "1")) 
  ics.addList("l",  Map("a" -> List("1","2","3"), "b"-> List("10","20","30")))
  ics.addList("ll",  Map("a" -> List("1","2","3"), "b"-> List("10","20")))
  
  val x = new ICS(ics)

  "ICS.apply should" in {

    "return None for a non existent variable " in {
      x("b") must_== None
    }

    "return Some thing for existent variables" in {
      x("a") must_== Some("1")
    }
    
  }
  
  "ICS.list should" in {
    "return a void list for non existent values" in {
      x.list("n").size == 0
    }
    
    "return a  list of maps for existent values" in {
      x.list("l").size == 3
    }
    
  }
}