package scalademo

class Config extends wcs.java.Config {

  override def getSite = "ScalaDemo"

  override def getAttributeType(c: String) =
    if (c == null) null
    else if (c == "Page") "PageAttribute"
    else null

}