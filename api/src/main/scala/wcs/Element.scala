package wcs

import COM.FutureTense.Interfaces.ICS
import scala.xml.NodeSeq
import wcs.core.{ Element => CoreElement }
import java.io.PrintWriter

abstract class Element extends CoreElement  with Log {

  def exec(ics: ICS) = try {
    apply(new Env(ics)) match {
      case a: Seq[Any] =>
        a map { _.toString } mkString ""
      case x @ _ =>
        debug("here: "+x.getClass)
        x.toString
    }
  } catch {
    case t: Throwable =>

      // extract the stacktrace
      val caw = new java.io.CharArrayWriter
      val pw = new java.io.PrintWriter(caw)
      t.printStackTrace(pw)

      // message

      <h1>{ t.getMessage }</h1>
      <pre>{ caw.toString() }</pre>.toString

    case e @ _ =>
      <pre>{ e.toString }</pre>.toString
  }

  def apply(e: Env): Any
}