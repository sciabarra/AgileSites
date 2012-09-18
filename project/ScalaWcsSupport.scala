import sbt._
import Keys._

object ScalaWcsSupport {

  // new settings
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS Webapp CS Directory")
  lazy val wcsCsdtJar = SettingKey[String]("wcs-csdt-jar", "WCS CSDT Jar")

  lazy val wcsSite = SettingKey[String]("wcs-site", "WCS Site for site")
  lazy val wcsUser = SettingKey[String]("wcs-site", "WCS Site for user")
  lazy val wcsPassword = SettingKey[String]("wcs-site", "WCS Site password")

  lazy val wcsSetup = InputKey[Unit]("wcs-setup", "WCS Setup")

  // configurations default values
  val wcsHomeTask = wcsHome := "sites"
  val wcsWebappTask = wcsWebapp := "cs"

  // jars to include when performing a setup
  val includeFilterSetup = "scala-*" || "scalawcs-*"

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

  // setup task
  val wcsSetupTask = wcsSetup <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, managedClasspath in Compile, Keys.`package` in Compile,
        classDirectory in Compile, wcsHome, wcsWebapp,
        publishLocal in ScalaWcsBuild.setup, publishLocal in ScalaWcsBuild.tags) map {
          (args, classpath, jar, classes, home, webapp, _, _) =>

            val destlib = file(webapp) / "WEB-INF" / "lib"
            val jars = classpath.files filter (includeFilterSetup accept _)

            // copy jars to wcs
            val copied = for (file <- jars) yield {
              val tgt = destlib / file.getName
              IO.copyFile(file, tgt)
              println(">>> " + tgt)
              tgt.getAbsolutePath
            }

            // write an appropriate property file
            val destfile = file(webapp) / "WEB-INF" / "classes" / "scalawcs.properties"
            val develMode = args.indexOf("devel") != -1;
            val pw = new java.io.PrintWriter(destfile)
            if (develMode) {
              // write a property to find the package jar build by sbt
              pw.println("scalawcs.jar=%s".format(jar.toString))
              println("\nConfigured in Development Mode\nuse ~package to compile\njar in " + jar)
            } else {
              // directly locate the original sbt 
              val destjar = file(home) / jar.getName
              IO.copyFile(jar, destjar)
              println(">>> " + destjar)
              pw.println("scalawcs.jar=%s".format(destjar.toString))
              println("\nConfigured in Production Mode\njar in " + destjar)
            }
            pw.close
            println("*** Please restart WCS ***")
        }
  }

  def wcsCsdtCommand = Command.args("wcs-csdt", "WCS CSDT Interface") { (state, args) =>
    println("Hi " + args.mkString(" "))
    state
  }

}