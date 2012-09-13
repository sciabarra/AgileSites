import scala.xml._
import java.io.File

object Tld2Tag {

  def tld2class(s: String) = {
    val f = (new File(s)).getName
    val f1 = f.take(f.size - 4)
    "%s%s".format(f1.charAt(0).toUpper, f1.substring(1))
  }

  def preHead(s: String) = """package wcs.tag
import COM.FutureTense.Interfaces.FTValList
import COM.FutureTense.Interfaces.ICS

object %s {
""".format(tld2class(s))

  val preBody = """ {
  val _args_ : FTValList = new FTValList()
"""

  def postBody(lib: String, tag: String) = """
  _ics_.runTag("%s.%s", _args_);
 }
""".format(lib.toUpperCase, tag.toUpperCase)

  val postHead = """
}
"""

  def apply(filename: String) = {
    // load file
    val xml = XML.loadFile(filename)

    // lib name
    val libname = new File(filename).getName.takeWhile(_ != '.')

    // enumerate tables
    val res = for (tag <- xml \\ "tag") yield {

      val tagname = (tag \ "name").text
      val attrs = tag \\ "attribute"

      val required = attrs.filter { a => (a \ "required").text == "true" }.distinct
      val optional = attrs.filter { a => (a \ "required").text == "false" }.distinct

      val argRequired = for (attr <- required) yield {
        "`" + (attr \\ "name" text) + "`" + ": String"
      }

      val valRequired = for (attr <- required) yield {
        val name = (attr \\ "name" text)
        "  _args_.setValString(\"" + name.toUpperCase + "\", `" + name + "`)"
      }

      val argOptional = for (attr <- optional) yield {
        "`" + (attr \\ "name" text) + "`" + ": String = null"
      }

      val valOptional = for (attr <- optional) yield {
        val name = (attr \\ "name" text)
        "  if(`" + name + "`!=null)\n" +
          "    _args_.setValString(\"" + name.toUpperCase + "\", `" + name + "`)"
      }


      " def `" + tagname + "`" +
        argRequired.mkString("(", ",\n", ")" +
          argOptional.mkString("(", ",\n", ")") +
          "(implicit _ics_ : ICS) =") +
        preBody +
        (valRequired ++ valOptional).mkString("\n") +
        postBody(libname, tagname)
    }

    // result
    preHead(filename) +
      res.mkString("\n") +
      postHead

    //"%d".format(tags.size)
  }
}