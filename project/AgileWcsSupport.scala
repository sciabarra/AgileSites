package wcs.build

import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

trait AgileWcsSupport {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsSites = SettingKey[String]("wcs-sites", "WCS Sites to configure")
  lazy val wcsShared = SettingKey[String]("wcs-shared", "WCS Shared Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS ContentServer Webapp CS Directory")
  lazy val wcsVersion = SettingKey[String]("wcs-version", "WCS or Fatwire Version")

  lazy val wcsUrl = SettingKey[String]("wcs-url", "WCS URL")
  lazy val wcsUser = SettingKey[String]("wcs-user", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-password", "WCS Site password")

  lazy val wcsSetup = InputKey[Unit]("wcs-setup", "WCS Setup")
  lazy val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS Deploy")

  lazy val wcsFlexBlobs = SettingKey[String]("wcs-flex-blobs", "WCS Flex Blobs Regexp")
  lazy val wcsStaticBlobs = SettingKey[String]("wcs-static-blobs", "WCS Static Blobs Regexp")
  lazy val wcsVirtualHosts = SettingKey[Seq[Tuple2[String, String]]]("wcs-virtual-hosts", "WCS Virtual Host mapping")
  lazy val wcsWebappSatellite = SettingKey[String]("wcs-webapp-satellite", "WCS SatelliteServer Webapp CS Directory")

  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsCsdt = InputKey[Unit]("wcs-dt", "WCS Development Tool")
  lazy val wcsCm = InputKey[Unit]("wcs-cm", "WCS Catalog Mover")

  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy resources")
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  lazy val wcsUpdateAssets = TaskKey[String]("wcs-update-assets", "WCS update assets")

  // generate tag access classes from tld files
  val coreGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp, baseDirectory, wcsVersion) map {
      (dstDir, srcDir, base, version) =>

        println("*** Generating tags from %s\n".format(srcDir))

        // generate tags
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"
        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val cls = Tld2Tag.tld2class(src)
          val dst = file(dstDir / cls + ".scala")
          val dstj = file(dstDir / cls + ".java")
          //if tld.getName.equalsIgnoreCase("asset.tld") // select only one for debug generator
        } yield {
          if (!dst.exists) {
            val body = Tld2Tag(src)
            IO.write(dst, body)
            //print(cls + " ")
            println("+++ " + dst)
          }
          if (!dstj.exists) {
            val bodyj = Tld2Tagj(src)
            IO.write(dstj, bodyj)
            println("+++ " + dstj)
          }
          dst :: dstj :: Nil
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
        l.toSeq.flatten ++ ll.toSeq
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
          val cmd = Array(url + "/ContentServer",
            "username=" + user,
            "password=" + password,
            "cmd=" + (if (args.size > 0) args(0) else "listcs"),
            "fromSites=" + sites,
            "datastore=" + sites + "-" + version,
            "resources=" + (if (args.size > 1) args(1)
            else if (args.size == 0) "@ALL_ASSETS"
            else if (args.size >= 1) args(0) match {
              case "listcs" => "@ALL_ASSETS;@ALL_NONASSETS"
              case "listds" => "@ALL_ASSETS;@ALL_NONASSETS"
              case "import" =>
                println("""importing only sites - you need to run after:
> wcs-dt import @ASSET_TYPE
> wcs-dt import @ALL_ASSETS
> wcs-dt import @STARTMENU
> wcs-dt import @TREETAB""")
                "@SITE"
              case "export" =>
                println("exporting only sites & assets types - you need to export manually assets and non-assets")
                "@SITE;@ASSET_TYPE"
            }))

          //println(cmd.mkString("java -cp "+seljars.mkString(":")+" com.fatwire.csdt.client.main.CSDT ", " ", ""))
          Run.run("com.fatwire.csdt.client.main.CSDT",
            seljars, cmd, s.log)(runner)
      }
  }

  // interface to catalogmover from sbt
  val wcsCmTask = wcsCm <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, version, httpUrl, sites, user, password, classpath, s, runner) =>
          val seljars = classpath.files

          //val cmd = args
          //Run.run("COM.FutureTense.Apps.CatalogMover", seljars, cmd, s.log)(runner)

          def help = println("""Usage: wcs-cm <function> [<options>]
 <function> to perform (import, import_all, export, export_all) 
 <options>::  -f <file to import>
  -t <catalog name> (can be repeated, export only)
                """)

          if (args.size == 0) {
            help
          } else {

            //val url = new java.net.URL(httpUrl)
            //val host = url.getProtocol + "://" + url.getHost+":"+url.getPort
            //val path = url.getPath+ "CatalogManager"
            
            val url = httpUrl + "/CatalogManager"

            //println(url)
            
            val cp = classpath.files.mkString(java.io.File.pathSeparator)
            val dir = file("export") / "Populate-" + version
            val cmd = Seq("-cp", cp, "COM.FutureTense.Apps.CatalogMover")

            val opts = Seq("-u", user, "-p", password, "-b", url, "-d", dir.toString, "-x")
            val all = cmd ++ opts ++ args

            //println(all)
            //for(file <- classpath.files) println(file)
            //println(opts++args)

            Fork.java(None, all, Some(new java.io.File(".")), s.log)

          }
      }
  }

  // package jar task - build the jar and copy it  to destination 
  val wcsPackageJarTask = wcsPackageJar <<=
    (assembly, wcsShared) map {
      (jar, shared) =>

        val destdir = file(shared) / "agilewcs"
        val destjar = file(shared) / "agilewcs" / jar.getName

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
        recursiveCopy(src, file(tgt))(!isHtml(_))
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
        val deployer = new AgileWcsDeployer(url, sites, user, pass)
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

    // shift the url assembler to add agilewcs as the first
    if (prp.getProperty("uri.assembler.1.shortform") != "agilewcs") {

      val p1s = prp.getProperty("uri.assembler.1.shortform")
      val p1c = prp.getProperty("uri.assembler.1.classname")
      val p2s = prp.getProperty("uri.assembler.2.shortform")
      val p2c = prp.getProperty("uri.assembler.2.classname")

      prp.setProperty("uri.assembler.3.shortform", p2s)
      prp.setProperty("uri.assembler.3.classname", p2c)
      prp.setProperty("uri.assembler.2.shortform", p1s)
      prp.setProperty("uri.assembler.2.classname", p1c)
      prp.setProperty("uri.assembler.1.shortform", "agilewcs")
      prp.setProperty("uri.assembler.1.classname", "wcs.core.Assembler")
    }

    prp.setProperty("agilewcs.sites", sites)
    for ((k, v) <- sitesSeq) {
      prp.setProperty("agilewcs." + normalizeSiteName(k), v)
    }

    prp.setProperty("agilewcs.blob.flex", flexBlobs)
    prp.setProperty("agilewcs.blob.static", staticBlobs)

    // store
    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileWCS setup")
  }

  // configure futurentense.ini
  def setupFutureTenseIni(home: String, static: String, appjar: String, sites: String, version: String) {

    val prpFile = file(home) / "futuretense.ini"
    val prp = new java.util.Properties
    prp.load(new java.io.FileReader(prpFile))

    prp.setProperty("agilewcs.sites", sites);
    prp.setProperty("agilewcs.jar", appjar);
    prp.setProperty("agilewcs.static", file(static).getAbsolutePath);

    prp.setProperty("cs.csdtfolder", file("export").getAbsolutePath)
    prp.setProperty("cs.pgexportfolder", (file("export") / "xmlpub" / (sites + "-" + version)).getAbsolutePath)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "updated by AgileWCS setup")

  }

  def setupAgileWcsPrp(dir: String, sites: String, static: String, appjar: String, flexBlobs: String, staticBlobs: String) {
    val prpFile = file(dir) / "WEB-INF" / "classes" / "agilewcs.properties"
    val prp = new java.util.Properties
   
    if(prpFile.exists)
      prpFile.delete
    prp.setProperty("agilewcs.sites", sites);
    prp.setProperty("agilewcs.jar", appjar);
    prp.setProperty("agilewcs.static", file(static).getAbsolutePath);
    prp.setProperty("agilewcs.blob.flex", flexBlobs)
    prp.setProperty("agilewcs.blob.static", staticBlobs)

    println("~ " + prpFile)
    prp.store(new java.io.FileWriter(prpFile),
      "created by AgileWCS setup")

    //otherConfig.setProperty("agilewcs.user", username);
    //otherConfig.setProperty("agilewcs.password", password);
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
      (AgileWcsBuild.addFilterSetup accept _)

    // jars to remove when performing a setup

    val removeJars = destlib.listFiles filter
      (AgileWcsBuild.removeFilterSetup accept _)

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
  val wcsSetupTask = wcsSetup <<= inputTask {
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

            if (args.length == 1 && args(0) == "satellite") {

              println("*** Installing AgileWCS for Satellite Server. ***");

              setupCopyJars(satelliteWebapp, classpath.files)

              setupServletRequest(satelliteWebapp, sites, virtualHosts, flexBlobs, staticBlobs)

              setupAgileWcsPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs)

              println("*** Installed AgileWCS for Satellite. You need to restart the application server or (if weblogic) redeploy the webapp Satellite. ***")

            } else {

              println("*** Installing AgileWCS ***");

              val vhosts = (sites split ",") map { site =>
                (site, url + "/Satellite/" + normalizeSiteName(site))
              } toSeq

              setupMkdirs(shared, version, sites)

              setupCopyJars(webapp, classpath.files)

              setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)

              setupAgileWcsPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs)

              setupFutureTenseIni(home, static, appjar, sites, version)

              println("*** Installed AgileWCS. You need to restart the application server or (if weblogic) redeploy the webapp ContentServer,  then execute \"wcs-deploy\" ***")

            }

        }
  }

}
