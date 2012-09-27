package wcs

import wcs.core.{Element => CoreElement}

import scala.xml.NodeSeq

import COM.FutureTense.Interfaces.ICS;

abstract class Element extends CoreElement {

  implicit var ics: ICS = null

  def exec(_ics: ICS) = {
    ics = _ics
    apply(new X(_ics)).toString
  }
  def apply(x: X): NodeSeq
}