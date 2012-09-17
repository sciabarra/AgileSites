import sbt._
import Keys._
import sys.process._

object ScalaWcsBuild extends Build {

  // new configurations
  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS Webapp CS Directory")
  lazy val wcsSetup = InputKey[Unit]("wcs-setup", "WCS Deploy")

  // parameters
  val commonDependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.6.6",
    "org.eintr.loglady" %% "loglady" % "1.0.0",
    "org.specs2" %% "specs2" % "1.12.1" % "test",
    "ch.qos.logback" % "logback-classic" % "1.0.7" % "test")

  val _includeFiltersUnmanagedJars = includeFilter in unmanagedJars := "commons-*" || "http-*" || "cs-*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar"

  val _includeFilterSetup = "scala-*" || "scalawcs-*"

  // configurations default values
  val _wcsHome = wcsHome := "sites"
  val _wcsWebapp = wcsWebapp := "cs"
  val _unmanagedBase = unmanagedBase in Compile <<= wcsWebapp { base => file(base) / "WEB-INF" / "lib" }

  val _wcsSetup = wcsSetup <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, managedClasspath in Compile, Keys.`package` in Compile,
        classDirectory in Compile, wcsHome, wcsWebapp, publishLocal) map {
          (args, classpath, jar, classes, home, webapp, _) =>

            val destlib = file(webapp) / "WEB-INF" / "lib"                    
            val jars = classpath.files filter( _includeFilterSetup accept _)

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

  // generate tag access classes from tld files
  val _tagGenerator = (sourceGenerators in Compile) <+=
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
          //if !dst.exists
        } yield {
          print(cls + " ")
          val body = Tld2Tag(src)
          IO.write(dst, body)
          dst
        }
        l.toSeq
    }

  
  // projects
  lazy val setup: Project = Project(
    id = "setup",
    base = file("setup"),
    settings = Defaults.defaultSettings ++ Seq(
      scalaVersion := "2.9.1",
      organization := "org.scalawcs",
      name := "scalawcs-setup",
      version := "0.2",
      libraryDependencies ++= commonDependencies ++ Seq(
        "commons-logging" % "commons-logging" % "1.1.1",
        "javax.servlet" % "servlet-api" % "2.5"),
      _unmanagedBase, 
      _includeFiltersUnmanagedJars))

  lazy val tags: Project = Project(
    id = "tags",
    base = file("tags"),
    settings = Defaults.defaultSettings ++ Seq(
      scalaVersion := "2.9.1",
      organization := "org.scalawcs",
      name := "scalawcs-tags",
      version := "0.2",
      libraryDependencies ++= commonDependencies ++ Seq(
        "commons-logging" % "commons-logging" % "1.1.1",
        "javax.servlet" % "servlet-api" % "2.5"),
      _unmanagedBase,
      _includeFiltersUnmanagedJars,
      _tagGenerator))

  lazy val app: Project = Project(
    id = "app",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      scalaVersion := "2.9.1",
      organization := "org.scalawcs",
      name := "scalawcs-app",
      version := "0.2",
      libraryDependencies ++= commonDependencies ++
        Seq("org.scalawcs" %% "scalawcs-setup" % "0.2",
          "org.scalawcs" %% "scalawcs-tags" % "0.2"),
      _unmanagedBase,
      _includeFiltersUnmanagedJars,
      _wcsSetup))
}
