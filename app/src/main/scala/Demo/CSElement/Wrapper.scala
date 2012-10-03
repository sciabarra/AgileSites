package Demo.CSElement

import wcs._
import wcs.tag._
import java.io.CharArrayWriter
import java.io.PrintWriter
import Demo.test._

class Wrapper extends Element with Log {

 
  def apply(e: Env) = {

    // excercise log
    info("INFO Hello, world!!!")
    debug("DEBUG Hello, world!!!")
    trace("TRACE Hello, world!!!")
    warn("WARN Hello, world!!!")

    // call a tag
    implicit val ics = e.ics
    
    import Listobject._
    create("l", "a,b")
    addrow("l", 'a := "1", 'b := "2")
    addrow("l", 'a := "3", 'b := "3")
    tolist("l", "l")

    /*
    val e = try {
      val l = x.list("l")
    } catch {
      case f: Throwable =>
        val caw = new CharArrayWriter
        val pw = new PrintWriter(caw)
        f.printStackTrace(pw)
        <h2>f</h2>
        <h3>{ caw.toString() }</h3>
    }*/

    try { 
      <h1>{ e("pagename") }</h1>
      <p>List ncol:{ ics.GetList("l").numColumns() }</p>
      <p>List nrows:{ ics.GetList("l").numRows() }</p>
      <p>List m is null:{ ics.GetList("m") == null }</p>
      <p>List l is NOT null:{ ics.GetList("l") == null }</p>
      <h1>Tests</h1>
      <h2>RunTagSpec</h2>
      <pre>{ new RunTagSpec(ics) }</pre>
      <h2>TagSpec</h2>
      <pre>{ new TagSpec(e) }</pre>
      <h2>IListSpec</h2>
      <pre>{ new IListSpec(e) }</pre>
    } catch {
      case e: Exception =>
        e.printStackTrace()
        <h1>Error: { e.getMessage }</h1>

    }

  }
}

