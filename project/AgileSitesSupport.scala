package wcs.build

import sbt._
import Keys._
import Dialog._

import sbtassembly.Plugin._
import AssemblyKeys._

trait AgileSitesSupport extends AgileSitesUtil {

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
  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsVirtualHosts = SettingKey[Seq[Tuple2[String, String]]]("wcs-virtual-hosts", "WCS Virtual Host mapping")
  lazy val wcsVirtualHostsTask = wcsVirtualHosts := Seq[Tuple2[String, String]]()
  
  // the satellite webapp defaults to a sister /cs webapp named /ss).getParentFile / "ss").getAbsolutePath }
  lazy val wcsWebappSatellite = TaskKey[String]("wcs-webapp-satellite", "WCS Satellite Webapp")
  val wcsWebappSatelliteTask = wcsWebappSatellite <<= (wcsWebapp) map {
    (x) => (file(x).getParentFile / "ss").getAbsolutePath 
  }

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
          // if tld.getName.equalsIgnoreCase("asset.tld") // select only one for debug generator
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
      (argTask, wcsHome, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, home, version, url, sites, user, password, classpath, s, runner) =>

          val re = "^(cas-client-core-\\d|csdt-client-\\d|rest-api-\\d|wem-sso-api-\\d|wem-sso-api-cas-\\d|spring-\\d|commons-logging-|servlet-api|sites-security|esapi-|cs-|http(client|core|mime)-).*.jar$".r;
          val seljars = classpath.files.filter(f => !re.findAllIn(f.getName).isEmpty)
          
          val workspaces = (file("export") / "envision").listFiles.filter(_.isDirectory).map(_.getName)
          val workspaceSearch = ("#cs_workspace#" +: args).reverse.filter(_.startsWith("#")).head.substring(1)
          
          val workspace = if(!workspaceSearch.endsWith("#"))
              workspaces.filter( _.indexOf(workspaceSearch) != -1) 
          else workspaces.filter(_ == workspaceSearch.init) 

          val defaultSite = sites.split(",").head

          val fromSites = (( "!" + defaultSite) +: args).reverse.filter(_.startsWith("!")).head.substring(1)
          val toSites = (( "^" + fromSites) +: args).reverse.filter(_.startsWith("^")).head.substring(1)

          //println(workspaceSearch)
          //println(workspace.mkString(" "))

