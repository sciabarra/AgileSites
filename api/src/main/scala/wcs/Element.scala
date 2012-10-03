package wcs

import COM.FutureTense.Interfaces.ICS;
import scala.xml.NodeSeq
import wcs.core.{ Element => CoreElement }

abstract class Element extends CoreElement {

  def exec(ics: ICS) = {
    apply(new Env(ics)).toString
  }

  def apply(e: Env): NodeSeq
}