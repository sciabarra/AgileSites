import scala.xml._
import java.io.File

object Tld2Tagj {

  val javaKeywords = Set(
    "abstract", "continue", "for", "new", "switch",
    "assert", "default", "goto", "package", "synchronized",
    "boolean", "do", "if", "private", "this",
    "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws",
    "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try",
    "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile",
    "const", "float", "native", "super", "while", "string", "system")

  def tld2class(s: String) = {
    val f = (new File(s)).getName
    val f1 = f.take(f.size - 4)
    "%s%sTag".format(f1.charAt(0).toUpper, f1.substring(1))
  }

  def preHead(s: String) = {
    val cl = tld2class(s)
    """package wcs.java.tag;
    
import COM.FutureTense.Interfaces.*;
import java.util.logging.*;
import java.lang.String;
    
public class %s  {    
  private static boolean debug = java.lang.System.getProperty("wcs.tag.debug") != null;
  private static Logger log = Logger.getLogger("%s");  
  static String ftValList2String(String name, FTValList vl)  {
    StringBuilder sb = new StringBuilder();
    sb.append(">>>");
    sb.append(name);
    sb.append(":");
    java.util.Enumeration en = vl.keys();
    while (en.hasMoreElements()) {
      String k = en.nextElement().toString();
      Object v = vl.getValString(k);
      sb.append( " "+k+"="+v);
    }
    return sb.toString();
  }
    
 """.format(cl, cl)
  }

  def body(lib: String, name: String, reqArgs: List[String], optArgs: List[String]): String = {

    val uname = if (javaKeywords.contains(name)) name + "_" else name
    val cname = uname(0).toUpperCase + uname.substring(1)
    val lname = uname.toLowerCase;
    val tname = lib.toUpperCase + "." + uname.toUpperCase

    val parList = reqArgs.map("String " + _.toString) mkString ","

    val parnameList = reqArgs.map(_.toString) mkString ","

    val setRequired = reqArgs.map(x => "args.setValString(\"" + x + "\", " + x + ");").mkString("\n")

    val setOptional = optArgs map { x =>
      """ public %s %s(String val) { args.setValString("%s", val); return this; } 
      """.format(cname, x, x)
    } mkString "\n";

    """
public static class %s {
  private FTValList args = new FTValList();
  public %s set(String name, String value) { args.setValString(name,value); return this; }
%s
""".format(cname, cname, setOptional) +
      """
  public %s(%s) {
%s
  }
""".format(cname, parList, setRequired) +
      """
  public int run(ICS ics) { 
      ics.runTag("%s", args); 
      if(debug) {
         java.lang.System.err.println(ftValList2String("%s", args));
         log.finest(ftValList2String("%s", args));
      }
      return ics.GetErrno(); 
  }
}
""".format(tname, cname, cname) +
      """
public static %s %s(%s) {
  return new %s(%s);
}
""".format(cname, lname, parList, cname, parnameList)

  }

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

      val reqList = required.map { _ \\ "name" text }.toList
      val optList = optional.map { _ \\ "name" text }.toList

      body(libname, tagname, reqList, optList)

    }

    // result
    preHead(filename) +
      res.mkString("\n") +
      postHead

    //"%d".format(tags.size)

  }

  def main(args: Array[String]) {
    import java.io._
    args.foreach {
      f: String =>
        val jf = "\\.tld$".r.replaceAllIn(f, ".java")
        println("+++ " + jf)
        val out = new FileWriter(jf)
        out.write(Tld2Tagj(jf))
        out.close
    }
  }
}