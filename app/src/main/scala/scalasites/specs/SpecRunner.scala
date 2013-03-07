package scalasites.specs

import wcs.scala._
import scala.xml.NodeSeq

class SpecRunner extends Element {

  def apply(e: Env): String = {

    /* exercise log
    info("INFO Hello, world!!!")
    debug("DEBUG Hello, world!!!")
    trace("TRACE Hello, world!!!")
    warn("WARN Hello, world!!!")*/

    try {

      <h1>Tests</h1>
      <h2>RunTagSpec</h2>
      <pre>{ new RunTagSpec(e.ics) }</pre>
      <h2>TagSpec</h2>
      <pre>{ new TagSpec(e) }</pre>
      <h2>IListSpec</h2>
      <pre>{ new IListSpec(e) }</pre>
      <h2>EnvSpec</h2>
      <pre>{ new EnvSpec(e) }</pre>

    } catch {
      case e: Throwable => <h1>Exception: { e.getMessage }</h1>
    }
  }
}