package app.CSElement

import test._
import wcs._
import wcs.tag._

class swWrapper extends Element {

  def apply(x: ICS) = {
    implicit val ics = x.ics
    import Listobject._
    
    create("l", "a,b")
    addrow("l", 'a := "1", 'b := "2")
    addrow("l", 'a := "3", 'b := "3")
    tolist("l", "l")
    
    //val l = x.list("l")

    try {

      <h1>{ x("pagename") }</h1>
      <p>List ncol:{ x.GetList("l").numColumns() }</p>
      <p>List nrows:{ x.GetList("l").numRows() }</p>
      <p>List m is null:{ x.GetList("m") == null }</p>
      <p>List l is NOT null:{ x.GetList("l") == null }</p>
      <h1>{ x("pagename") }</h1>
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

