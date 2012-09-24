package app.Template.Page

import wcs._

class swBody extends Element {

  def apply(ics: ICS) =
    <p>Hello</p>
    /*
    <p>{ v("title") }</p>
    <ul>{
      attribute("title") { v =>
        <li>{ v }</li>
      }
    }</ul>
    */

}