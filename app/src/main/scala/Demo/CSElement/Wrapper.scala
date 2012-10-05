package Demo.CSElement

import wcs._
import wcs.tag._

import java.io.CharArrayWriter
import java.io.PrintWriter
import Demo.test._

class Wrapper extends Element with Log {

  def apply(e: Env) = Seq(
    <h1>Hello</h1>,
    "<p><b>",
    element("HelloWorld"),
    "</b></p>")

}

