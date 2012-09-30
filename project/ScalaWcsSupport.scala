import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object ScalaWcsSupport {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS Webapp CS Directory")
  lazy val wcsSetup = InputKey[Unit]("wcs-setup", "WCS Setup")
  lazy val wcsDeploy = TaskKey[String]("wcs-deploy", "WCS Deploy")

  lazy val wcsUser = SettingKey[String]("wcs-user", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-password", "WCS Site password")
  lazy val wcsUrl = SettingKey[String]("wcs-url", "WCS URL")
  lazy val wcsSite = SettingKey[String]("wcs-site", "WCS Site for site")

  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")
  lazy val wcsCsdt = InputKey[Unit]("wcs-csdt", "WCS CSDT")

  // generate tag access classes from tld files
  val tagGeneratorTask = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp) map {
      (dstDir, srcDir) =>
        //println(dstDir)
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"

        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val cls = Tld2Tag.tld2class(src)
          val dst = file(dstDir / cls + ".scala")
          // if tld.getName.equalsIgnoreCase("listobject.tld") // select only one for debug generator
        } yield {
          if (!dst.exists) {
            print(cls + " ")
            val body = Tld2Tag(src)
            IO.write(dst, body)
          }
          dst
        }
        l.toSeq
    }

  val wcsCsdtTask = wcsCsdt <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, wcsUrl, wcsSite, wcsUser, wcsPassword, fullClasspath in Compile, streams, runner) map {
        (args, url, site, user, password, classpath, s, runner) =>
          val re = "^(cas-client-core-\\d|csdt-client-\\d|rest-api-\\d|wem-sso-api-\\d|wem-sso-api-cas-\\d).*.jar$".r;
          val seljars = classpath.files.filter(f => !re.findAllIn(f.getName).isEmpty)
          val cmd = Array(url + "ContentServer",
            "username=" + user,
            "password=" + password,
            "cmd=" + (if (args.size > 0) args(0) else "listcs"),
            "fromSites=" + site,
            "datastore=" + site,
            "resources=" + (if (args.size > 1) args(1) else "@ALL_ASSETS"))
          //println(cmd.mkString("java -cp "+seljars.mkString(":")+" com.fatwire.csdt.client.main.CSDT ", " ", ""))
          Run.run("com.fatwire.csdt.client.main.CSDT",
            seljars, cmd, s.log)(runner)
      }
  }

  // deploy task  
  val wcsDeployTask = wcsDeploy <<=
    (assembly, wcsHome) map {
      (jar, home) =>
        val destjar = file(home) / jar.getName
        IO.copyFile(jar, destjar)
        println("+++ " + destjar.getAbsolutePath)
        destjar.getAbsolutePath.toString
    }

  // setup task
  val wcsSetupTask = wcsSetup <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask,
        Keys.`package` in Compile in ScalaWcsBuild.core,
        wcsDeploy,
        managedClasspath in Runtime,
        classDirectory in Compile,
        wcsHome, wcsWebapp, wcsSite) map {
          (args, corejar, appjar, classpath, classes, home, webapp, site) =>
            // write property file
            val configFile = file(home) / "futuretense.ini"
            val config = new java.util.Properties
            config.load(new java.io.FileReader(configFile))
            config.setProperty("cs.csdtfolder", file("export").getAbsolutePath)
            config.setProperty("scalawcs.jar", appjar);
            config.store(new java.io.FileWriter(configFile),
              "updated by ScalaWCS setup")

            // jars to include when performing a setup
            val destlib = file(webapp) / "WEB-INF" / "lib"

            // add jars ... but with an hack
            // remove the scalawcs-core from the the classpath
            // and add the one just packaged
            //val jarFiles = classpath.files filterNot
            //  (_.getName startsWith "scalawcs-core")
            //val addJars = (jarFiles :+ corejar) filter
            //  (ScalaWcsBuild.addFilterSetup accept _)

            val addJars = classpath.files filter
              (ScalaWcsBuild.addFilterSetup accept _)

            val removeJars = destlib.listFiles filter
              (ScalaWcsBuild.removeFilterSetup accept _)

            // create csdt export file
            file("export").mkdir
            (file("export") / "envision").mkdir
            (file("export") / "envision" / site).mkdir

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

              //println("<<< " + file.getAbsolutePath)
              println("+++ " + tgt.getAbsolutePath)
            }

            println("*** You need to restart WCS")
        }
  }

}