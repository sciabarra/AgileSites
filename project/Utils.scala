package wcs.build

import sbt._
import Keys._
import Dialog._

trait Utils {

 // name says it all  
  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

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
    (file("export") / "envision" / defaultWorkspace(sites)).mkdir
    (file("export") / ("Populate")).mkdir
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
  
    val destlib = file(shared) / "agilesites" / "lib"
    destlib.mkdirs 

    // jars to include when performing a setup
    val addJars = classpathFiles filter(AgileSitesBuild.setupFilter accept _)
    //println(addJars)

    // jars to remove when performing a setup
    val removeJars = destlib.listFiles
    //println(removeJars)  
    
    setupCopyJars(destlib, addJars, removeJars)
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


}