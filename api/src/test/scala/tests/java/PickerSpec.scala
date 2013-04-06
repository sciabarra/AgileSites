package tests.java

import org.specs2.mutable.Specification
import wcs.java.Picker
import scala.xml.XML
import scala.xml.Elem
import wcs.scala.Log

class PickerSpec extends Specification with Log {

  implicit def s2x(s: String): Elem = XML.loadString(s)

  implicit def p2x(p: Picker): scala.xml.Elem = XML.loadString(p.toString)

  def cl(s: String) =
    s.replaceAll("[ \t\n\r]+", " ")

  "Picker should" in {

    //  println(picker.toString)

    "pick" in {
      val picker = Picker.load("/hello.html")

      nlog("picked:", picker.toString) must
        contain("<div id=\"title\">")
    }

    "select" in {
      val picker = Picker.load("/hello.html")

      nlog("selected:", picker.select("#content").toString) must
        startWith("<div id=\"content\">")
    }

    "replace" in {
      val picker = Picker.load("/hello.html", "#content")
      val s = picker.replace("p", "welcome.").toString

      //println(s)

      <div id="content">
        <p>welcome.</p>
      </div> must ==/(s2x(s))

    }

    //nlog("Before", menu)

    "single" in {

      val menu = Picker.load("/hello.html", "#menu ul")
      val single = menu.single("li")

      <ul>
        <li>First</li>
      </ul> must ==/(single)

      //(s2x(log("After", menu.single("li").toString)))
    }

    "before/after" in {
      val single = Picker.load("/hello.html", "#menu ul").single("li")
      val after = single.after("li", <li id="second">Second</li>.toString)

      <ul>
        <li>First</li>
        <li id="second">Second</li>
      </ul> must ==/(after)

      val before = after.before("#second", <li>One and Half</li>.toString)

      <ul>
        <li>First</li>
        <li>One and Half</li>
        <li id="second">Second</li>
      </ul> must ==/(before)
    }

    "class/attr" in {
      val title = Picker.load("/hello.html", "#title")

      val tit = title.attr("#title", "id", "replaced")
        .addClass("h1", "demo")
        .attr("h1", "test", "demo")

      <div id="replaced">
        <h1 test="demo" class=" demo">Hello</h1>
      </div> must ==/(title)
    }

  }
}
