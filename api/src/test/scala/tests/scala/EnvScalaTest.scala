package tests.scala
import org.specs2.mutable.Specification
import wcs.implx.XmlICS
import wcs.scala.Env

class EnvScalaTest extends Specification {

  val ics = new XmlICS()
  ics.addMapVar(Map("a" -> "1", "b" -> "2"))
  ics.addMapList("l1", Map("v" -> List("hello")))
  ics.addMapList("l3", Map("w" -> List("1", "2", "3")))
  ics.addMapList("l22", Map("x" -> List("10", "20"), "y" -> List("100", "200")))

  val env = new Env(ics)
  "Env should" in {
    "check var a is 1" in {
      //e("a")
      
    }
    
    "check var z is null" in {
      //log(env.getVar("z")) must_== null
    }
  }

}