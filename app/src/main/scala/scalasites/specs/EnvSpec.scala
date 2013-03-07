package scalasites.specs

import wcs.scala._
import wcs.scala.tag._
import wcs.scala.util.Specification
import wcs.java.util.Util

class EnvSpec(e: Env) extends Specification {

  implicit val i = e.ics

  e("c") = "Page"
  e("cid") = "1"
  e("d") = "2001-01-01 01:02:03"

  import ListobjectTag._
  create("l", "a,b,c")
  addrow("l", 'a := "1", 'b := "2", 'c := "3")
  addrow("l", 'a := "11", 'b := "22", 'c := "33")
  addrow("l", 'a := "111", 'b := "222", 'c := "333")
  tolist("l", "l")

  /*
  import ListobjectTag._
  create("ll", "a,b")
  addrow("ll", 'a := "1", 'b := "2")
  addrow("ll", 'a := "11", 'b := "22")
  tolist("ll", "ll")
  */

  e('ll) = Map('a := "1", 'b := "2") ::
    Map('a := "11", 'b := "22") ::
    Nil

  e('nl) = Nil

  //i.SetVar("c", "Page");
  //i.SetVar("cid", "1");
  //i.SetVar("d", );

  "Env should" in {

    "read expected values" in {
      e("c") must_== "Page"
      e.asDate("d").orNull must_== Util.toDate("2001-01-01 01:02:03")

    }

    "return c/cid" in {
      e.c must_== "Page"
      e.cid must_== 1
    }

    "read the expected list" in {
      e.range('l).size must_== 3

      e('l, "a") must_== "1"
      e('l, "b") must_== "2"
      e('l, "c") must_== "3"

      e('l, 1, "a") must_== "1"
      e('l, 1, "b") must_== "2"
      e('l, 1, "c") must_== "3"

      e('l, 2, "a") must_== "11"
      e('l, 2, "b") must_== "22"
      e('l, 2, "c") must_== "33"

      e('l, 3, "a") must_== "111"
      e('l, 3, "b") must_== "222"
      e('l, 3, "c") must_== "333"

    }

    "loop list" in {

      for (i <- e.range('l)) {
        if (i == 1) {
          e('ll, i, "a") must_== "1"
          e('ll, i, "b") must_== "2"

        }
        if (i == 2) {
          e('ll, i, "a") must_== "11"
          e('ll, i, "b") must_== "22"
        }
      }

      "map list" in {
        val ls = (for (i <- e.range('l)) yield {
          e('ll, i, "a") :: e('ll, i, "b") :: Nil
        }) flatMap { x => x }

        ls(0) must_== "1"
        ls(1) must_== "2"
        ls(2) must_== "11"
        ls(3) must_== "22"
      }

      "null list" in {
        e('nl).size must_== 0
        e('l, 0, "x") must_== ""
        e.get('l, 0, "x") must_== None
        e.get('l, 1, "x") must_== None
      }
    }
  }

}