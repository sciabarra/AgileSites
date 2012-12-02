package wcs.scala

import wcs.scala._
import wcs.java.{ Element => JElement }
import scala.xml.NodeSeq
import COM.FutureTense.Interfaces.ICS
import scala.xml.NodeBuffer

abstract class Element extends JElement with Log {

  // convert a NodeSeq in a string
  implicit def nodeSeq2String(buf: NodeSeq): String =
    (for (node <- buf.elements) yield node.toString).mkString

  implicit def nodeBuffer2String(buf: NodeBuffer): String =
    (for (node <- buf.elements) yield node.toString).mkString

  /**
   * Execute the element
   *
   */
  override def exec(ics: ICS): String = {

    try {
      val env = new Env(ics);
      val res = apply(env);
      // TODO split stream
      ics.StreamText(res);
      return null;
    } catch {
      case ex =>
        ex.printStackTrace();
        ex.getMessage();
    }
  }

  /**
   * This method must not be implemented - the apply(e: wcs.scala.Env) should instead
   */
  def apply(e: wcs.java.Env): String = null

  /**
   * Call another element
   */
  def call(name: String, args: Tuple2[Symbol, String]*) = {
    // TODO
  }

  /**
   * Generate an url for an asset specified by id
   */
  def url(id: Tuple2[String, Long]) = {

    // TODO

  }

  /**
   * Apply the element
   */
  def apply(e: wcs.scala.Env): String
}