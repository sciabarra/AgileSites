package agilesites.build.util

import java.io.File
import java.io.FileReader
import java.net.URL

import sbt._

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

  def writeFile(file: File, body: String, log: sbt.Logger) = {
    //println("*** %s%s****\n".format(file.toString, body))
    if (log != null)
      log.debug("+++ %s".format(file.toString))
    file.getParentFile.mkdirs
    val w = new java.io.FileWriter(file)
    w.write(body)
    w.close
  }

  // is an html file?
  def isHtml(f: File) = !("\\.html?$".r findFirstIn f.getName.toLowerCase isEmpty)

  // is not a .less file?
  def notLess(f: File) = !f.getName.endsWith(".less")

  // copy files from a src dir to a target dir recursively 
  // filter files to copy
  def recursiveCopy(src: File, tgt: File, log: Logger)(sel: File => Boolean) = {
    val nsrc = src.getPath.length
    val cplist = (src ** "*").get.filterNot(_.isDirectory).filter(sel) map {
      x =>
        val dest = tgt / x.getPath.substring(nsrc)
        (x, dest)
    }
    if (log != null)
      log.info(s"copying #${cplist.size} files")
    IO.copy(cplist).toSeq
  }

  // write an index of the files in a subdirectory in the target file
  def recursiveIndex(srcDir: File, tgtFile: File, log: sbt.Logger)(sel: File => Boolean) = {
    val plen = srcDir.getAbsolutePath().size
    val body = (srcDir ** "*").
      filter(_.isFile).filter(sel).get.
      map(_.getAbsolutePath.substring(plen).replace(File.separatorChar, '/')).
      mkString("\n")
    writeFile(tgtFile, body, log)
    tgtFile
  }

  // simple http call returning the result as a string
  def httpCallRaw(req: String) = {
    val scan = new java.util.Scanner(new URL(req).openStream(), "UTF-8")
    val res = scan.useDelimiter("\\A").next()
    scan.close
    //">>>%s\n%s<<<%s\n" format(req,res,req)
    res
  }

  // invoking the url (for comma separated options)
  def httpCall(op: String, option: String, url: String, user: String, pass: String, sites: String = null) = {

    // create a site list if is is not empty
    val siteList = if (sites == null) {
      List("")
    } else {
      sites split (",") map { s => "&site=" + s } toList
    }

    //println(siteList)

    val out = for (site <- siteList) yield {
      val req = "%s/ContentServer?pagename=AAAgile%s&username=%s&password=%s%s%s"
        .format(url, op, user, pass, option, site)
      println(">>> " + req + "")
      httpCallRaw(req)
    }
    out mkString ""
  }

  // name says it all  
  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

}