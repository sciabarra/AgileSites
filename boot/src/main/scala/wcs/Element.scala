package wcs

import scala.xml.NodeSeq
import wcs.boot.{Element => JElement };
import COM.FutureTense.Interfaces.{ICS => JICS };

abstract class Element extends JElement {
  
  def exec(jics: JICS) = apply(new ICS(jics)).toString

  def apply(ics: ICS): NodeSeq
}