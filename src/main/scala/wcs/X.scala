package wcs

import scala.collection.immutable

import wcs.ICSProxy
import java.io.OutputStream
import java.security.Principal
import java.util.Collection
import java.util.Enumeration
import java.util.Map
import java.util.Vector


/**
 * Scala API on top of ICS
 */
class X extends ICSProxy {
  /**
   * the value or a null string if none
   */
  def apply(s: String) = get(s).getOrElse("")

  /**
   * Return Some variable value if the variable is available or None if there is no such variable
   */

  def get(s: String) = {
    val v = ics.GetVar(s)
    if (v == null)
      None
    else
      Some(v)
  }

  val voidList = List[immutable.Map[String, String]]()

  /**
   * Return WCS lists as  Seq of Maps
   */
  def list(s: String): Seq[immutable.Map[String, String]] = {

    val ls = ics.GetList(s)
    if (ls == null)
      return voidList

    //log.trace("numRows=%d", ls.numRows)
    //log.trace("numColumns=%d", ls.numColumns)

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
