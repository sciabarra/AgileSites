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

  val _wcsHome = wcsHome := "sites"
  val _wcsWebapp = wcsWebapp := "cs"

  val _wcsDeploy = wcsDeploy <<= inputTask {
    (argTask: TaskKey[Seq[String]]) =>
      (argTask, managedClasspath in Compile, Keys.`package` in Compile,
        classDirectory in Compile, wcsHome, wcsWebapp) map {
          (args, classpath, jar, classes, home, webapp) =>

            val destlib = file(webapp) / "WEB-INF" / "lib"
            val jars = classpath.filter(!_.data.isDirectory).map(_.data)
            val destfile = file(webapp) / "WEB-INF" / "classes" / "scalawcs.properties"

            val lib = file("lib")
            lib.mkdirs

            val develMode = args.indexOf("devel") != -1;

            // copy jars to wcs
            //println("\nCopying to WCS:")
            val copied = for (file <- jars) yield {
              val src = destlib / file.getName
              IO.copyFile(file, src)
              println(">>> " + file)
              src.getAbsolutePath
            }

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

  val _tagGenerator = (sourceGenerators in Compile) <+=
    (sourceManaged in Compile, wcsWebapp) map {
      (dstDir, srcDir) =>
        //println(dstDir)
        val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"

        println(tlds)

        val l = for {
          tld <- tlds.listFiles
          if tld.getName.endsWith(".tld")
        } yield {
          //println(tld)
          val src = tld.getAbsolutePath
          val cls = Tld2Tag.tld2class(src)
          val dst = dstDir / cls + ".scala"
          //println("====\n" + dst)
          print(cls + " ")
          val body = Tld2Tag(src)
          val dstFile = file(dst)
          IO.write(dstFile, body)
          dstFile
        }
        l.toSeq
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
      scalaVersion := "2.9.2", _wcsWebapp, _unmanagedBase, _tagGenerator))

  lazy val root: Project = Project(
    id = "scalawcs",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      name := "ScalaWCS",
      organization := "com.sciabarra",
      version := "0.1",
      scalaVersion := "2.9.2",
      libraryDependencies ++= Seq(
        "com.sciabarra" %% "scalawcs-boot" % "0.1",
        "org.apache.tomcat" % "servlet-api" % "6.0.32",
        "commons-logging" % "commons-logging" % "1.1.1"),
      _wcsHome, _wcsWebapp, _wcsDeploy, _unmanagedBase)) aggregate (boot)
}
