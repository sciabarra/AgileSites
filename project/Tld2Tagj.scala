package wcs.build

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
    """package wcs.core.tag;
    
import static wcs.Api.*;
import wcs.api.Arg;
import wcs.api.Log;
import COM.FutureTense.Interfaces.*;
    
public class %s  {    
  private static Log log = Log.getLog(%s.class);  
  static String ftValList2String(String name, FTValList vl)  {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append(":");
    java.util.Enumeration en = vl.keys();
    while (en.hasMoreElements()) {
      String k = en.nextElement().toString();
      Object v = vl.getValString(k);
      sb.append(" ").append(k).append("=").append(v!=null?v.toString():"null");
    }
    return sb.toString();
  }
    
 """.format(cl, cl)
  }

  def body(lib: String, name: String, parArgs: List[String]): String = {

    val uname = if (javaKeywords.contains(name)) name + "_" else name
    val cname = uname(0).toUpper + uname.substring(1)
    val lname = uname.toLowerCase;
    val tname = lib.toUpperCase + "." + uname.toUpperCase

    val setParams = parArgs map { x =>
      """ public %s %s(String val) { args.setValString("%s", val); return this; } 
      """.format(cname, x, x.toUpperCase)
    } mkString "\n";

    """
  public static class %s {
  private FTValList args = new FTValList();
  private String output;
  public %s set(String name, String value) { args.setValString(name,value); return this; }
%s
  public %s() { }""".format(cname, cname, setParams, cname) + """
  public int run(ICS ics) { 
      ics.runTag("%s", args); 
      log.trace(ftValList2String("%s", args));
      return ics.GetErrno(); 
  }
  public String eval(ICS ics, String output) {
	  String tmp = tmp();
	  args.setValString(output.toUpperCase(), tmp);
	  run(ics);
	  String res = ics.GetVar(tmp);
	  ics.RemoveVar(tmp);
	  return res;		
  } 
  public %s set(Arg ... _args) {
	  for(Arg arg: _args) {
		  args.setValString(arg.name, arg.value);
	  }
	  return this;
  }
}
""".format(tname, cname, cname, cname) + """
public static %s %s() {
  return new %s();
}
""".format(cname, lname, cname)

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

      val parList = reqList ::: optList

      body(libname, tagname, parList)

    }

    // result
    preHead(filename) +
      res.mkString("\n") +
      postHead
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