package Demo.CSElement

import wcs.scala._
import wcs.tag._

import java.io.CharArrayWriter
import java.io.PrintWriter
import Demo.test._

class Wrapper extends Element with Log {

  def apply(e: Env) = Seq(
    <h1>Hello</h1>,
    "<p><b>",
    call("HelloWorld"),
    "</b></p>").toString

}

