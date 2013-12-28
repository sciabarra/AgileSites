package wcs.build

import sbt._
import Keys._
import Dialog._

import sbtassembly.Plugin._
import AssemblyKeys._

trait AgileSitesUtil {

 // name says it all  
  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

  // is an html file?
  def isHtml(f: File) = !("\\.html?$".r findFirstIn f.getName.toLowerCase isEmpty)

  // copy files from a src dir to a target dir recursively 
  // filter files to copy
  def recursiveCopy(src: File, tgt: File, log: Logger)(sel: File => Boolean) = {
    val nsrc = src.getPath.length
    val cplist = (src ** "*").get.filterNot(_.isDirectory).filter(sel) map {
      x =>
        val dest = tgt / x.getPath.substring(nsrc)
        log.debug("+++ " + dest)
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
      val req = "%s/ContentServer?pagename=AAAgile%s&username=%s&password=%s%s%s"
        .format(url, op, user, pass, option, site)
      println(">>> " + req + "")
      httpCallRaw(req)
    }
    out mkString ""
  }

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
      val p3s = prp.getProperty("uri.assembler.3.shortform")
      val p3c = prp.getProperty("uri.assembler.3.classname")
      val p4s = prp.getProperty("uri.assembler.4.shortform")
      val p4c = prp.getProperty("uri.assembler.4.classname")

      if(p4s!=null && p4s!="") prp.setProperty("uri.assembler.5.shortform", p4s)
      if(p4c!=null && p4c!="") prp.setProperty("uri.assembler.5.classname", p4c)
      if(p3s!=null && p4s!="") prp.setProperty("uri.assembler.4.shortform", p3s)
      if(p3s!=null && p4s!="") prp.setProperty("uri.assembler.4.classname", p3c)
      prp.setProperty("uri.assembler.3.shortform", p2s)
      prp.setProperty("uri.assembler.3.classname", p2c)
      prp.setProperty("uri.assembler.2.shortform", p1s)
      prp.setProperty("uri.assembler.2.classname", p1c)
      prp.setProperty("uri.assembler.1.shortform", "agilesites")
      prp.setProperty("uri.assembler.1.classname", "wcs.core.Assembler")
    }

    for ((k, v) <- sitesSeq) {
      prp.setProperty("agilesites.site." + normalizeSiteName(k), v)
      prp.setProperty("agilesites.name." + normalizeSiteName(k), k)
    }

    prp.setProperty("agilesites.blob.flex", flexBlobs)
    prp.setProperty("agilesites.blob.static", staticBlobs)

    // store
    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileSites setup")
  }

  // configure futurentense.ini
  def setupFutureTenseIni(home: String, shared: String, sites: String, static: String, version: String) {

    val prpFile = file(home) / "futuretense.ini"
    val prp = new java.util.Properties
    prp.load(new java.io.FileReader(prpFile))

    val jardir = file(shared) / "agilesites"

    prp.setProperty("agilesites.dir", jardir.getAbsolutePath);
    prp.setProperty("agilesites.poll", "1000");
    prp.setProperty("agilesites.static", file(static).getAbsolutePath);
    prp.setProperty("cs.csdtfolder", file("export").getAbsolutePath)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileSites setup")

  }

  // create a static configuration file
  def setupAgileSitesPrp(dir: String, shared: String, sites: String, static: String, flexBlobs: String, staticBlobs: String) {
    val prpFile = file(dir) / "WEB-INF" / "classes" / "agilesites.properties"
    val prp = new java.util.Properties

    if (prpFile.exists)
      prpFile.delete

    prp.setProperty("agilesites.sites", sites);
    prp.setProperty("agilesites.webapp", dir);
    prp.setProperty("agilesites.dir", (file(shared) / "agilesites").getAbsolutePath );
    prp.setProperty("agilesites.static", file(static).getAbsolutePath);
    prp.setProperty("agilesites.blob.flex", flexBlobs)
    prp.setProperty("agilesites.blob.static", staticBlobs)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "created by AgileSites setup")
  }

  // create directories for the setup
  def setupMkdirs(shared: String, version: String, sites: String) {
    // create local export dir for csdt
    (file("export")).mkdir
    (file("export") / "envision").mkdir
    (file("export") / "envision" / "cs_workspace").mkdir
    (file(shared) / "Storage").mkdir
    (file(shared) / "Storage" / "Static").mkdir
  }

  // select jars for the setup offline
  def setupCopyJarsWeb(webapp: String, classpathFiles: Seq[File]) {

    val destlib = file(webapp) / "WEB-INF" / "lib"

    val addJars = classpathFiles.filter( _.getName.startsWith("agilesites-core") )
    
    val removeJars = destlib.listFiles.filter(_.getName.toLowerCase.startsWith("agilesites-core"))

    setupCopyJars(destlib, addJars, removeJars)
  
    //println(destlib)
  }

  // select jars for the setup online
  def setupCopyJarsLib(shared: String, classpathFiles: Seq[File]) {
  
    val parentlib = file(shared) / "agilesites"

    val destlib = parentlib / "lib"

    destlib.mkdirs 

    // jars to include when performing a setup
    val addJars = classpathFiles filter(AgileSitesBuild.setupFilter accept _)
    //println(addJars)

    // jars to remove when performing a setup
    val removeJars = destlib.listFiles
    //println(removeJars)  
    
    setupCopyJars(destlib, addJars, removeJars)

    for(file <- destlib.listFiles) {
      val parentfile = parentlib / file.getName
      if(parentfile.exists) {
        parentfile.delete
        println("- " + parentfile.getAbsolutePath)
      }
    } 
      
 } 

  // copy jars filtering and and remove
  def setupCopyJars(destlib: File, addJars: Seq[File], removeJars: Seq[File]) {

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
      //println(file)
      println("+ " + tgt.getAbsolutePath)
    }

  }

  def deploy(url: String, user: String, pass: String, sites: String) {
    println(httpCall("Setup", "&sites=%s".format(sites), url, user, pass))
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
    val indexRe = """.*@Index\(\"(.*?)\"\).*""".r;
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

  def writeFile(file: File, body: String, log: Logger) = {
    //println("*** %s%s****\n".format(file.toString, body))
    log.debug("+++ %s".format(file.toString))
    val w = new java.io.FileWriter(file)
    w.write(body)
    w.close
  }

  // find the default workspace from sites
  def defaultWorkspace(sites: String) =  normalizeSiteName(sites.split(",").head)
	
  def catalogManager(url: String, user: String, pass: String, jars: Seq[File], opts: Seq[String], log: Logger) {
        val cp = (Seq(file("bin").getAbsoluteFile) ++ jars).mkString(java.io.File.pathSeparator)
        val dir = file("core") / "populate"
        //println(dir.getAbsolutePath)
        val cmd = Seq("-cp", cp, "COM.FutureTense.Apps.CatalogMover")
        val stdopts = Seq("-u", user, "-p", pass, "-b", url + "/CatalogManager", "-d", dir.getAbsolutePath, "-x")
        val all = if(opts.size >0) {
                    if(opts(0)  ==  "view") cmd
                    else cmd ++ stdopts ++ opts
                  } else {  
                      log.info("use 'view' to display the gui")                      
                      log.info("use a sequence of options for other functions")
                      log.info("(not needed -u -p -b)")
                      cmd ++ Seq("-h")
                  } 

        Fork.java(None, all, Some(new java.io.File(".")), log)  
  }

}