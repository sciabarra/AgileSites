package wcs

import scala.collection.immutable

import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.IList
import wcs.core.ICSProxy

/**
 * Scala API on top of ICS
 */
class Env(_ics: ICS) extends ICSProxy {

  init(_ics)

  /**
   * the value or a null string if none
   */
  def apply(s: String) = get(s).getOrElse("")

  /**
   * Return Some variable value if the variable is available or None if there is no such variable
   */
  def get(s: String): Option[String] = {
    val v = _ics.GetVar(s)
    if (v == null)
      None
    else
      Some(v)
  }

  /**
   * Return a ilists as  Seq of Maps
   */
  def list(s: String): Seq[immutable.Map[String, String]] = {
    val voidList = List[immutable.Map[String, String]]()

    val ls = _ics.GetList(s)
    if (ls == null)
      return voidList

    val l = for (i <- 1 to ls.numRows) yield {
      ls.moveTo(i)
      val ll = for (j <- 0 to ls.numColumns - 1) yield {
        val name = ls.getColumnName(j)
        val value = ls.getValue(name)
        name -> value
      }
      ll.toMap
    }
    l.toSeq
  }

}
