package wcs.scala.util

import COM.FutureTense.Interfaces.ICS
import scala.xml.NodeSeq
import COM.FutureTense.Interfaces.FTValList
import scala.xml.Elem

trait Future {
  def apply(ics: ICS): String
}

class FutureString(s: String) extends Future {
  def apply(ics: ICS) = {
    ics.StreamText(s)
    s
  }
}

class FutureElem(val el: Elem) extends Future {
  def apply(ics: ICS): String = {
    val s = el.toString
    ics.StreamText(s)
    s
  }
}

class FutureCallElement(element: String, args: Tuple2[Symbol, String]*) extends Future {

  val vlist: FTValList = new FTValList()
  args.foreach {
    x => vlist.setValString(x._1.toString.substring(1), x._2)
  }

  def apply(ics: ICS) = {
    ics.CallElement(element, vlist)
    "call " + element
  }

}