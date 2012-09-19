package app.CSElement

import wcs._
import wcs.tag._

class swWrapper extends Element {

  def apply(x: ICS) = {

    Listobject.create("ol", "a,b")()(x.ics)
    x.SetVar("a", "10")
    x.SetVar("b", "20")
    Listobject.addrow("ol")()(x.ics)
    x.SetVar("a", "30")
    x.SetVar("b", "40")
    Listobject.addrow("ol")()(x.ics)
    Listobject.tolist("ol", "l")()(x.ics)

    <h1>{ x("pagename").get }</h1>
    <p>List ncol:{x.GetList("l").numColumns()}</p>
    <p>List m is null:{x.GetList("m")==null}</p>
    <p>List l is NOT null:{x.GetList("l")==null}</p>
    
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

