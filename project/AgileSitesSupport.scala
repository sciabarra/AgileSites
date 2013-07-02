package wcs.build

import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import Dialog._
import xsbt.api.Discovery

trait AgileSitesSupport extends Utils {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsShared = SettingKey[String]("wcs-shared", "WCS Shared Directory")
  lazy val wcsVersion = SettingKey[String]("wcs-version", "WCS or Fatwire Version")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS ContentServer Webapp CS Directory")

  lazy val wcsUrl = SettingKey[String]("wcs-url", "WCS URL")
  lazy val wcsUser = SettingKey[String]("wcs-user", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-password", "WCS Site password")

  lazy val wcsSites = SettingKey[String]("wcs-sites", "WCS Sites to configure")

  lazy val wcsFlexBlobs = SettingKey[String]("wcs-flex-blobs", "WCS Flex Blobs Regexp")
  lazy val wcsStaticBlobs = SettingKey[String]("wcs-static-blobs", "WCS Static Blobs Regexp")
  lazy val wcsWebappSatellite = SettingKey[String]("wcs-webapp-satellite", "WCS SatelliteServer Webapp CS Directory")
  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsVirtualHosts = SettingKey[Seq[Tuple2[String, String]]]("wcs-virtual-hosts", "WCS Virtual Host mapping")
  lazy val wcsVirtualHostsTask = wcsVirtualHosts := Seq[Tuple2[String, String]]();

  // generate tag access classes from tld files
  val coreGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp, baseDirectory, wcsVersion) map {
      (dstDir, srcDir, base, version) =>

        // generate tags
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"

        if (!tlds.isDirectory)
          throw new RuntimeException("not found files in " + tlds)

        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
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

  // interface to csdt from sbt
  val wcsCsdtTask = InputKey[Unit]("wcs-dt", "WCS Development Tool") <<= inputTask {
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

  // hello
  val wcsHello = TaskKey[Option[String]]("wcs-hello", "WCS Hello")
  val wcsHelloTask = wcsHello <<= (wcsUrl) map {
    (url) =>
      try {
        val res = httpCallRaw(url + "/HelloCS")
        var re = """(?s).*java\.version=([\w\.-_]+).*""".r
        res match {
          case re(ver) =>
            if (System.getProperty("java.version") != ver) {
              println("""*** Sites use a different version of Java than the shell
      	                 |*** Please use the same setting JAVA_HOME before starting the shell
      	                 |***""".stripMargin)
              None
            } else {
              println("Sites running with java " + ver)
              Some(ver)
            }
          case _ =>
            println("Sites running")
            //println(" no match ")
            Some("unknown")
        }
      } catch {
        case ex =>
          println("Sites NOT running")
          None
      }
  }

  // deploy sites
  val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS deploy sites")
  val wcsDeployTask = wcsDeploy <<= (wcsUrl, wcsUser, wcsPassword, wcsSites) map {
    (url, user, pass, sites) =>
      println(httpCall("Setup", "&sites=" + sites, url, user, pass))
  }

  // drop sites
  val wcsDropSites = TaskKey[Unit]("wcs-drop-sites", "WCS drop existing sites")
  val wcsDropSitesTask = wcsDropSites <<= (wcsUrl, wcsUser, wcsPassword, wcsSites) map {
    (url, user, pass, sites) =>
  }

  // package jar task - build the jar and copy it  to destination 
  val wcsPopulate = TaskKey[Unit]("wcs-populate", "WCS catalog manager import")
  val wcsPopulateTask = wcsPopulate <<=
    (wcsHello, wcsUser, wcsPassword, wcsUrl, fullClasspath in Compile, streams) map {
      (hello, user, pass, url, classpath, s) =>
        if (hello.isEmpty)
          throw new Exception("Please start Sites before importing")
        val cp = (Seq(file("bin").getAbsoluteFile) ++ classpath.files).mkString(java.io.File.pathSeparator)
        val dir = file("core") / "populate"
        println(dir.getAbsolutePath)
        val cmd = Seq("-cp", cp, "COM.FutureTense.Apps.CatalogMover")
        val opts = Seq("-u", user, "-p", pass, "-b", url + "/CatalogManager", "-d", dir.getAbsolutePath, "-x")
        val all = cmd ++ opts ++ Seq("import_all")
        Fork.java(None, all, Some(new java.io.File(".")), s.log)
        ()
    }

  // interface to catalogmover from sbt
  val wcsSetupOnlineTask = InputKey[Unit]("wcs-setup-online", "WCS Setup Online") <<= inputTask {
    (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword,
        wcsPopulate, wcsPackageJar, wcsCopyStatic,
        fullClasspath in Compile, streams, runner) map {
          (args, version, url, sites, user, pass, ignore, jar, count, classpath, s, runner) =>
            val cmd = if (args.length == 1 && args(0) == "drop") {
              "&drop=%s&sites=%s".format(args.tail.mkString(","), sites)
            } else {
              "&sites=" + sites
            }
            println(httpCall("Setup", cmd, url, user, pass))
        }
  }

  // package jar task - build the jar and copy it  to destination 
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")

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

  // copy resources from the app to the webapp task
  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy static resources")

  val wcsCopyStaticTask = wcsCopyStatic <<=
    (baseDirectory, wcsWebapp) map {
      (base, tgt) =>
        val src = base / "static"
        recursiveCopy(src, file(tgt), true)(x => true)
    }

  // copy html to destination
  val wcsCopyHtmlTask =
    (resourceGenerators in Compile) <+=
      (baseDirectory, resourceManaged in Compile) map {
        (base, dstDir) =>
          val srcDir = base / ".." / "static"
          //println("*** " + srcDir)
          recursiveCopy(srcDir, dstDir, true)(isHtml)
      }

  // generate index classes from sources
  val wcsGenerateIndexTask =
    (resourceGenerators in Compile) <+=
      (compile in Compile, resourceManaged in Compile) map {
        (analysis, dstDir) =>

          //println(analysis.apis.internal)

          // TODO COMPLETE

          /*
	       val all = Discovery.applications(Tests.allDefs(analysis)) 
	       for(item <- all) {
	          println(item)
	       }
          */
          //println("!!! " + compiled.apis.allInternalSources)
          //for(src <- compiled.apis.allInternalSources) {
          //  println(src)
          //}
          //println(compiled.relations.classes)

          //for(src <- compiled.relations.classes) 
          //  println(src)

          Seq[File]()
      }

  // setup offline task
  lazy val wcsSetupSatellite = TaskKey[Unit]("wcs-setup-satellite", "WCS Setup Satellte")

  val wcsSetupSatelliteTask = wcsSetupSatellite <<=
    (wcsPackageJar, managedClasspath in Runtime, classDirectory in Compile,
      wcsSites, wcsVersion, wcsHome, wcsShared, wcsUrl,
      wcsWebappSatellite, wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts) map {
        (appjar, classpath, classes,
        sites, version, home, shared, url,
        satelliteWebapp, flexBlobs, staticBlobs, virtualHosts) =>

          println("*** Installing AgileSites for Satellite  ***");

          setupCopyJars(satelliteWebapp, classpath.files)
          setupServletRequest(satelliteWebapp, sites, virtualHosts, flexBlobs, staticBlobs)

          println("**** Please restart Satellite ****")
      }

  // setup offline task
  lazy val wcsSetupOffline = TaskKey[Unit]("wcs-setup-offline", "WCS Setup Offline")

  val wcsSetupOfflineTask = wcsSetupOffline <<=
    (wcsHello, wcsPackageJar, managedClasspath in Runtime, classDirectory in Compile,
      wcsSites, wcsVersion, wcsHome, wcsShared, wcsWebapp, wcsUrl, wcsFlexBlobs, wcsStaticBlobs) map {
        (hello, appjar, classpath, classes, sites, version, home, shared, webapp, url, flexBlobs, staticBlobs) =>

          if (!hello.isEmpty)
            throw new Exception("Please stop Sites before doing offline setup")

          println("*** Installing AgileSites for ContentServer ***");

          val static = (file(shared) / "Storage" / "Static") getAbsolutePath

          val vhosts = (sites split ",") map { site =>
            (site, url + "/Satellite/" + normalizeSiteName(site))
          } toSeq

          setupMkdirs(shared, version, sites)
          setupCopyJars(webapp, classpath.files)
          setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)
          setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs)
          setupFutureTenseIni(home, static, appjar, sites, version)

        //setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs) //not used for now

      }

