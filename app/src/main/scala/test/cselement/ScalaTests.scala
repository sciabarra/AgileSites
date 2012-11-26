package test.cselement

import wcs.scala._
import scala.xml.NodeSeq
import test.cselement.tests.RunTagSpec
import test.cselement.tests.TagSpec
import test.cselement.tests.IListSpec

class ScalaTests extends Element {

  def apply(e: Env): String = {

    /* exercise log
    info("INFO Hello, world!!!")
    debug("DEBUG Hello, world!!!")
    trace("TRACE Hello, world!!!")
    warn("WARN Hello, world!!!")
    

    <h1>Tests</h1>
    <h2>RunTagSpec</h2>
    <pre>{ new RunTagSpec(e.ics) }</pre>
    <h2>TagSpec</h2>
    <pre>{ new TagSpec(e) }</pre>
    <h2>IListSpec</h2>
    <pre>{ new IListSpec(e) }</pre>.toString
    */
    
    <h1>Hello World, from Scala</h1>.toString

  }
}