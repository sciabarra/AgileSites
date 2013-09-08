package wcs.build

import sbt._
import Keys._
import Dialog._

trait Utils {

  // name says it all  
  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

  // is an html file?
  def isHtml(f: File) = !("\\.html?$".r findFirstIn f.getName.toLowerCase isEmpty)

  // copy files from a src dir to a target dir recursively 
  // filter files to copy
  def recursiveCopy(src: File, tgt: File, verbose: Boolean = false)(sel: File => Boolean) = {
    val nsrc = src.getPath.length
    val cplist = (src ** "*").get.filterNot(_.isDirectory).filter(sel) map {
      x =>
        val dest = tgt / x.getPath.substring(nsrc)
        if (verbose)
          println("+++ " + dest)
        (x, dest)
    }
    IO.copy(cplist).toSeq
  }

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

  // configure satellite
  def setupServletRequest(webapp: String, sites: String, sitesSeq: Seq[Tuple2[String, String]], flexBlobs: String, staticBlobs: String) {

    val prpFile = file(webapp) / "WEB-INF" / "classes" / "ServletRequest.properties"

    val prp = new java.util.Properties
    prp.load(new java.io.FileReader(prpFile))

    // shift the url assembler to add agilesites as the first
    if (prp.getProperty("uri.assembler.1.shortform") != "agilesites") {

      val p1s = prp.getProperty("uri.assembler.1.shortform")
      val p1c = prp.getProperty("uri.assembler.1.classname")
      val p2s = prp.getProperty("uri.assembler.2.shortform")
      val p2c = prp.getProperty("uri.assembler.2.classname")

      prp.setProperty("uri.assembler.3.shortform", p2s)
      prp.setProperty("uri.assembler.3.classname", p2c)
      prp.setProperty("uri.assembler.2.shortform", p1s)
      prp.setProperty("uri.assembler.2.classname", p1c)
      prp.setProperty("uri.assembler.1.shortform", "agilesites")
      prp.setProperty("uri.assembler.1.classname", "wcs.core.Assembler")
    }

    for ((k, v) <- sitesSeq) {
      prp.setProperty("agilesites.site." + normalizeSiteName(k), v)
    }

    prp.setProperty("agilesites.blob.flex", flexBlobs)
    prp.setProperty("agilesites.blob.static", staticBlobs)

    // store
    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileSites setup")
  }

  // configure futurentense.ini
  def setupFutureTenseIni(home: String, static: String, appjar: String, sites: String, version: String) {

    val prpFile = file(home) / "futuretense.ini"
    val prp = new java.util.Properties
    prp.load(new java.io.FileReader(prpFile))

    //prp.setProperty("agilesites.sites", sites); // not used for now

    prp.setProperty("agilesites.jar", appjar);
    prp.setProperty("agilesites.static", file(static).getAbsolutePath);

    prp.setProperty("cs.csdtfolder", file("export").getAbsolutePath)
    prp.setProperty("cs.pgexportfolder", (file("export") / "xmlpub" / (sites + "-" + version)).getAbsolutePath)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileSites setup")

  }

  def setupAgileSitesPrp(dir: String, sites: String, static: String, appjar: String, flexBlobs: String, staticBlobs: String) {
    val prpFile = file(dir) / "WEB-INF" / "classes" / "agilesites.properties"
    val prp = new java.util.Properties

    if (prpFile.exists)
      prpFile.delete

    prp.setProperty("agilesites.sites", sites);
    prp.setProperty("agilesites.jar", appjar);
    prp.setProperty("agilesites.static", file(static).getAbsolutePath);
    prp.setProperty("agilesites.blob.flex", flexBlobs)
    prp.setProperty("agilesites.blob.static", staticBlobs)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "created by AgileSites setup")

