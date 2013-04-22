package tests.java
import org.specs2.mutable.Specification
import wcs.scala.Log
import wcs.scala.XmlICS
import wcs.java.Env
import java.text.SimpleDateFormat

class EnvJavaSpec extends Specification with Log {

  sequential 
  
  val fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val ics = new XmlICS()
  
  ics.addMapVar(Map("a" -> "xxx", "d" -> "2012-01-02 12:23:34", "n" -> "1234"))
  ics.addMapList("l", Map("f" -> List("xxx", "2012-01-02 12:23:34", "1234")))
  
  import wcs.java.util.Util.listString
  ics.setList("ll", Array( listString("f", "a","b","c"), listString("g", "1", "2", "3")))

  val env = new Env(ics, null)
  "Env should" in {

    "check var a is xxx" in {
      nlog(env.getString("a")) must_== "xxx"
    }

    "check var d is a date" in {
      val d = env.getDate("d")
      val s = d.getClass.getCanonicalName().toString
      s must_== "java.util.Date"
      fmt.format(d) must_== "2012-01-02 12:23:34"
    }

    "check var n is a long" in {
      val n = env.getLong("n")
      n.getClass.getCanonicalName must_== "java.lang.Long"
      n must_== 1234l
    }

    "check var z is null" in {
      nlog(env.getString("z")) must_== null
    }

    "check d is not a long" in {
      val d = env.getLong("d")
      //println("d=" + d)
      d must_== null
    }

    "check n is not a date" in {
      env.getDate("n") must_== null
    }

    "check list default field" in {
      env.getString("l", "f") must_== "xxx"
    }

    "check list first field" in {
      env.getString("l", "f") must_== "xxx"
      env.getString("l", 1, "f") must_== "xxx"
    }

    "check list second field" in {
      env.getString("l", 2, "f") must_== "2012-01-02 12:23:34"
      fmt.format(env.getDate("l", 2, "f")) must_== "2012-01-02 12:23:34"
    }

    "check list third field" in {
      env.getString("l", 3, "f") must_== "1234"
      env.getLong("l", 3, "f") must_== 1234
    }

    "check loop" in {
      val l = for (i <- 1 to env.getSize("l")) yield {
        env.getString("l", i, "f")
      }
      l(0) must_== "xxx"
      l(1) must_== "2012-01-02 12:23:34"
      l(2) must_== "1234"

    }
    
    
    "check loop again" in {
      val ll = for (i <- 1 to env.getSize("ll")) yield {
        (env.getString("ll", i, "f"), env.getString("ll", i, "g"))
      }
      ll(0) must_== ("a", "1")
      ll(1) must_== ("b", "2")
      ll(2) must_== ("c", "3")

    }
  }

}