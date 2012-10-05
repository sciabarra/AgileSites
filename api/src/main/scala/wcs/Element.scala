package wcs

import scala.xml.NodeSeq
import wcs.core.{ Element => CoreElement }
import wcs.util.Future
import wcs.util.FutureCallElement
import wcs.util.FutureElem
import java.io.PrintWriter
import COM.FutureTense.Interfaces.ICS

abstract class Element extends CoreElement with Log {

  def element(name: String, args: Tuple2[Symbol, String]*) = {
    new FutureCallElement(name, args: _*)
  }			

  def exec(ics: ICS) = try {

    for (fut <- apply(new Env(ics))) {
      debug(fut(ics))
    }
    ""

  } catch {
    case t: Throwable =>

      // message
      val msg = <h1>{ t.getMessage }</h1>
                <pre>{ stacktrace(t) }</pre>.toString

      ics.StreamText(msg)

      ""

    case e @ _ =>
      val msg = <pre>{ e.toString }</pre>.toString
      ics.StreamText(msg)
      ""
  }

  def apply(e: Env): Seq[Future]
}