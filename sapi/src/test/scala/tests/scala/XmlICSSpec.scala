package tests.scala

import org.specs2.mutable._
import wcs.implx._
import wcs.scala._

class XmlICSSpec extends Specification with Log {

  val ics = new XmlICS()
  ics.addMapVar(Map("a" -> "1", "b" -> "2"))
  ics.addMapList("l1", Map("v" -> List("hello")))
  ics.addMapList("l3", Map("w" -> List("1", "2", "3")))
  ics.addMapList("l22", Map("x" -> List("10", "20"), "y" -> List("100", "200")))

  val x: Env = new Env(ics, "agilesites")

  override def is =
    "XmlICSSpec should" ^
      p ^
      "x.get(a).get=1" ! { x.get("a").get must_== "1" } ^
      "x.get(b).get=2" ! { x.get("b").get must_== "2" } ^
      "x(a)=1" ! { x("a") must_== "1" } ^
      "x.get(c)=None" ! { x.get("c") must_== None } ^
      "x(c)=''" ! { x("c") must_== "" } ^
      "x.list(l1) size=1" ! { x.list("l1").size must_== 1 } ^
      "x.list(l1) width=1" ! { x.list("l1")(0).keys.size must_== 1 } ^
      "x.list(l22) size=2" ! { x.list("l22").size must_== 2 } ^
      "x.list(l22) width=2" ! { x.list("l22")(0).keys.size must_== 2 } ^
      "x.list(l3).sum=6" ! { x.list("l3").map(_("w").toInt).sum must_== 6 } ^
      "x.list(l22)=[[10,100],[20,200]]" ! { x.list("l22") must_== List(Map("x" -> "10", "y" -> "100"), Map("x" -> "20", "y" -> "200")) }

}