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
      val picker = new Picker("/hello.html")

      nlog("picked:", picker.toString) must
        contain("<div id=\"title\">")
    }

    "select" in {
      val picker = new Picker("/hello.html")

      nlog("selected:", picker.select("#content").toString) must
        startWith("<div id=\"content\">")
    }

    "replace" in {
      val picker = new Picker("/hello.html", "#content")
      val s = picker.replace("p", "welcome.").toString

      //println(s)

      <div id="content">
        <p>welcome.</p>
      </div> must ==/(s2x(s))

    }

    //nlog("Before", menu)

    "single" in {

      val menu = new Picker("/hello.html", "#menu ul")
      val single = menu.single("li")

      <ul>
        <li>First</li>
      </ul> must ==/(single)

      //(s2x(log("After", menu.single("li").toString)))
    }

    "append/prepend" in {
      val single = new Picker("/hello.html", "#menu ul").single("li")
      val append = single.append("li", <li id="second">Second</li>.toString)

      <ul>
        <li>First</li>
        <li id="second">Second</li>
      </ul> must ==/(append)

      val prepend = append.prepend("#second", <li>One and Half</li>.toString)

      <ul>
        <li>First</li>
        <li>One and Half</li>
        <li id="second">Second</li>
      </ul> must ==/(prepend)
    }

  }
}
