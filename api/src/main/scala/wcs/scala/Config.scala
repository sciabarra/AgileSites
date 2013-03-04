package wcs.scala
import COM.FutureTense.Interfaces.ICS

abstract class Config extends wcs.java.Config {

  override def getAttributeType(typ: String) = attributeType(typ)

  override def getSite() = site()

  def attributeType(s: String): String

  def site(): String

}