package wcs.scala
import wcs.java.{ SetupBase => JSetupBase }
import wcs.java.{ CSElement => JCSElement }
import wcs.java.{ Template => JTemplate }
import wcs.java.{ SiteEntry => JSiteEntry }

/**
 * Setup wrapper in Scala
 */
abstract class SetupBase extends JSetupBase {

  case class CSElement(name: String, element: String)

  case class Template(name: String, element: String, cscache: String = "true, ~0", sscache: String = "true,~0")

  case class SiteEntry(name: String, element: String, wrapper: Boolean = false)

  override def getCSElements = Array[wcs.java.CSElement](
    csElements map { x => new wcs.java.CSElement(x.name, x.element) }: _*)

  override def getTemplates = Array[JTemplate](
    templates map { x => new JTemplate(x.name, x.element, x.cscache, x.sscache) }: _*)

  override def getSiteEntries = Array[JSiteEntry](
    siteEntries map { x => new JSiteEntry(x.name, x.element, x.wrapper) }: _*)

  def csElements: List[CSElement]

  def templates: List[Template]

  def siteEntries: List[SiteEntry]

}