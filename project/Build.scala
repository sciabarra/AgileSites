import sbt._
import Keys._
import sys.process._

object ScalaWcsBuild extends Build {

  val _scalaVersion = "2.9.2"
  val _organization = "com.sciabarra"

  lazy val wcsHome = SettingKey[String]("wcs-home", "WCS Home Directory")
  lazy val wcsWebapp = SettingKey[String]("wcs-webapp", "WCS Webapp CS Directory")
  lazy val wcsDeploy = InputKey[Unit]("wcs-deploy", "WCS Deploy")

  lazy val _unmanagedBase = unmanagedBase <<= wcsWebapp { base => file(base) / "WEB-INF" / "lib" }

  /*
  val _unmanagedJars = unmanagedJars in Compile <<= (baseDirectory, wcsWebapp) map { (base, webapp) =>
    val baseDirectories = (base / "lib") +++ (file(webapp) / "WEB-INF" / "lib")
    //println(baseDirectories)
    val customJars = (baseDirectories ** "*.jar")
    //println(customJars)
    customJars.classpath
  }*/

  val _wcsHome = wcsHome := "sites"
  val _wcsWebapp = wcsWebapp := "cs"

  val _wcsDeploy = wcsDeploy <<= inputTask { (argTask: TaskKey[Seq[String]]) =>
    (argTask, managedClasspath in Compile, Keys.`package` in Compile,
      classDirectory in Compile, wcsHome, wcsWebapp) map {
        (args, classpath, jar, classes, home, webapp) =>

          val destlib = file(webapp) / "WEB-INF" / "lib"
          val jars = classpath.filter(!_.data.isDirectory).map(_.data)
          val destfile = file(webapp) / "WEB-INF" / "classes" / "scalawcs.properties"

          val lib = file("lib")
          lib.mkdirs

          val quickMode = args.indexOf("quick") != -1;
          val develMode = args.indexOf("devel") != -1;

          // copy jars to wcs
          //println("\nCopying to WCS:")
          val copied = for (file <- jars) yield {
            val src = destlib / file.getName
            if (!quickMode) {
              IO.copyFile(file, src)
              println(">>> " + file)
            }
            src.getAbsolutePath
          }

          //println("\nCopying from WCS:")
          /*
        val copiedBack = for {
          file <- destlib.listFiles
          if copied.indexOf(file.getAbsolutePath) == -1
        } yield {
          val tgt = lib / file.getName
          if (!quickMode) {
            IO.copyFile(file, tgt)
            println("<<< " + tgt)
          }
          tgt
        }*/

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

          println("Please restart WCS and SBT")
      }
  }

  override lazy val settings = super.settings ++
    Seq(wcsHome := "sites", wcsWebapp := "cs")

  lazy val boot: Project = Project(
    id = "scalawcs-boot",
    base = file("boot"),
    settings = Defaults.defaultSettings ++ Seq(
      name := "ScalaWCS-boot",
      organization := "com.sciabarra",
      version := "0.1",
      libraryDependencies ++= Seq(
        "org.apache.tomcat" % "servlet-api" % "6.0.32",
        "commons-logging" % "commons-logging" % "1.1.1"),
      scalaVersion := "2.9.2", _wcsWebapp, _unmanagedBase))

  lazy val root: Project = Project(
    id = "scalawcs",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      libraryDependencies +=
        "com.sciabarra" %% "scalawcs-boot" % "0.1",
      name := "ScalaWCS",
      organization := "com.sciabarra",
      version := "0.1",
      scalaVersion := "2.9.2",
      _wcsHome, _wcsWebapp, _wcsDeploy, _unmanagedBase)) aggregate (boot)
}
