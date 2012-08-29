package wcs

import scala.xml.NodeSeq
import COM.FutureTense.Interfaces.{ICS => JICS };

abstract class Element extends wcs.boot.Element {
  
  def exec(jics: JICS) = apply(new ICS(jics)).toString

  def apply(ics: ICS): NodeSeq
}