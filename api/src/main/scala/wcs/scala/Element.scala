package wcs.scala

import wcs.scala._
import wcs.java.{ Element => JElement }
import scala.xml.NodeSeq
import COM.FutureTense.Interfaces.ICS
import scala.xml.NodeBuffer

/**
 * The element implementation
 */
abstract class Element extends JElement with Log {

  // convert a NodeSeq in a string
  implicit def nodeSeq2String(buf: NodeSeq): String =
    (for (node <- buf.iterator) yield node.toString).mkString

  implicit def nodeBuffer2String(buf: NodeBuffer): String =
    (for (node <- buf.iterator) yield node.toString).mkString

  var c = ""
  var cid = 0L

  /**
   * Execute the element - return null if all ok (nothing to be steamed)
   * Otherwise return the error message to be streamed in the output
   *
   */
  override def exec(ics: ICS): String = {
    try {
      
      // initialize the element
      c = ics.GetVar("c")
      cid = try { ics.GetVar("cid").toLong } catch { case _ => 0l }
      site = ics.GetVar("site")

      // the most important line of code of the whole framework
      stream(ics, apply(new Env(ics)));
      
      // nulls mean all ok
      return null;
      
    } catch {
      // return the exception
      case ex =>
        ex.printStackTrace();
        ex.getMessage();
    }
  }

  /**
   * Call another element
   */
  def call(name: String, args: Tuple2[Symbol, String]*) = {
    import wcs.java.util.Util.Arg
    val seq = for ((k, v) <- args) yield { new Arg(k.name, v) }
    wcs.java.Element.scheduleCall(site + "/" + name, seq: _*)
  }

  /**
   * Generate an url for an asset specified by id
   */
  def url(id: Tuple2[String, Long]) = {

    // TODO

  }

  /**
   * Apply the element - actual implementation required
   */
  def apply(e: wcs.scala.Env): String

  /**
   * This method must not be implemented - the apply(e: wcs.scala.Env) should instead
   */
  def apply(e: wcs.java.Env): String = null

}