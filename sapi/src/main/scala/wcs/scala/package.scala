import scala.xml.Elem
import java.io.CharArrayWriter
import java.io.PrintWriter
import scala.xml.Elem

package wcs {

  package object scala {

    // print a stacktrace
    def stacktrace(t: Throwable) = {
      val caw = new CharArrayWriter
      val pw = new PrintWriter(caw)
      t.printStackTrace(pw)
      caw.toString
    }

    implicit def eleme2string(elem: Elem) = elem.toString

  }
}