package agilesites.build
import sbt._
import Keys._

trait SetupSettings {
  this: Plugin with UtilSettings with ConfigSettings with DeploySettings =>

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

      if (p4s != null && p4s != "") prp.setProperty("uri.assembler.5.shortform", p4s)
      if (p4c != null && p4c != "") prp.setProperty("uri.assembler.5.classname", p4c)
      if (p3s != null && p4s != "") prp.setProperty("uri.assembler.4.shortform", p3s)
      if (p3s != null && p4s != "") prp.setProperty("uri.assembler.4.classname", p3c)

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
  def setupCopyJarsWeb(webapp: String, classpathFiles: Seq[File], version: String) {

    val destlib = file(webapp) / "WEB-INF" / "lib"

    val addJars = classpathFiles.filter(webappFilter accept _)

    val removeJars = destlib.listFiles.filter(webappFilter accept _)

    setupCopyJars(destlib, addJars, removeJars)

    //println(destlib)
  }

  // select jars for the setup online
  def setupCopyJarsLib(shared: String, classpathFiles: Seq[File]) {

    val parentlib = file(shared) / "agilesites"

    val destlib = parentlib / "lib"

    destlib.mkdirs

    // jars to include when performing a setup
    val addJars = classpathFiles filter (setupFilter accept _)
    //println(addJars)

    // jars to remove when performing a setup
    val removeJars = destlib.listFiles
    //println(removeJars)  

    setupCopyJars(destlib, addJars, removeJars)

    for (file <- destlib.listFiles) {
      val parentfile = parentlib / file.getName
      if (parentfile.exists) {
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

  // copy jars in the webapp lib folder
  lazy val sitesCopyJarsWeb = taskKey[Unit]("Sites Copy Jars to WEB-INF/lib")
  val sitesCopyJarsWebTask = sitesCopyJarsWeb <<=
    (sitesHello, fullClasspath in Compile, sitesWebapp, sitesVersion) map {
      (hello, classpath, webapp, version) =>
        if (!hello.isEmpty)
          throw new Exception("Web Center Sites must be offline.")
        setupCopyJarsWeb(webapp, classpath.files, version)
    }

  // copy jars in the agilesites lib 
  lazy val sitesCopyJarsLib = taskKey[Unit]("Sites Copy Jars to agilesites/lib")
  val sitesCopyJarsLibTask = sitesCopyJarsLib <<=
    (sitesHello, fullClasspath in Compile, sitesShared) map {
      (hello, classpath, shared) =>
        if (!hello.isEmpty)
          throw new Exception("Web Center Sites must be offline.")
        setupCopyJarsLib(shared, classpath.files)
    }

  lazy val sitesSetup = taskKey[Unit]("Sites Setup Offline")
  val sitesSetupTask = sitesSetup := {

    val classes = (classDirectory in Compile).value
    val sites = asSites.value
    val version = sitesVersion.value
    val home = sitesHome.value
    val shared = sitesShared.value
    val webapp = sitesWebapp.value
    val url = sitesUrl.value
    val flexBlobs = asFlexRegex.value
    val staticBlobs = asBlobRegex.value
    val jar = asPackage.value

    val static = (file(shared) / "Storage" / "Static") getAbsolutePath

    println("*** Installing AgileSites for WebCenter Sites ***");

    val vhosts = (sites split ",") map { site =>
      (site, url + "/Satellite/" + normalizeSiteName(site))
    } toSeq

    setupMkdirs(shared, version, sites)
    setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)
    setupFutureTenseIni(home, shared, static, sites, version)

    // remove any other jar starting with agilesites-all-assembly 
    // remnants of the past
    for (f <- (file(shared) / "agilesites").listFiles) {
      if (f.isFile && f.getName.startsWith("agilesites-all-assembly")) {
        //println("candidate to removal: "+f)
        if (!f.getAbsolutePath.equals(jar)) {
          f.delete
          println("--- " + f);
        } else {
          //println("not removing "+jar)
        }
      }
    }

    // remove pupulate mark if there
    (file(home) / "populate.done").delete

    println("""**** Setup Complete.
              |**** Please restart your application server.
              |**** You need to complete installation with "wcs-deploy".""".stripMargin)
  }

  //val asAssemblyTarget = settingKey[File]("target of an assembly")

  //val asAssemblyTask = taskKey[Unit]("copy the target of an assembly")

  //asCopyAssemblyTarget := file("bin"),

  val setupSettings = Seq()
}