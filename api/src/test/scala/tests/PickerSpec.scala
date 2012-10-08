package tests

import org.jsoup._
import wcs.Picker
import org.specs2.mutable.Specification

object PickerSpec extends Specification with Picker {

  val base = "http://localhost:8380/cs/"
  val html = "/index.html"
  
  "pick should" in {
    
    pick("/hello") {
      current
    } must_== "<h1>Hello</h1>"
    
  }
  
  "select should" in {
    
  }
  
  "% should" in {
    
  }
    
  "%! should" in {
    
  }
  
  "%> should" in {
    
  }
  
  "%!> should" in {
    
  }
  
  def sample() {
    val in = getClass.getResourceAsStream(html)
    val doc = Jsoup.parse(in, null, base)
    doc.select("#body").html("<p>I am the new body</p>")
    doc.select("#menu").append("<li>alpha").append("<li>beta")
    println(doc)
  }

}