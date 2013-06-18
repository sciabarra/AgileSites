package wcs.build

import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import Dialog._

trait AgileSitesSupport {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsShared = SettingKey[String]("wcs-shared", "WCS Shared Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS ContentServer Webapp CS Directory")
  lazy val wcsVersion = SettingKey[String]("wcs-version", "WCS or Fatwire Version")

  lazy val wcsUrl = SettingKey[String]("wcs-url", "WCS URL")
  lazy val wcsUser = SettingKey[String]("wcs-user", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-password", "WCS Site password")

  lazy val wcsSites = SettingKey[String]("wcs-sites", "WCS Sites to configure")
  lazy val wcsVirtualHosts = SettingKey[Seq[Tuple2[String, String]]]("wcs-virtual-hosts", "WCS Virtual Host mapping")

  lazy val wcsSetupOffline = InputKey[Unit]("wcs-setup-offline", "WCS Setup Offline")
  lazy val wcsSetupOnline =  InputKey[Unit]("wcs-setup-online", "WCS Setup Online")
  lazy val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS Deploy")

  lazy val wcsFlexBlobs = SettingKey[String]("wcs-flex-blobs", "WCS Flex Blobs Regexp")
  lazy val wcsStaticBlobs = SettingKey[String]("wcs-static-blobs", "WCS Static Blobs Regexp")
  lazy val wcsWebappSatellite = SettingKey[String]("wcs-webapp-satellite", "WCS SatelliteServer Webapp CS Directory")

  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsCsdt = InputKey[Unit]("wcs-dt", "WCS Development Tool")

  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy static resources")
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  lazy val wcsUpdateAssets = TaskKey[String]("wcs-update-assets", "WCS update assets")
  lazy val wcsLog = InputKey[Unit]("wcs-log", "WCS log manager")
  

  // initial value for sites and vhosts
  //val wcsSitesTask = wcsSites := Seq[String]()

  val wcsVirtualHostsTask = wcsVirtualHosts := Seq[Tuple2[String, String]]() ;

  // generate tag access classes from tld files
  val coreGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp, baseDirectory, wcsVersion) map {
      (dstDir, srcDir, base, version) =>

        // generate tags
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"

        if(!tlds.isDirectory)
          throw new RuntimeException("not found files in "+tlds)
          
        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val cls = Tld2Tag.tld2class(src)
          val clsj = Tld2Tagj.tld2class(src)
          val dstj = file(dstDir / clsj + ".java")
          //	if tld.getName.equalsIgnoreCase("asset.tld") // select only one for debug generator
        } yield {
          if (!dstj.exists) {
            val bodyj = Tld2Tagj(src)
            IO.write(dstj, bodyj)
            println("+++ " + dstj)
          }
          dstj 
        }

        // copy versioned class
        val src = base / "src" / "main" / "version" / version
        val ll = for {
          file <- src.listFiles
          dfile = dstDir / file.getName
        } yield {
          println("+++ " + dfile)
          IO.copyFile(file, dfile)
          dfile
        }

