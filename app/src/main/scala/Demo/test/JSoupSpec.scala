package demo.test

import wcs.scala.Env
import wcs.scala.util.Specification
import org.jsoup._
import wcs.scala.Log

class JSoupSpec extends Specification {
}

object JSoupSpec extends Log {
  def apply(e: Env) {
    
    trace("saluti, world")
    
    val is = getClass.getResourceAsStream("/index.html")
    Jsoup.parse(is, null, "/cs")
  }
}
