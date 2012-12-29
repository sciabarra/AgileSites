import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object AgileWcsSupport {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS Webapp CS Directory")
  lazy val wcsUser = SettingKey[String]("wcs-user", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-password", "WCS Site password")
  lazy val wcsUrl = SettingKey[String]("wcs-url", "WCS URL")
  lazy val wcsSites = SettingKey[String]("wcs-sites", "WCS Sites to configure")
  lazy val wcsVersion = SettingKey[String]("wcs-version", "WCS or Fatwire Version")

  lazy val wcsSetup = InputKey[Unit]("wcs-setup", "WCS Setup")
  lazy val wcsConfig = TaskKey[String]("wcs-config", "WCS Write configurations")

  lazy val wcsDeploy = TaskKey[Unit]("wcs-deploy", "WCS Deploy")
  lazy val wcsCopyStatic = TaskKey[Unit]("wcs-copy-static", "WCS copy resources")
  lazy val wcsPackageJar = TaskKey[String]("wcs-package-jar", "WCS package jar")
  lazy val wcsUpdateModel = TaskKey[String]("wcs-update-model", "WCS update content model")

  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsCsdt = InputKey[Unit]("wcs-dt", "WCS Development Tool")
  lazy val wcsCm = InputKey[Unit]("wcs-cm", "WCS Catalog Mover")

  // generate tag access classes from tld files
  val coreGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp, baseDirectory, wcsVersion) map {
      (dstDir, srcDir, base, version) =>
        //println(dstDir)

        // generate tags
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"
        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val cls = Tld2Tag.tld2class(src)
          val dst = file(dstDir / cls + ".scala")
          val dstj = file(dstDir / cls + ".java")
          //if tld.getName.equalsIgnoreCase("csmac.tld") // select only one for debug generator
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

  // copy files from a src dir to a taget dir recursively - not used for now
  def recursiveCopy(src: File, tgt: File) = {
    val nsrc = src.getPath.length
    val cplist = (src ** "*").get.filterNot(_.isDirectory) map {
      x =>
        val dest = tgt / x.getPath.substring(nsrc)
        println("+++ " + dest)
        (x, dest)
    }
    IO.copy(cplist)
    cplist.size
  }

  // interface to csdt from sbt
  val wcsCsdtTask = wcsCsdt <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsVersion, wcsUrl, wcsSites, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, version, url, sites, user, password, classpath, s, runner) =>
          val re = "^(cas-client-core-\\d|csdt-client-\\d|rest-api-\\d|wem-sso-api-\\d|wem-sso-api-cas-\\d|spring-\\d|commons-logging-|servlet-api).*.jar$".r;
          val seljars = classpath.files.filter(f => !re.findAllIn(f.getName).isEmpty)
          val cmd = Array(url + "ContentServer",
            "username=" + user,
            "password=" + password,
            "cmd=" + (if (args.size > 0) args(0) else "listcs"),
            "fromSites=" + sites,
            "datastore=AgileWCS-" + version,
            "resources=" + (if (args.size > 1) args(1)
            else if (args.size == 0) "@ALL_ASSETS"
            else if (args.size >= 1) args(0) match {
              case "listcs" => "@ALL_ASSETS;@ALL_NONASSETS"
              case "listds" => "@ALL_ASSETS;@ALL_NONASSETS"
              case "import" =>
                println("importing only site configuration - import manually assets and non-assets")
                "@SITE;@ASSET_TYPE"
              case "export" =>
                println("importing only site configuration - import manually assets and non-assets")
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
            val url = httpUrl + "CatalogManager"

            val cp = classpath.files.mkString(java.io.File.pathSeparator)
            val dir = file("export") / "envision" / ("AgileWCS-" + version) / "Populate"
            val cmd = Seq("-cp", cp, "COM.FutureTense.Apps.CatalogMover")

            val opts = Seq("-u", user, "-p", password, "-b", url, "-d", dir.toString, "-x")
            val all = cmd ++ opts ++ args

            //for(file <- classpath.files) println(file)
            //println(opts++args)

            Fork.java(None, all, Some(new java.io.File(".")), s.log)

          }
      }

  }

  // package jar task - build the jar and copy it  to destination 
  val wcsPackageJarTask = wcsPackageJar <<=
    (assembly, wcsHome) map {
      (jar, home) =>
        val destjar = file(home) / jar.getName
        IO.copyFile(jar, destjar)
        println("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  // copy resources from the app to the webapp task
  val wcsCopyStaticTask = wcsCopyStatic <<=
    (baseDirectory, wcsWebapp) map {
      (base, tgt) =>
        val src = base / "app" / "src" / "main" / "static"
        recursiveCopy(src, file(tgt))
    }

  // copy resources to the webapp task
  val wcsUpdateModelTask = wcsUpdateModel <<=
    (wcsUrl, wcsSites, wcsUser, wcsPassword, wcsPackageJar) map {
      (url, sites, user, pass, _) =>
        val deployer = new AgileWcsDeployer(url, sites, user, pass)
        println(deployer.deploy())
        deployer.getStatus
    }

  // deploy task  
  val wcsDeployTask = wcsDeploy <<=
    (wcsCopyStatic, wcsUpdateModel) map { (count, update) => () }

  // configuring everything
  val wcsConfigTask = wcsConfig <<=
    (wcsPackageJar, wcsVersion, wcsHome, wcsWebapp, wcsSites, wcsUser, wcsPassword) map {
      (appjar, version, home, webapp, site, username, password) =>

        // create local export dir for csdt
        file("export").mkdir
        (file("export") / "envision").mkdir
        (file("export") / "envision" / ("AgileWCS-" + version)).mkdir
        (file("export") / "envision" / ("AgileWCS-" + version) / "Populate").mkdir

        // configure futurentense. init
        val configFile = file(home) / "futuretense.ini"
        val config = new java.util.Properties
        config.load(new java.io.FileReader(configFile))

        /*
        config.setProperty("agilewcs.site", site);
        config.setProperty("agilewcs.user", username);
        config.setProperty("agilewcs.password", password);
        */
        config.setProperty("agilewcs.jar", appjar);
        config.setProperty("cs.csdtfolder", file("export").getAbsolutePath)
        config.setProperty("cs.pgexportfolder", file("export").getAbsolutePath)
        config.store(new java.io.FileWriter(configFile),
          "updated by AgileWCS setup")

        // same properties also for core in the classpath
        // NOTE this config file is currently unused
        val otherConfigFile = file(webapp) / "WEB-INF" / "classes" / "agilewcs.prp"
        val otherConfig = new java.util.Properties
        otherConfig.setProperty("agilewcs.site", site);
        otherConfig.setProperty("agilewcs.user", username);
        otherConfig.setProperty("agilewcs.password", password);
        otherConfig.setProperty("agilewcs.jar", appjar);
        otherConfig.store(new java.io.FileWriter(otherConfigFile),
          "created by AgileWCS setup")

        // configure

        configFile.getAbsolutePath
    }

  // setup task
  val wcsSetupTask = wcsSetup <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsConfig, Keys.`package` in Compile in AgileWcsBuild.core,
        managedClasspath in Runtime, classDirectory in Compile, wcsWebapp) map {
          (args, _, corejar, classpath, classes, webapp) =>

            // jars to include when performing a setup
            val destlib = file(webapp) / "WEB-INF" / "lib"

            val addJars = classpath.files filter
              (AgileWcsBuild.addFilterSetup accept _)

            val removeJars = destlib.listFiles filter
              (AgileWcsBuild.removeFilterSetup accept _)

            // create csdt export file

            // remove jars
            for (file <- removeJars) {
              val tgt = destlib / file.getName
              tgt.delete
              println("--- " + tgt.getAbsolutePath)
            }

            // add jars
            for (file <- addJars) yield {
              val tgt = destlib / file.getName
              IO.copyFile(file, tgt)

              println("<<< " + file.getAbsolutePath)
              println("+++ " + tgt.getAbsolutePath)
            }

            println("*** You need to restart WCS and then execute \"wcs-deploy\" ***")
        }
  }

}
