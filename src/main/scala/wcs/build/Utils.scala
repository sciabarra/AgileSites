package wcs.build

import java.io.File
import java.net.URL

trait Utils {

  // name says it all  
  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

 
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
      val req = "%s/ContentServer?pagename=AAAgile%s&user=%s&pass=%s%s%s"
        .format(url, op, user, pass, option, site)
      println(">>> " + req + "")
      httpCallRaw(req)
    }
    out mkString ""
  }

  def writeFile(file: File, body: String) = {
    val res = "*** %s%s****\n".format(file.toString, body)
    //println("+++ %s".format(file.toString))
    val w = new java.io.FileWriter(file)
    w.write(body)
    w.close
    file.toString
    //body
  }
    
}