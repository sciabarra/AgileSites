package app.CSElement

import wcs._
import wcs.tag._
import COM.FutureTense.Interfaces.FTValList

class swWrapper extends Element {

  def apply(x: ICS) = {

    Listobject.create(name = "ol", columns = "a,b")()(x.ics)

    val _args_ : FTValList = new FTValList()
    _args_.setValString("NAME", "ol")
    _args_.setValString("a", "10")
    _args_.setValString("b", "20")
    x.ics.runTag("LISTOBJECT.ADDROW", _args_);
    x.ics.runTag("LISTOBJECT.ADDROW", _args_);
    x.ics.runTag("LISTOBJECT.ADDROW", _args_);
    Listobject.tolist(name = "ol", listvarname = "l")()(x.ics)
    

    /*x.SetVar("a", "10")
    x.SetVar("b", "20")
    Listobject.addrow("ol")()(x.ics)
    
    x.SetVar("a", "30")
    x.SetVar("b", "40")
    Listobject.addrow("ol")()(x.ics)*/

    <h1>{ x("pagename").get }</h1>
    <p>List ncol:{ x.GetList("l").numColumns() }</p>
    <p>List nrows:{ x.GetList("l").numRows() }</p>
    <p>List m is null:{ x.GetList("m") == null }</p>
    <p>List l is NOT null:{ x.GetList("l") == null }</p>

    
    <table>
      {
        x.list("l").foreach { m =>
          <td>{ m("a") }</td>
          <td>{ m("b") }</td>
        }
      }
    </table>
    
  }
}

