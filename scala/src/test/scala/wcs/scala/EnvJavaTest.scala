package wcs.scala

import wcs.java.Env
import wcs.scala.util.XmlICS

import java.text.SimpleDateFormat
import org.scalatest._
import org.scalatest.matchers.MustMatchers._

class EnvJavaTest extends FlatSpec with Log {

  //sequential 

  val fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  val ics = new XmlICS()

  ics.addMapVar(Map("a" -> "xxx", "d" -> "2012-01-02 12:23:34", "n" -> "1234"))
  ics.addMapList("l", Map("f" -> List("xxx", "2012-01-02 12:23:34", "1234")))

  import wcs.java.util.Util.listString
  ics.setList("ll", Array(listString("f", "a", "b", "c"), listString("g", "1", "2", "3")))

  val env = new Env(ics, "scalademo")

  behavior of "Env"

  it should "check var a is xxx" in {
    nlog(env.getString("a")) must be === "xxx"
  }

  it should "check var d is a date" in {
    val d = env.getDate("d")
    val s = d.getClass.getCanonicalName().toString
    s must be === "java.util.Date"
    fmt.format(d) must be === "2012-01-02 12:23:34"
  }

  it should "check var n is a long" in {
    val n = env.getLong("n")
    n.getClass.getCanonicalName must be === "java.lang.Long"
    n must be === 1234l
  }

  it should "check var z is null" in {
    nlog(env.getString("z")) must be === null
  }

  it should "check d is not a long" in {
    val d = env.getLong("d")
    //println("d=" + d)
    d must be === null
  }

  it should "check n is not a date" in {
    env.getDate("n") must be === null
  }

  it should "check list default field" in {
    env.getString("l", "f") must be === "xxx"
  }

  it should "check list first field" in {
    env.getString("l", "f") must be === "xxx"
    env.getString("l", 1, "f") must be === "xxx"
  }

  it should "check list second field" in {
    env.getString("l", 2, "f") must be === "2012-01-02 12:23:34"
    fmt.format(env.getDate("l", 2, "f")) must be === "2012-01-02 12:23:34"
  }

  it should "check list third field" in {
    env.getString("l", 3, "f") must be === "1234"
    env.getLong("l", 3, "f") must be === 1234
  }

  it should "check loop" in {
    val l = for (i <- 1 to env.getSize("l")) yield {
      env.getString("l", i, "f")
    }
    l(0) must be === "xxx"
    l(1) must be === "2012-01-02 12:23:34"
    l(2) must be === "1234"

  }

  it should "check loop again" in {
    val ll = for (i <- 1 to env.getSize("ll")) yield {
      (env.getString("ll", i, "f"), env.getString("ll", i, "g"))
    }
    ll(0) must be === ("a", "1")
    ll(1) must be === ("b", "2")
    ll(2) must be === ("c", "3")

  }

}