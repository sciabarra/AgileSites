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

  case class Site(id: Long, name: String, description: String, types: Array[String], users: Array[String], roles: Array[String])

  case class CSElement(id: Long, name: String, description: String, element: String)

  case class Template(id: Long, name: String, description: String, element: String, cscache: String = "true, ~0", sscache: String = "true,~0")

  case class SiteEntry(id: Long, name: String, description: String, element: String, wrapper: Boolean = false)

  //override def getSite = new wcs.java.Site(site.id, site.name, site.description, site.types, site, users, site.roles)

  //	public Site(final Long id, final String name, final String description, final String[] types, final String[] users, final String[] roles) 

  override def getCSElements = Array[wcs.java.CSElement](
    csElements map { x => new JCSElement(x.id, x.name, x.description, x.element) }: _*)

  override def getTemplates = Array[JTemplate](
    templates map { x => new JTemplate(x.id, x.name, x.description, x.element, x.cscache, x.sscache) }: _*)

  override def getSiteEntries = Array[JSiteEntry](
    siteEntries map { x => new JSiteEntry(x.id, x.name, x.description, x.element, x.wrapper) }: _*)

  override def getSite = new JSite(site.id, site.name, site.description, site.types, site.users, site.roles)

  def site: Site

  def csElements: List[CSElement]

  def templates: List[Template]

  def siteEntries: List[SiteEntry]

}