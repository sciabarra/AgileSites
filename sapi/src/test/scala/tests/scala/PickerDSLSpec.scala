package tests.scala

import org.specs2.mutable.Specification
import wcs.scala.PickerDSL
import scala.xml.XML
import scala.xml.Elem
import wcs.scala.Log

class PickerDSLSpec extends Specification with Log with PickerDSL {
  
  implicit def s2x(s: String): Elem = XML.loadString(s)

  implicit def p2x(p: wcs.java.Picker): scala.xml.Elem = XML.loadString(p.toString)

  def cl(s: String) =
    s.replaceAll("[ \t\n\r]+", " ")

  "Picker should" in {

    //  println(picker.toString)

    "pick" in {
      val picker = pick("/hello.html") {  }

      nlog("picked:", picker.toString) must
        contain("<div id=\"title\">")
    }

    "select" in {
      val picker = pick("/hello.html", "#content") { }

      nlog("selected:", picker) must
        startWith("<div id=\"content\">")
    }

    "replace" in {
      val s =
        pick("/hello.html", "#content") {
          "p" %= "welcome."
        }

      <div id="content">
        <p>welcome.</p>
      </div> must ==/(s2x(s))

    }

    //nlog("Before", menu)

    "single" in {

      val single =
        pick("/hello.html", "#menu ul") {
          "li" %!
        }

      <ul>
        <li>First</li>
      </ul> must ==/(s2x(single))

      //(s2x(log("After", menu.single("li").toString)))
    }

    "append/prepend" in {
      val append = pick("/hello.html", "#menu ul") {

        "li" %!> <li id="second">Second</li>
      }

      <ul>
        <li>First</li>
        <li id="second">Second</li>
      </ul> must ==/(s2x(append))

      val prepend = pick("/hello.html", "#menu ul") {

        "li" %!> <li id="second">Second</li>

        "#second" %< <li>One and Half</li>
      }

      <ul>
        <li>First</li>
        <li>One and Half</li>
        <li id="second">Second</li>
      </ul> must ==/(s2x(prepend))
    }

    "class/attr" in {

      //val title1 = new Picker("/hello.html", "#title")

      val title = pick("/hello.html", "#title") {
        "#title" -> "id" %= "replaced"
        "h1" %+ "demo"
        "h1" -> "test" %= "demo"
      }

      <div id="replaced">
        <h1 test="demo" class=" demo">Hello</h1>
      </div> must ==/(s2x(title))
    }

  }
}
