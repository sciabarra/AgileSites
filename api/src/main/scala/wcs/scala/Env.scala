package wcs.scala

import scala.collection.immutable

import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.IList
import wcs.java.{ Env => JEnv }

/**
 * Scala API on top of ICS
 */

class Env(_ics: ICS) extends JEnv(_ics) {

  /**
   * the value or a empty string if none
   */
  def apply(variable: String) = get(variable).getOrElse("")

  /**
   * the value or an empty string if note string if none
   */
  def apply(list: String, field: String) = get(list, field).getOrElse("")

  /**
   * the value or an empty string if none
   */
  def apply(list: String, field: String, pos: Int) = get(list, field, pos).getOrElse("")

  /**
   * Check if a variable exists
   */
  def exist(variable: String) = getString(variable) == null

  /**
   * Check if list field exists
   */
  def exist(list: String, field: String) = getString(list, field) == null

  /**
   * Return the optional value of the variable
   */
  def get(variable: String): Option[String] = {
    val v = getString(variable)
    if (v == null)
      None
    else
      Some(v)
  }

  /**
   * Return the optional value of the variable
   */
  def get(list: String, field: String): Option[String] = {
    val v = getString(list, field)
    if (v == null)
      None
    else
      Some(v)
  }

  /**
   * Return the optional value of the variable
   */
  def get(list: String, field: String, pos: Int): Option[String] = {
    val v = getString(list, field, pos)
    if (v == null)
      None
    else
      Some(v)
  }

  /**
   * Return Some(object)
   */
  def get(s: Symbol) = {
    val o = ics.GetObj(s.toString)
    if (o == null)
      None
    else
      Some(o)
  }

  /**
   * set a variable
   */
  def update(s: String, v: String) {
    ics.SetVar(s, v)
  }
  
  /**
   * Range of a list
   */
  def range(list: String) = {
    val l = ics.GetList(list)
    if(l==null)
      0 to -1
     else
       1 to l.numRows()
  }

  /**
   * Return a ilists as Seq of Maps
   */
  def list(list: String): Seq[Map[String, String]] = {

    val ls = ics.GetList(list)
    if (ls == null)
      List[Map[String, String]]()
    else {
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

}
