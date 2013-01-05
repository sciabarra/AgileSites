package wcs.scala

abstract class Config extends wcs.java.Config {
  
  override def getAttributeType(s: String) = attributeType(s)
  
  def attributeType(s: String): String

}