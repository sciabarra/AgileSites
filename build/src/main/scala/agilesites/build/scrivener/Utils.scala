package agilesites.build.scrivener

import java.io.File
import java.io.FileReader

trait Utils {

  def file(s: String) = new File(s)

  def file(f: File, s: String) = new File(f, s)

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

}