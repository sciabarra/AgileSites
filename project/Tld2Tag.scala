import scala.xml._
import java.io.File

object Tld2Tag {

  def tld2class(s: String) = {
    val f = (new File(s)).getName
    val f1 = f.take(f.size - 4)
    "%s%sTag".format(f1.charAt(0).toUpper, f1.substring(1))
  }

  def preHead(s: String) = {
    val cl = tld2class(s)
    """package wcs.scala.tag
    
import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.FTValList
    
object %s  {    
  val _debug = java.lang.System.getProperty("wcs.tag.debug") != null;
  val log  = java.util.logging.Logger.getLogger("%s");
    
 """.format(cl,cl)
  }

  val preBody = """ {
  val _args_ : FTValList = new FTValList()
"""

  def postBody(lib: String, tag: String) = {

    val lt = lib.toUpperCase + "." + tag.toUpperCase;

    """
  _params_.foreach {
     x => _args_.setValString(x._1.toString.substring(1), x._2)
  } 
  _ics_.runTag("%s", _args_);
  if(_debug) {
     Console.println(ftValList2String("%s", _args_))
     log.finest(ftValList2String("%s", _args_))
  }
  _ics_.GetErrno
}
""".format(lt, lt, lt)

  }

  val postHead = """
  // dump an ftval list for logging
  def ftValList2String(name: String, vl: FTValList) = {
    var sb = new StringBuilder()
    sb.append(">>>");
    sb.append(name)
    sb.append(":")
    val en = vl.keys
    while (en.hasMoreElements()) {
      val k = en.nextElement().toString()
      val v = vl.getValString(k)
      sb.append(" %s=%s".format(k, v))
    }
    sb.toString()
  }
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

      val argOptional = for (attr <- optional) yield {
        "`" + (attr \\ "name" text) + "`" + ": String = null"
      }

      val valRequired = for (attr <- required) yield {
        val name = (attr \\ "name" text)
        "  _args_.setValString(\"" + name.toUpperCase + "\", `" + name + "`)"
      }

      val valOptional = for (attr <- optional) yield {
        val name = (attr \\ "name" text)
        "  if(`" + name + "`!=null)\n" +
          "    _args_.setValString(\"" + name.toUpperCase + "\", `" + name + "`)"
      }

      " def `" + tagname + "`(\n" +
        (argRequired).mkString(",\n") +
        (if (argRequired.isEmpty) "" else ",") +
        "\n_params_ : Tuple2[Symbol,String]*)" +
        (if (argOptional.isEmpty) ""
        else argOptional.mkString("(", ",\n", ")")) +
        "(implicit _ics_ : ICS) : Int =" +
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

  def main(args: Array[String]) {
    args.foreach {
      a: String => println(Tld2Tag(a))
    }
  }
}