package wcs.scala
import wcs.java.{ Setup => JSetup }
import wcs.java.{ Site => JSite }
import wcs.java.{ CSElement => JCSElement }
import wcs.java.{ Template => JTemplate }
import wcs.java.{ SiteEntry => JSiteEntry }
import wcs.java.{ AttrTypes => JAttrTypes }
import scala.xml.NodeSeq

/**
 * Setup wrapper in Scala
 */
abstract class Setup extends JSetup {

  // future use
  //case class Site(id: Long, name: String, description: String, types: Array[String], users: Array[String], roles: Array[String]) extends Asset

  class Asset

  object TemplateType extends Enumeration {
    type TemplateType = Value
    val UNSPECIFIED = Value('\0');
    val INTERNAL = Value('b');
    val STREAMED = Value('r');
    val EXTERNAL = Value('x');
    val LAYOUT = Value('l');
  }

  case class Site(name: String)

  case class Template(subtype: String, name: String, ttype: TemplateType.TemplateType, elementClass: Class[_ <: wcs.core.Element], description: String = null, cscache: String = "false", sscache: String = "false") extends Asset

  case class CSElement(name: String, elementClass: Class[_ <: wcs.core.Element], element: String = null, description: String = null) extends Asset

  case class SiteEntry(name: String, wrapper: Boolean = true, element: String = null, description: String = null) extends Asset

  case class AttrTypes(name: String, xml: NodeSeq) extends Asset

  // abstract functions to be overriden

  def site: Site

  def assets: List[Asset]

  override def getAssets = Array[wcs.java.Asset](
    assets map { a =>
      a match {
        case x: CSElement =>
          new JCSElement(x.name, x.elementClass, x.element).description(x.description)
        case x: Template =>
          new JTemplate(x.subtype, x.name, x.ttype.id.asInstanceOf[Char], x.elementClass).cache(x.cscache, x.sscache).description(x.description)
        case x: SiteEntry =>
          new JSiteEntry(x.name, x.wrapper, x.element)
        case x: AttrTypes =>
          new JAttrTypes(x.name, x.xml.toString)
      }
    }: _*)

}