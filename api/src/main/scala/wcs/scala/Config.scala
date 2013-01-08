package wcs.scala
import COM.FutureTense.Interfaces.ICS

abstract class Config extends wcs.java.Config {

  override def getAttributeType(typ: String) = attributeType(typ)

  def attributeType(s: String): String

  override def getDefaultTemplate(typ: String) = defaultTemplate(typ)

  def defaultTemplate(typ: String): String

}