        // return files generated and copied
        l.toSeq ++ ll.toSeq
    }

  // copy files from a src dir to a target dir recursively 
  // filter files to copy
  def recursiveCopy(src: File, tgt: File)(sel: File => Boolean) = {
    val nsrc = src.getPath.length
    val cplist = (src ** "*").get.filterNot(_.isDirectory).filter(sel) map {
      x =>
        val dest = tgt / x.getPath.substring(nsrc)
        println("+++ " + dest)
        (x, dest)
    }
    IO.copy(cplist).toSeq
  }

  // interface to csdt from sbt
  val wcsCsdtTask = wcsCsdt <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, version, url, sites, user, password, classpath, s, runner) =>
          val re = "^(cas-client-core-\\d|csdt-client-\\d|rest-api-\\d|wem-sso-api-\\d|wem-sso-api-cas-\\d|spring-\\d|commons-logging-|servlet-api).*.jar$".r;
          val seljars = classpath.files.filter(f => !re.findAllIn(f.getName).isEmpty)
          val firstArg = if (args.size > 0) args(0) else "listcs"
          var resources = if (args.size > 1) args.drop(1)
          else firstArg match {
            case "listcs" => Seq("@ALL_ASSETS;@ALL_NONASSETS")
            case "listds" => Seq("@ALL_ASSETS;@ALL_NONASSETS")
            case "import" =>
              Seq("@SITE", "@ASSET_TYPE", "@ALL_ASSETS", "@STARTMENU", "@TREETAB")
            case "export" =>
              Seq("@SITE", "@ASSET_TYPE", "@ALL_ASSETS", "@STARTMENU", "@TREETAB")
            case _ =>
              println("Unknown command")
              Seq()
          }

          for (res <- resources) {
            val cmd = Array(url + "/ContentServer",
              "username=" + user,
              "password=" + password,
              "cmd=" + firstArg,
              "resources=" + res,
              "fromSites=" + sites,
              "datastore=" + sites + "-" + version)

            Run.run("com.fatwire.csdt.client.main.CSDT",
              seljars, cmd, s.log)(runner)
          }
      }
  }

  // interface to catalogmover from sbt
  val wcsSetupOnlineTask = wcsSetupOnline <<= inputTask {
   (argsTask: TaskKey[Seq[String]]) =>
    (argsTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
     (args, version, httpUrl, sites, user, password, classpath, s, runner) =>
        val seljars = classpath.files
        val url = httpUrl + "/CatalogManager"

        //println(url)

        val cp = classpath.files.mkString(java.io.File.pathSeparator)
        val dir = file("export") / "Populate-" + version
        val cmd = Seq("-cp", cp, "COM.FutureTense.Apps.CatalogMover")

        val opts = Seq("-u", user, "-p", password, "-b", url, "-d", dir.toString, "-x")
        val all = cmd ++ opts ++ Seq("import_all")

        if(args.length == 0) messageDialog("Ensure the application server is UP and RUNNING, then press OK")
        Fork.java(None, all, Some(new java.io.File(".")), s.log)
        if(args.length == 0) messageDialog("Setup Complete.\nYou can now create site and templates.\nYou need to deploy them with \"wcs-deploy\".")
    }
}


  // package jar task - build the jar and copy it  to destination 
  val wcsPackageJarTask = wcsPackageJar <<=
    (assembly, wcsShared) map {
      (jar, shared) =>

        val destdir = file(shared) / "agilesites"
        val destjar = file(shared) / "agilesites" / jar.getName

        destdir.mkdir
        IO.copyFile(jar, destjar)
        println("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  def isHtml(f: File) = !("\\.html?$".r findFirstIn f.getName.toLowerCase isEmpty)

  // copy resources from the app to the webapp task
  val wcsCopyStaticTask = wcsCopyStatic <<=
    (baseDirectory, wcsWebapp) map {
      (base, tgt) =>
        val src = base / "app" / "src" / "main" / "static"
        recursiveCopy(src, file(tgt))(x=>true)
    }

  val wcsCopyHtmlTask =
    (resourceGenerators in Compile) <+=
      (baseDirectory, resourceManaged in Compile) map {
        (base, dstDir) =>
          val srcDir = base / "src" / "main" / "static"
          println("*** " + srcDir)
          recursiveCopy(srcDir, dstDir)(isHtml)
      }


  // copy resources to the webapp task
  val wcsUpdateAssetsTask = wcsUpdateAssets <<=
    (wcsUrl, wcsSites, wcsUser, wcsPassword, wcsPackageJar) map {
      (url, sites, user, pass, _) =>
        val deployer = new AgileSitesDeployer(url, sites, user, pass)
        println(deployer.deploy())
        deployer.getStatus
    }

  def normalizeSiteName(s: String) = s.toLowerCase.replaceAll("""[^a-z0-9]+""", "")

  // deploy task  
  val wcsDeployTask = wcsDeploy <<=
    (wcsCopyStatic, wcsUpdateAssets) map { (count, update) => () }

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

  // setup task
  val wcsSetupOfflineTask = wcsSetupOffline <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask,
        wcsPackageJar, managedClasspath in Runtime, classDirectory in Compile,
        wcsSites, wcsVersion, wcsHome, wcsShared, wcsWebapp, wcsUrl,
        wcsWebappSatellite, wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts) map {
          (args,
          appjar, classpath, classes,
          sites, version, home, shared, webapp, url,
          satelliteWebapp, flexBlobs, staticBlobs, virtualHosts) =>

            val static = (file(shared) / "Storage" / "Static") getAbsolutePath


            val silent = ( args.length > 0 && args(args.length-1) == "silent" )  

            if(!silent)
              messageDialog("Ensure the application server is NOT RUNNING, then press OK")

            if (args.length == 1 && args(0) == "satellite") {

              println("*** Installing AgileSites for Satellite  ***");

              setupCopyJars(satelliteWebapp, classpath.files)
              setupServletRequest(satelliteWebapp, sites, virtualHosts, flexBlobs, staticBlobs)
              
              //setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs) //not used for now

              if(!silent)
                messageDialog("Installation Complete. Please restart your application server.")

            } else {

              println("*** Installing AgileSites for ContentServer ***");

              val vhosts = (sites split ",") map { site =>
                (site, url + "/Satellite/" + normalizeSiteName(site))
              } toSeq

              setupMkdirs(shared, version, sites)

              setupCopyJars(webapp, classpath.files)
              setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)
              setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs)
              setupFutureTenseIni(home, static, appjar, sites, version)
              
              if(!silent)
                messageDialog("Installation Complete. Please restart your application server.\nYou need to complete with \"wcs-setup-online\".")
            }

        }
  }

  val wcsLogTask = wcsLog <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsUrl, wcsUser, wcsPassword, streams) map {
        (args, url, user, pass, streams) =>

          def parse(ls: List[String]) = ls match {
            case Nil => "host=127.0.0.1&port=4445"
            case logger :: Nil => "logger=" + logger + "&host=127.0.0.1&port=4445"
            case logger :: port :: Nil => "logger=" + logger + "&port=" + port + "&host=127.0.0.1"
            case logger :: port :: host :: _ => "logger=" + logger + "&host=" + host + "&port=" + port
          }

          def usage {
            println("""|usage:
                  | wcs-log view
            	    |    start the log viewer
                  | wcs-log list
            	    |    list what you are sending to the remote logger
                  | wcs-log <level> [<logger>] [<port>] [<host>]
                  |    enable logging to the log viewer
                  | wcs-log stop [<logger>] [<port>] [<host>] 
                  |    disable logging to the log viewer
                  | <level>  must be one of error, warn, info, debug, trace
                  | <logger> defaults to the root loogger, you can restrict to your packages
                  | <port>   defaults to 4445
                  | <host>   defaults to 127.0.0.1
                  |""".stripMargin)
          }

          val cmd = args match {
            case Nil =>
              usage
              None
            case "view" :: Nil =>
              new Thread {
                override def run {
                  Fork.java(None, Seq("-jar", "lumbermill.jar"),
                    Some(new File("bin")), StdoutOutput)
                }
              }.start
              None
            case "start" :: Nil =>
              Some("op=start&level=DEBUG&%s" format parse(Nil))
            case "stop" :: rest =>
              Some("op=stop&%s" format parse(rest))
            case "list" :: rest =>
              Some("op=list")
            case level :: rest =>
              if ("ERROR|WARN|DEBUG|INFO|TRACE".r findAllIn level.toUpperCase isEmpty) {
                println("Invalid level " + level)
                None
              } else
                Some("op=start&level=%s&%s" format (level.toUpperCase, parse(rest)))
            case _ =>
              usage
              None
          }

          if (cmd.isDefined) {
            val req = "%s/ContentServer?pagename=AAAgileLog&user=%s&pass=%s&%s"
              .format(url, user, pass, cmd.get)
            //println(req)
            val scan = new java.util.Scanner(new URL(req).openStream(), "UTF-8")
            println(scan.useDelimiter("\\A").next())
            scan.close
          }
      }
  }

}
