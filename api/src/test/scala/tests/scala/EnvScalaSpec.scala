package tests.scala
import org.specs2.mutable.Specification
import wcs.implx.XmlICS
import wcs.scala.Env
import java.text.SimpleDateFormat
import wcs.scala.Log
import wcs.java.Config

class EnvScalaTest extends Specification with Log {

  val ics = new XmlICS()
  ics.addMapVar(Map("a" -> "xxx", "d" -> "2012-01-02 12:23:34", "n" -> "1234"))
  ics.addMapList("l", Map("f" -> List("yyy", "2012-02-03 12:23:34", "1234")))

  val fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val d1 = fmt.parse("2012-01-02 12:23:34")
  val d2 = fmt.parse("2012-02-03 12:23:34")

  val e = new Env(ics, "agilesites")
  "Env should" in {

    " var a is xxx" in {
      e("a") must_== "xxx"
    }

    "no var is '' " in {
      e("z") must_== ""
    }

    "exist var a" in {
      nlog("exist a=", e.exist("a")) must_== true
    }

    "exist list l" in {
      log("get list l=", ics.GetList("l"))
      log("exist l=", e.exist('l)) must_== true
    }

    "not exist var l" in {
      e.exist("l") must_== false

    }

    "not exist list a" in {
      e.exist('a) must_== false
    }

    "get some a" in {
      log("e.get(a).get=", e.get("a").get) must_== "xxx"
    }

    "get none" in {
      e.get("z") must_== None
    }

    "get some liste" in {
      log("l[f]=", e.get('l, "f").get) must_== "yyy"
    }

    "get none list" in {
      e.get('ll, "f") must_== None
    }

    "iterate " in {

      val l = for (i <- e.range('l)) yield { log(i); log(e('l, i, "f")) }
      l(0) must_== "yyy"
      l(1) must_== "2012-02-03 12:23:34"
      l(2) must_== "1234"

    }

    "get long and date " in {
      e.asLong("n") must_== Some(1234l)
      e.asDate("d") must_== Some(d1)
      e.asDate("n") must_== None
      e.asLong("d") must_== None
      e.asDate('l, 2, "f") must_== Some(d2)
      e.asLong('l, 3, "f") must_== Some(1234l)
      e.asLong('l, 2, "f") must_== None
      e.asDate('l, 3, "f") must_== None
    }

  }

}