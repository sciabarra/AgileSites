import scala.xml.NodeSeq
import wcs.util.FutureElem
import wcs.util.Future
package object wcs {

  // parameters are 'a := val
  class ArgAssoc(x: Symbol) {
    def :=(y: String): Tuple2[Symbol, String] = Tuple2(x, y)
  }

  // creating the param assoc
  implicit def sym2ArgAssoc(x: Symbol) = new ArgAssoc(x)
  
   // convert a string in a future when required
  implicit def string2Future(s: String) : Future = new wcs.util.FutureString(s)

  // convert a nodeseq in a future
  implicit def elem2Future(el: scala.xml.Elem): Future = new FutureElem(el)


  // print a stacktrace
  def stacktrace(t: Throwable) = {
    val caw = new java.io.CharArrayWriter
    val pw = new java.io.PrintWriter(caw)
    t.printStackTrace(pw)
    caw.toString
  }

}