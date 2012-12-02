package wcs.scala
import wcs.java.{ Setup => JSetup }
import wcs.java.{ Site => JSite }
import wcs.java.{ CSElement => JCSElement }
import wcs.java.{ Template => JTemplate }
import wcs.java.{ SiteEntry => JSiteEntry }

/**
 * Setup wrapper in Scala
 */
abstract class Setup extends JSetup {

  class Asset

  // future use
  //case class Site(id: Long, name: String, description: String, types: Array[String], users: Array[String], roles: Array[String]) extends Asset

  case class Site(name: String)

  case class Template(name: String, element: Class[_ <: wcs.core.Element], subtype: String = "", description: String = "", cscache: String = "false", sscache: String = "false") extends Asset

  case class CSElement(name: String, element: Class[_ <: wcs.core.Element], description: String = "") extends Asset

  case class SiteEntry(name: String, wrapper: Boolean, elementname: String, element: Class[_ <: wcs.core.Element], description: String = "") extends Asset

  // abstract functions to be overriden

  def site: Site

  def assets: List[Asset]

  // future use
  //override def getSite = new JSite(site.id, site.name, site.description, site.types, site.users, site.roles)
  override def getSite = new JSite(site.name)

  override def getAssets = Array[wcs.java.Asset](
    assets map { a =>
      a match {
        case x: CSElement =>
          new JCSElement(x.name, x.element.getCanonicalName()).description(x.description)
        case x: Template =>
          new JTemplate(x.subtype, x.name, x.element.getCanonicalName()).cache(x.cscache, x.sscache).description(x.description)
        case x: SiteEntry =>
          new JSiteEntry(x.name, x.wrapper, x.elementname, x.element.getCanonicalName())
      }
    }: _*)

}