    //otherConfig.setProperty("agilesites.user", username);
    //otherConfig.setProperty("agilesites.password", password);
    //otherConfig.setProperty("cs.csdtfolder", file("export").getAbsolutePath)
    //otherConfig.setProperty("cs.pgexportfolder", file("export").getAbsolutePath)
  }

  def setupMkdirs(shared: String, version: String, sites: String) {
    // create local export dir for csdt
    (file("export")).mkdir
    (file("export") / "xmlpub").mkdir
    (file("export") / "xmlpub" / (sites + "-" + version)).mkdir
    (file("export") / "envision").mkdir
    (file("export") / "envision" / (sites + "-" + version)).mkdir
    (file("export") / ("Populate-" + version)).mkdir
    (file(shared) / "Storage").mkdir
    (file(shared) / "Storage" / "Static").mkdir
  }

  // classpath.files
  // destlib.listFiles
  def setupCopyJars(webapp: String, classpathFiles: Seq[File]) {

    val destlib = file(webapp) / "WEB-INF" / "lib"

    // jars to include when performing a setup

    val addJars = classpathFiles filter
      (AgileSitesBuild.addFilterSetup accept _)

    // jars to remove when performing a setup

    val removeJars = destlib.listFiles filter
      (AgileSitesBuild.removeFilterSetup accept _)

    // remove jars
    println("** removing old version of files");
    for (file <- removeJars) {
      val tgt = destlib / file.getName
      tgt.delete
      println("- " + tgt.getAbsolutePath)
    }

    // add jars
    println("** installing new version of files");
    for (file <- addJars) yield {
      val tgt = destlib / file.getName
      IO.copyFile(file, tgt)
      //println("=== " + file.getAbsolutePath)
      println("+ " + tgt.getAbsolutePath)
    }

  }

  def deploy(url: String, user: String, pass: String, args: Seq[String], sites: String, srcFile: File, dir: String, file: String) = {

    //val baseEnc = java.net.URLEncoder.encode(base.getAbsolutePath, "UTF-8");
    val drop = if (args.length > 0 && args(0) == "drop") "yes" else "no"

    //val srcUrl = java.net.URLEncoder.encode(srcFile.getURI.getURL, "UTF-8");
    val srcUrl = srcFile.toString

    val cmd = "&sites=%s&drop=%s&url=%s&dir=%s&file=%s".format(sites, drop, srcUrl, dir, file)

    println(httpCall("Setup", cmd, url, user, pass))

  }

  /**
   * Look for a java source file
   */
  def extractClassAndIndex(file: File): Option[Tuple2[String, String]] = {
    import scala.io._

    //println("***" + file)

    var packageRes: Option[String] = None;
    var indexRes: Option[String] = None;
    var classRes: Option[String] = None;
    val packageRe = """.*package\s+([\w\.]+)\s*;.*""".r;
    val indexRe = """.*@AddIndex\(\"(.*?)\"\).*""".r;
    val classRe = """.*class\s+(\w+).*""".r;

    if (file.getName.endsWith(".java") || file.getName.endsWith(".scala"))
      for (line <- Source.fromFile(file).getLines) {
        line match {
          case packageRe(m) =>
            //println(line + ":" + m)
            packageRes = Some(m)
          case indexRe(m) =>
            //println(line + ":" + m)
            indexRes = Some(m)
          case classRe(m) =>
            //println(line + ":" + m)
            classRes = Some(m)
          case _ => ()
        }
      }

    if (packageRes.isEmpty || indexRes.isEmpty || classRes.isEmpty)
      None
    else {
      val t = (indexRes.get, packageRes.get + "." + classRes.get)
      Some(t)
    }

    // TODO: rewrite in functional style
    // maybe this is better for (p <- packageRes; i <- indexRes; c <- classRes) yield { (i, p + "." + c) }

  }

  def writeFile(file: File, body: String) = {
    //println("*** %s%s****\n".format(file.toString, body))
    println("+++ %s".format(file.toString))
    val w = new java.io.FileWriter(file)
    w.write(body)
    w.close
  }
	
}