          if(args.size >0 && args(0) == "raw") {
              Run.run("com.fatwire.csdt.client.main.CSDT", 
                       seljars, args.drop(1), s.log)(runner)

          } else if(args.size == 0) {
            println("""usage:wcs-dt [<cmd>]  [<selector> ...] [#<workspace>[#]] [!<from-sites>] [^<to-sites>]
                       | <workspace> is a substring of available workspaces, use #workspace# for an exact match
                       |   default workspace is: cs_workspace
                       |   available workspaces are: %s
                       | <from-sites> and <to-sites> is a comma separated list of sites defined, 
                       |   <from-sites> defaults to <workspace>, <to-sites> default to <from-sites> 
                       | <cmd> is one of 'listcs', 'listds', 'import', 'export', 'mkws'
                       |    defaults to 'listcs'
                       | <selector> check developer tool documentation for complete syntax
                       |    you can use <AssetType>[:<id>] or a special form,
                       |    the special form are
                       |      @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB
                       |  and also additional @ALL for all of them
                       |""".stripMargin.format(workspaces.mkString("'", "', '", "'")))
          } else if (workspace.size == 0)
            println("workspace " + workspaceSearch + " not found - create it with mkws <workspace>")
          else if (workspace.size > 1)
            println("workspace " + workspaceSearch + " is ambigous")
          else {

            def processArgs(args: Seq[String]) = {
                if(args.size == 0 || args.size==1) {
                  println("""please specify what you want to export or use @ALL to export all
                       | you can use <AssetType>[:<id>] or a special form,
                       | the special form are
                       |   @SITE @ASSET_TYPE @ALL_ASSETS @STARTMENU @TREETAB @ROLE
                       |  and also additional @ALL meaning  all of them""".stripMargin)
                  Seq()
                } else if(args.size == 2 && args(1) == "@ALL") {
                  Seq("@SITE", "@ASSET_TYPE", "@ALL_ASSETS", "@STARTMENU", "@TREETAB", "@ROLE")
                } else {
                  args.drop(1)
                }
            }

            val args1 = args.filter(!_.startsWith("#")).filter(!_.startsWith("!")).filter(!_.startsWith("^"))
            val firstArg = if (args1.size > 0) args1(0) else "listcs"
            val resources = firstArg match {
              case "listcs" => processArgs(args1)
              case "listds" => processArgs(args1)
              case "import" => processArgs(args1)
              case "export" => processArgs(args1)
              case "mkws" =>
                if(args1.size==1) {
                 println("please specify workspace name")
                } else {
                  val ws = file("export") / "envision" / args1(1)
                  if(ws.exists)
                    println("nothing to do - workspace "+args1(1)+" exists")
                  else {
                    ws.mkdirs
                    if(ws.exists)
                      println(" workspace "+args1(1)+" created")
                    else 
                      println("cannot create workspace "+args1(1))
                  } 
                }
                Seq()
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
                "fromSites=" + fromSites,
                "toSites=" + toSites,
                "datastore=" + workspace.head)

              s.log.debug(seljars.mkString("\n"))
              //s.log.debug(cmd.mkString(" "))
              Run.run("com.fatwire.csdt.client.main.CSDT", seljars, cmd, s.log)(runner)
            }
          }
      }
  }

  // hello
  val wcsHello = TaskKey[Option[String]]("wcs-hello", "WCS Hello")
  val wcsHelloTask = wcsHello <<= (wcsUrl) map {
    (url) => 
      try {
        val res = httpCallRaw(url + "/HelloCS")
        var re = """(?s).*java\.version=([\w\.\-_]+).*""".r
        res match {
          case re(ver) =>
            val prp = System.getProperty("java.version") 
            if (prp != ver) {
              println("""*** WebCenter Sites use java %s and AgileSites uses java %s
                         |*** They are different versions of Java.
      	                 |*** Please set JAVA_HOME and use the same java version for both
      	                 |***""".format(prp,ver).stripMargin)
              None
            } else {
              println("WebCenter Sites running with java " + ver)
              Some(ver)
            }
          case _ =>
            println("WebCenter Sites running")
            //println(" no match ")
            Some("unknown")
        }
      } catch {
        case ex =>
          println("WebCenter Sites NOT running")
          None
      }
  }

  lazy val wcsCatalogManager = InputKey[Unit]("wcs-cm", "WCS Catalog Manager")
  val wcsCatalogManagerTask = wcsCatalogManager <<= inputTask {
    (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, wcsUrl,  wcsUser, wcsPassword, fullClasspath in Compile, streams) map {
        (args,  url, user, pass, classpath, s) =>
          catalogManager(url, user, pass, classpath.files, args, s.log)    
      }
  }


  lazy val wcsPopulate = TaskKey[Unit]("wcs-populate", "WCS Catalog Manager Populate")
  val wcsPopulateTask = wcsPopulate <<= 
     (wcsHello, wcsHome, wcsUrl,  wcsUser, wcsPassword, fullClasspath in Compile, streams) map {
        (hello, home, url, user, pass, classpath, s) =>
          if(hello.isEmpty)
            throw new Exception("Web Center Sites must be online.")
          catalogManager(url, user, pass, classpath.files, Seq("import_all"), s.log)
          ( file(home) / "populate.done" ).createNewFile
  }


  /// note the same name for package and assembly
  /// but assembly used in top level, package used in subprojects
 
  // package jar task - build the jar and copy it  to destination 
  lazy val wcsAssemblyJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  val wcsAssemblyJarTask = wcsAssemblyJar <<=
    (assembly, wcsShared, streams) map {
      (jar, shared, s) =>

        val destdir = file(shared) / "agilesites"
        val destjar = file(shared) / "agilesites" / jar.getName

        destdir.mkdir
        IO.copyFile(jar, destjar)
        s.log.info("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  // package jar task - build the jar and copy it  to destination 
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  val wcsPackageJarTask = wcsPackageJar <<=
    (Keys.`package` in Compile, wcsShared, streams) map {
      (jar, shared, s) =>

        val destdir = file(shared) / "agilesites"
        val destjar = file(shared) / "agilesites" / jar.getName

        destdir.mkdir
        IO.copyFile(jar, destjar)
        s.log.info("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  // copy statics
  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy static resources")
  val wcsCopyStaticTask = wcsCopyStatic <<=
    (baseDirectory, wcsWebapp, streams) map {
      (base, tgt, s) =>
        val src = base / "app" / "src" / "main" / "static"
        s.log.debug(" from"+src)
        val l = recursiveCopy(src, file(tgt), s.log)(x => true)
        println("*** copied "+(l.size)+" static files")
    }

  val wcsCopyHtmlTask = (resourceGenerators in Compile) <+=
      (baseDirectory, resourceManaged in Compile, streams) map {
        (base, dstDir, s) =>
          val srcDir = base / "src" / "main" / "static"
          s.log.debug("copyHtml from"+srcDir)
          recursiveCopy(srcDir, dstDir, s.log)(isHtml)
      }

  // generate index classes from sources
  val wcsGenerateIndexTask =
    (resourceGenerators in Compile) <+=
      (compile in Compile, resourceManaged in Compile, streams) map {
        (analysis, dstDir, s) =>
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
            writeFile(file, body, s.log)
            file
          }
          l.toSeq
      }

  // copy resources to the webapp task
  lazy val wcsUpdateAssets = TaskKey[Unit]("wcs-update-assets", "WCS update assets")
  val wcsUpdateAssetsTask = wcsUpdateAssets <<=
    (wcsPackageJar, wcsHome, wcsHello, fullClasspath in Compile, wcsUrl, wcsUser, wcsPassword, wcsSites, streams) map {
      (_, home, hello, classpath, url, user, pass, sites, s) =>
        // check wcs is online 
        if (hello.isEmpty)
          throw new Exception("WebCenter Site must be online.")
        // pupulate with support elements 
        val flag = file(home) / "populate.done"
        if(!flag.exists) {
          catalogManager(url, user, pass, classpath.files, Seq("import_all"), s.log)
          flag.createNewFile
        }    
        // deploy
        deploy(url, user, pass, sites)  
    }

  // deploy task  
  lazy val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS Deploy")
  val wcsDeployTask = wcsDeploy <<=
    (wcsCopyStatic, wcsUpdateAssets) map { (_, _) => () }

  // copy jars in the webapp lib folder
  lazy val wcsCopyJarsWeb = TaskKey[Unit]("wcs-copyjars-web", "WCS Copy Jars to WEB-INF/lib")
  val wcsCopyJarsWebTask = wcsCopyJarsWeb <<= 
    (wcsHello, fullClasspath in Compile, wcsWebapp, wcsVersion) map {
      (hello, classpath, webapp, version) =>
       if(!hello.isEmpty)
          throw new Exception("Web Center Sites must be offline.")
       setupCopyJarsWeb(webapp, classpath.files, version)
  }

  // copy jars in the agilesites lib 
  lazy val wcsCopyJarsLib = TaskKey[Unit]("wcs-copyjars-lib", "WCS Copy Jars to agilesites/lib")
  val wcsCopyJarsLibTask = wcsCopyJarsLib <<= 
    (wcsHello, fullClasspath in Compile, wcsShared) map {
      (hello, classpath, shared) =>
         if(!hello.isEmpty)
           throw new Exception("Web Center Sites must be offline.")
         setupCopyJarsLib(shared, classpath.files)
  }


  lazy val wcsSetup = TaskKey[Unit]("wcs-setup", "WCS Setup Offline")
  val wcsSetupTask = wcsSetup <<= 
      (wcsCopyJarsWeb, wcsCopyJarsLib, wcsCopyStatic, classDirectory in Compile,
        wcsSites, wcsVersion, wcsHome, wcsShared, wcsWebapp, wcsUrl,
         wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts, wcsPackageJar) map {
          (_, _, _, classes,
           sites, version, home, shared, webapp, url,
           flexBlobs, staticBlobs, virtualHosts, jar) =>

            val static = (file(shared) / "Storage" / "Static") getAbsolutePath

            // switch to hsql if needed
            val hsqlflag = (file(home) / "hsql.flag")
            if(hsqlflag.exists) {
              switchFutureTenseIni2Hsql(home)
              hsqlflag.delete
              println("*** switched to hsql ***")
            }

            println("*** Installing AgileSites for WebCenter Sites ***");

            val vhosts = (sites split ",") map { site =>
              (site, url + "/Satellite/" + normalizeSiteName(site))
            } toSeq

            setupMkdirs(shared, version, sites)
            setupServletRequest(webapp, sites, vhosts, flexBlobs, staticBlobs)
            setupAgileSitesPrp(webapp, shared, sites, static, flexBlobs, staticBlobs)
            setupFutureTenseIni(home, shared, static,  sites, version)

            // remove any other jar starting with agilesites-all-assembly 
            // remnants of the past
            for(f <- (file(shared) / "agilesites").listFiles) {
                if(f.isFile && f.getName.startsWith("agilesites-all-assembly")) {
                  //println("candidate to removal: "+f)
                  if(! f.getAbsolutePath.equals(jar)) {
                    f.delete
                    println("--- "+f);
                  } else {
                    //println("not removing "+jar)
                  }
                }
            }


            // remove pupulate mark if there
            ( file(home) / "populate.done" ).delete

            println("""**** Setup Complete.
                |**** Please restart your application server.
                |**** You need to complete installation with "wcs-deploy".""".stripMargin)
  }

  // setup task
  lazy val wcsSetupSatellite = TaskKey[Unit]("wcs-setup-satellite", "WCS Setup Satellite Offline")
  val wcsSetupSatelliteTask = wcsSetupSatellite <<= 
    (wcsCopyJarsWeb, classDirectory in Compile, wcsSites, wcsVersion, wcsWebappSatellite,
       wcsFlexBlobs, wcsStaticBlobs, wcsVirtualHosts) map {
      (_, classes, sites, version, webapp,
       flexBlobs, staticBlobs, virtualHosts) =>

            println("*** Installing AgileSites for WebCenter Sites Satellite ***");

            setupServletRequest(webapp, sites, virtualHosts, flexBlobs, staticBlobs)
            //setupAgileSitesPrp(webapp, sites, static, appjar, flexBlobs, staticBlobs) //not used for now
            println("*** Installation Complete. \n**** Please restart your satellite server.")
        
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


  lazy val wcsServe = InputKey[Unit]("wcs-serve", "Launch WebServer")
  val wcsServeTask = wcsServe <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
    (argsTask, fullClasspath in Compile, baseDirectory, wcsHome, wcsUrl, streams) map {
      (args, classpath, base, home, url, s) =>

        args.headOption match {

          case None => println("usage: start|stop|status")
          
          case Some("status") =>

            try {
              new java.net.ServerSocket(8182).close
              println("tomcat not running")
            } catch {
              case e: Throwable => 
                 //e.printStackTrace
                 println("tomcat running")
            }

          case Some("stop") =>
            try {
              println("*** stopping tomcat ***")
              def sock = new java.net.Socket("127.0.0.1",8182)
              sock.getInputStream.read
              sock.close
              println("*** stopped tomcat ***")
            } catch {
              case e: Throwable => 
                 // e.printStackTrace
                 println("tomcat not running")
            }           
 
          case Some("start") => 
   
            // start tomcat
            val tomcat = new Thread() {
              override def run() {
                 try {
                
                   println("*** tomcat starting in port 8181 ***")
                   //for(folder <- folders) println("*** -"+folder)  
                   val root = (base / "app" / "src" / "main" / "static").getAbsolutePath
                   val test = (base / "app" / "src" / "test" / "static").getAbsolutePath
                   val cs   = (base / "wcs" / "webapps" / "cs" ).getAbsolutePath
                   val cas  = (base / "wcs" / "webapps" / "cas").getAbsolutePath

                   val webapps = Seq("="+root, "test="+test, "cs="+cs, "cas="+cas)
                   val tomcatProcess = tomcatServe(8181, classpath.files, webapps)
 
                 } catch {
                   case e: Throwable => 
                    //e.printStackTrace
                    println("!!! tomcat already running")
                 }
              }
            }
            tomcat.start
 	          Thread.sleep(3000);
            // wait for cs startup if deployed
            if( (file("wcs") / "webapps" / "cs").isDirectory ) {
              println(" *** Waiting for CS startup complete ***")
              println(httpCallRaw(url + "/HelloCS"))            
            }
        case Some(thing) =>
            println("usage: start|stop|status")
        }
      }
    }
  
  }
