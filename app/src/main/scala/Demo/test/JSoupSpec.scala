package Demo.test

import wcs.util.Specification
import wcs.Env
import org.jsoup._
import wcs.Log

class JSoupSpec extends Specification {
}

object JSoupSpec extends Log {
  def apply(e: Env) {
    
    trace("saluti, world")
    
    val is = getClass.getResourceAsStream("/index.html")
    Jsoup.parse(is, null, "/cs")
  }
}
