package Demo.test
import wcs.scala.Env
import wcs.scala.Log
import scala.xml.NodeSeq

object AllTests extends Log {
  def apply(e: Env): NodeSeq = {

    /* exercise log
    info("INFO Hello, world!!!")
    debug("DEBUG Hello, world!!!")
    trace("TRACE Hello, world!!!")
    warn("WARN Hello, world!!!")
    */
    
    <h1>Tests</h1>
    <h2>RunTagSpec</h2>
    <pre>{ new RunTagSpec(e.ics) }</pre>
    <h2>TagSpec</h2>
    <pre>{ new TagSpec(e) }</pre>
    <h2>IListSpec</h2>
    <pre>{ new IListSpec(e) }</pre>
  }
}