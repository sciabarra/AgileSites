package Demo.view.CSElement

import wcs._
import wcs.tag._
import Demo.test._
import java.io.CharArrayWriter
import java.io.PrintWriter

class Wrapper extends Element with Log {

 
  def apply(x: X) = {

    // excercise log
    info("INFO Hello, world!!!")
    debug("DEBUG Hello, world!!!")
    trace("TRACE Hello, world!!!")
    warn("WARN Hello, world!!!")

    // call a tag
    implicit val ics = x.ics
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
      <h1>{ x("pagename") }</h1>
      <p>List ncol:{ x.GetList("l").numColumns() }</p>
      <p>List nrows:{ x.GetList("l").numRows() }</p>
      <p>List m is null:{ x.GetList("m") == null }</p>
      <p>List l is NOT null:{ x.GetList("l") == null }</p>
      <h1>Tests</h1>
      <h2>RunTagSpec</h2>
      <pre>{ new RunTagSpec(ics) }</pre>
      <h2>TagSpec</h2>
      <pre>{ new TagSpec(x) }</pre>
      <h2>IListSpec</h2>
      <pre>{ new IListSpec(x) }</pre>
    } catch {
      case e: Exception =>
        e.printStackTrace()
        <h1>Error: { e.getMessage }</h1>

    }

  }
}

