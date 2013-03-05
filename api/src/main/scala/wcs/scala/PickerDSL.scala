package wcs.scala

import wcs.java.{ Picker => JPicker }
import scala.util.DynamicVariable

trait PickerDSL {

  private val currentPicker = new ThreadLocal[List[JPicker]]() {
    override def initialValue() = { Nil }
  }

  implicit def string2replacerAttr(t2: Tuple2[String, String]): ReplacerAttr = {
    new ReplacerAttr(t2)
  }

  implicit def string2replacer(where: String): Replacer = {
    new Replacer(where)
  }

  /**
   * Pick a resource with the given name then perform the action.
   * Return the resource with the replacements specified in the action.
   *
   */
  def pick(where: String)(action: => Any): String = {

    val picker = new JPicker(where)
    currentPicker.set(picker :: currentPicker.get())
    action
    currentPicker.set(currentPicker.get().tail)
    picker.html()

  }

  /**
   * Pick a resource with the given name, restrict to a subset, then perform the action
   * Return the resource with the replacements specified in the action.
   */
  def pick(where: String, what: String)(action: => Any): String = {
    val picker = new JPicker(where, what)
    currentPicker.set(picker :: currentPicker.get())
    action
    currentPicker.set(currentPicker.get().tail)
    picker.html()
  }

  /**
   * Focus on a specific part of the current picked html
   */
  def select(where: String)(action: => Unit) {
    currentPicker.get()(0).select(where)
    action
    currentPicker.get()(0).up()
  }

  /**
   * Perform a replacement
   */
  class Replacer(val where: String) {

    /**
     * Replace
     */
    def %=(what: String) {
      currentPicker.get()(0).replace(where, what)
    }

    /**
     * Single/Replace
     */
    def %!=(what: String) {
      currentPicker.get()(0).single(where).replace(where, what)
    }

    /**
     * Append
     */

    def %>(what: String) {
      currentPicker.get()(0).after(where, what)
    }

    /**
     * Single/Append
     */

    def %!>(what: String) {
      currentPicker.get()(0).single(where).after(where, what)
    }

    /**
     * Prepend
     */
    def %<(what: String) {
      currentPicker.get()(0).before(where, what)
    }

    /**
     * Single/Prepend
     */
    def %!<(what: String) {
      currentPicker.get()(0).single(where).before(where, what)
    }

    /**
     * Single
     */
    def %!() {
      currentPicker.get()(0).single(where)
    }

    /**
     * Remove
     */
    def %-() {
      currentPicker.get()(0).remove(where)
    }

    /**
     * Add Class
     */
    def %+(what: String) {
      currentPicker.get()(0).addClass(where, what)
    }

    def %=(elem: scala.xml.Elem): Unit = { %=(elem.toString) }
    def %!=(elem: scala.xml.Elem): Unit = { %!=(elem.toString) }
    def %>(elem: scala.xml.Elem): Unit = { %>(elem.toString) }
    def %!>(elem: scala.xml.Elem): Unit = { %!>(elem.toString) }
    def %<(elem: scala.xml.Elem): Unit = { %<(elem.toString) }
    def %!<(elem: scala.xml.Elem): Unit = { %!<(elem.toString) }
    def %+(elem: scala.xml.Elem): Unit = { %+(elem.toString) }

  }

  class ReplacerAttr(t2: Tuple2[String, String]) {
    /**
     * Replace attribute
     */
    def %=(what: String) {
      currentPicker.get()(0).attr(t2._1, t2._2, what)
    }
  }

}