  // command line log control
  lazy val wcsLog = InputKey[Unit]("wcs-log", "WCS log manager")

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
            println("""
                  |usage:
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
              Some("&op=start&level=DEBUG&%s" format parse(Nil))
            case "stop" :: rest =>
              Some("&op=stop&%s" format parse(rest))
            case "list" :: rest =>
              Some("&op=list")
            case level :: rest =>
              if ("ERROR|WARN|DEBUG|INFO|TRACE".r findAllIn level.toUpperCase isEmpty) {
                println("Invalid level " + level)
                None
              } else
                Some("&op=start&level=%s&%s" format (level.toUpperCase, parse(rest)))
            case _ =>
              usage
              None
          }

          if (cmd.isDefined)
            println(httpCall("Log", cmd.get, url, user, pass))
      }
  }

  lazy val wcsImport = InputKey[Unit]("wcs-import", "WCS import")

  val wcsImportTask = wcsImport <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsUrl, wcsUser, wcsPassword, wcsSites, streams) map {
        (args, url, user, pass, sites, streams) =>

          val imp = ImportXml(url, sites, user, pass)

          val (collected, uncollected) = imp.orderedDeps

          imp.importAssets(collected)

      }
  }

  lazy val wcsExport = InputKey[Unit]("wcs-export", "WCS export")

  val wcsExportTask = wcsExport <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsUrl, wcsUser, wcsPassword, wcsSites, streams) map {
        (args, url, user, pass, sites, streams) =>
          val out = httpCall("Export", "", url, user, pass, sites)
          println(out)
      }
  }

}
