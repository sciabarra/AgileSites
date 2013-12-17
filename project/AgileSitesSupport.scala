package wcs.build

import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._
import Dialog._

trait AgileSitesSupport extends Utils {

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
  val wcsVirtualHostsTask = wcsVirtualHosts := Seq[Tuple2[String, String]]();

  lazy val wcsFlexBlobs = SettingKey[String]("wcs-flex-blobs", "WCS Flex Blobs Regexp")
  lazy val wcsStaticBlobs = SettingKey[String]("wcs-static-blobs", "WCS Static Blobs Regexp")

  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  

  // generate tag access classes from tld files
  val coreGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp, baseDirectory, wcsVersion) map {
      (dstDir, srcDir, base, version) =>

        // generate tags
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"
          
        val l = if(tlds.isDirectory) for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val clsj = Tld2Tagj.tld2class(src)
          val dstj = file(dstDir / clsj + ".java")
          //if tld.getName.equalsIgnoreCase("asset.tld") // select only one for debug generator
        } yield {
          if (!dstj.exists) {
            val bodyj = Tld2Tagj(src)
            IO.write(dstj, bodyj)
            println("+++ " + dstj)
          }
          dstj
        } else Array[File]()
        
        // copy versioned class
        val src = base / "src" / "main" / "version" / version
        if(!src.isDirectory)
          throw new RuntimeException("wrong path in build.sbt or unsupported version ")
        
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
  lazy val wcsCsdt = InputKey[Unit]("wcs-dt", "WCS Development Tool")
  val wcsCsdtTask = wcsCsdt <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, version, url, sites, user, password, classpath, s, runner) =>
          val re = "^(cas-client-core-\\d|csdt-client-\\d|rest-api-\\d|wem-sso-api-\\d|wem-sso-api-cas-\\d|spring-\\d|commons-logging-|servlet-api|sites-security|esapi-|cs-|http(client|core|mime)-).*.jar$".r;
          val seljars = classpath.files.filter(f => !re.findAllIn(f.getName).isEmpty)

          val workspaces = (file("export") / "envision").listFiles.filter(_.isDirectory).map(_.getName)
          val workspaceSearch = (("#" + defaultWorkspace(sites)) +: args).reverse.filter(_.startsWith("#")).head.substring(1)
          val workspace = workspaces.filter(_.indexOf(workspaceSearch) != -1)

          if (args.size == 0) {
            println("""usage: wcs-dt  [<cmd>]  [<selector> ...] [#<workspace>]
                       | <workspace> can be a substring of available workspaces,
                       |   available workspaces are: %s
                       | <cmd> defaults to 'listcs'
                       |   must be one of 'listcs', 'listds', 'import', 'export', 
                       | <selector> check developer tool documentation for syntax
                       |  defaults for commands are
                       |      listcs: @ALL_ASSETS
                       |      listds: @ALL_ASSETS
                       |      import: @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB
                       |      export: @SITE @ASSET_TYPE
                       |""".stripMargin.format(workspaces.mkString("'", "', '", "'")))
          } else if (workspace.size == 0)
            println("workspace " + workspaceSearch + " not found")
          else if (workspace.size > 1)
            println("workspace " + workspaceSearch + " is ambigous")
          else {

            val args1 = args.filter(!_.startsWith("#"))
            val firstArg = if (args1.size > 0) args1(0) else "listcs"
            val resources = if (args1.size > 1) args1.drop(1)
            else firstArg match {
              case "listcs" => Seq("@ALL_ASSETS")
              case "listds" => Seq("@ALL_ASSETS")
              case "import" =>
                Seq("@SITE", "@ASSET_TYPE", "@ALL_ASSETS", "@STARTMENU", "@TREETAB")
              case "export" =>
                Seq("@SITE", "@ASSET_TYPE")
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
                //"fromSites=" + sites,
                "datastore=" + workspace.head)

              println(seljars.mkString("\n"))

              Run.run("com.fatwire.csdt.client.main.CSDT", seljars, cmd, s.log)(runner)
            }
          }
      }
  }

  lazy val wcsCopyJarsOnline = TaskKey[Unit]("wcs-copyjars-online", "WCS Copy Jars Offline")
  val wcsCopyJarsOnlineTask = wcsCopyJarsOnline <<= 
    (fullClasspath in Compile, wcsShared) map {
      (classpath, shared) =>
            for(a <- classpath) println(a)
            setupCopyJarsOnline(shared, classpath.files)
  }

  lazy val wcsSetupOnline = InputKey[Unit]("wcs-setup-online", "WCS Setup Online")
  val wcsSetupOnlineTask = wcsSetupOnline <<= inputTask {
    (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, wcsCopyJarsOnline, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, 
        fullClasspath in Compile, wcsWebapp, wcsShared, streams, runner) map {
        (args, _, version, httpUrl, sites, user, password, 
          classpath, webapp, shared, s, runner) =>
          
          val seljars = classpath.files
          val url = httpUrl + "/CatalogManager"

          //println(url)

          val files = Seq(file("bin").getAbsoluteFile) ++ classpath.files
          val owasp = "-Dorg.owasp.esapi.resources=" + (file(webapp) / "WEB-INF" / "classes").getAbsolutePath
          val cp = files.mkString(java.io.File.pathSeparator)
          val dir = file("export") / "Populate"
          val cmd = Seq("-cp", cp, owasp, /*"-Dlog4j.debug",*/ "COM.FutureTense.Apps.CatalogMover")
          val opts = Seq("-u", user, "-p", password, "-b", url, "-d", dir.toString, "-x")
          val all = cmd ++ opts ++ Seq("import_all")
          if (args.length == 0) messageDialog("Ensure the application server is UP and RUNNING, then press OK")
          Fork.java(None, all, Some(new java.io.File(".")), s.log)

          if (args.length == 0) messageDialog("Setup Complete.\nYou can now create site and templates.\nYou need to deploy them with \"wcs-deploy\".")

        //println(all.mkString(" "))
      }
  }

  /// note the same name for package and assembly
  /// but assembly used in top level, package used in subprojects
 
  // package jar task - build the jar and copy it  to destination 
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  val wcsAssemblyJarTask = wcsAssemblyJar <<=
    (assembly, wcsShared) map {
      (jar, shared) =>

        val destdir = file(shared) / "agilesites"
        val destjar = file(shared) / "agilesites" / jar.getName

        destdir.mkdir
        IO.copyFile(jar, destjar)
        println("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  // package jar task - build the jar and copy it  to destination 
  lazy val wcsAssemblyJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  val wcsPackageJarTask = wcsPackageJar <<=
    (Keys.`package` in Compile, wcsShared) map {
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



  // copy statics
  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy static resources")
  val wcsCopyStaticTask = wcsCopyStatic <<=
    (baseDirectory, wcsWebapp) map {
      (base, tgt) =>
        val src = base / "app" / "src" / "main" / "static"
        recursiveCopy(src, file(tgt))(x => true)
    }


  val wcsCopyHtmlTask = (resourceGenerators in Compile) <+=
      (baseDirectory, resourceManaged in Compile) map {
        (base, dstDir) =>
          val srcDir = base / "src" / "main" / "static"
          println("*** " + srcDir)
          recursiveCopy(srcDir, dstDir)(isHtml)
      }

  // generate index classes from sources
  val wcsGenerateIndexTask =
    (resourceGenerators in Compile) <+=
      (compile in Compile, resourceManaged in Compile) map {
        (analysis, dstDir) =>


          val groupIndexed =
            analysis.apis.allInternalSources. // all the sources
              map(extractClassAndIndex(_)). // list of Some(index, class) or Nome
              flatMap(x => x). // remove None
              groupBy(_._1). // group by (index, (index, List(class)) 
              map { x => (x._1, x._2 map (_._2)) }; // lift to (index, List(class))

          //println(groupIndexed)

          val l = for ((subfile, lines) <- groupIndexed) yield {
            val file = dstDir / subfile
            val body = lines mkString ("# generated - do not edit\n", "\n", "\n# by AgileSites build\n")
            writeFile(file, body)
            file
          }

          l.toSeq
      }

  // copy resources to the webapp task
  lazy val wcsUpdateAssets = TaskKey[String]("wcs-update-assets", "WCS update assets")
  val wcsUpdateAssetsTask = wcsUpdateAssets <<=
    (wcsUrl, wcsSites, wcsUser, wcsPassword, wcsPackageJar) map {
      (url, sites, user, pass, _) =>
        val deployer = new AgileSitesDeployer(url, sites, user, pass)
        println(deployer.deploy())
        deployer.getStatus
    }

  // deploy task  
  lazy val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS Deploy")
  val wcsDeployTask = wcsDeploy <<=
    (wcsCopyStatic, wcsUpdateAssets) map { (count, update) => () }


  lazy val wcsCopyJarsOffline = TaskKey[Unit]("wcs-copyjars-offline", "WCS Copy Jars Offline")
  val wcsCopyJarsOfflineTask = wcsCopyJarsOffline <<= 
    (managedClasspath in Runtime, wcsWebapp) map {
      (classpath, webapp) =>
            setupCopyJarsOffline(webapp, classpath.files)
  }

  // setup offline task
  lazy val wcsSetupOffline = InputKey[Unit]("wcs-setup-offline", "WCS Setup Offline")
  val wcsSetupOfflineTask = wcsSetupOffline <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask,
        wcsCopyJarsOffline, classDirectory in Compile,
        wcsSites, wcsVersion, wcsHome, wcsShared, wcsWebapp, wcsUrl,
         wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts) map {
          (args, _, classes,
           sites, version, home, shared, webapp, url,
           flexBlobs, staticBlobs, virtualHosts) =>

            val static = (file(shared) / "Storage" / "Static") getAbsolutePath

            val silent = (args.length > 0 && args(args.length - 1) == "silent")

            if (!silent)
              messageDialog("Ensure the application server is NOT RUNNING, then press OK")

            println("*** Installing AgileSites for ContentServer ***");

            val vhosts = (sites split ",") map { site =>
              (site, url + "/Satellite/" + normalizeSiteName(site))
            } toSeq

            setupMkdirs(shared, version, sites)
            setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)
            setupAgileSitesPrp(webapp, shared, sites, static, flexBlobs, staticBlobs)
            setupFutureTenseIni(home, shared, static,  sites, version)

            if (!silent)
              messageDialog("""Installation Complete.
                |\nPlease restart your application server.
                |\nYou need to complete with "wcs-setup-online".""".stripMargin)
        }
  }




  // setup task
  lazy val wcsSetupSatellite = InputKey[Unit]("wcs-setup-satellite", "WCS Setup Satellite Offline")
  val wcsSetupSatelliteTask = wcsSetupSatellite <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsCopyJarsOffline, classDirectory in Compile,
       wcsSites, wcsVersion, wcsWebapp,
       wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts) map {
      (args, _, classes,
       sites, version, webapp,
       flexBlobs, staticBlobs, virtualHosts) =>

            val silent = (args.length > 0 && args(args.length - 1) == "silent")

            if (!silent)
              messageDialog("Ensure the application server is NOT RUNNING, then press OK")

            println("*** Installing AgileSites for Satellite  ***");

            setupServletRequest(webapp, sites, virtualHosts, flexBlobs, staticBlobs)

            //setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs) //not used for now

            if (!silent)
              messageDialog("Installation Complete. Please restart your application server.")

        }
  }


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
