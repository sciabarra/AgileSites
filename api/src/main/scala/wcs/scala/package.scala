import scala.xml.NodeSeq
import wcs.scala.util.FutureElem
import wcs.scala.util.Future
import java.io.CharArrayWriter
import java.io.PrintWriter
import scala.xml.Elem

package wcs {

  package object scala {

    // convert a string in a future when required
    implicit def string2Future(s: String): Future = new wcs.scala.util.FutureString(s)

    // convert a nodeseq in a future
    implicit def elem2Future(el: Elem): Future = new FutureElem(el)

    // print a stacktrace
    def stacktrace(t: Throwable) = {
      val caw = new CharArrayWriter
      val pw = new PrintWriter(caw)
      t.printStackTrace(pw)
      caw.toString
    }

  }
}