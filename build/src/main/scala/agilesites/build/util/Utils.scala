package agilesites.build.util

import java.io.File
import java.io.FileReader
import java.net.URL

trait Utils {

  //def file(s: String) = new File(s)

  //def file(f: File, s: String) = new File(f, s)

  // read a file
  def readFile(f: File) = {
    val fr = new FileReader(f)
    val buf = new Array[Char](f.length().toInt)
    fr.read(buf)
    fr.close()
    new String(buf)
  }

  // get a wrapped property
  def prp(property: String) = {
    val r = System.getProperty(property)
    if (r == null)
      None
    else
      Some(r)
  }

  // simple http call returning the result as a string
  def httpCallRaw(req: String) = {
    val scan = new java.util.Scanner(new URL(req).openStream(), "UTF-8")
    val res = scan.useDelimiter("\\A").next()
    scan.close
    //">>>%s\n%s<<<%s\n" format(req,res,req)
    res
  }

}