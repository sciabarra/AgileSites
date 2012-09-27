import sbt._
import Keys._
import sys.process._
import sbtassembly.Plugin._
import AssemblyKeys._

object ScalaWcsBuild extends Build {

  // settings
  lazy val wcsHome = ScalaWcsSupport.wcsHome
  lazy val wcsWebapp = ScalaWcsSupport.wcsWebapp
  lazy val wcsCsdtJar = ScalaWcsSupport.wcsCsdtJar

  lazy val wcsUrl = ScalaWcsSupport.wcsUrl
  lazy val wcsSite = ScalaWcsSupport.wcsSite
  lazy val wcsUser = ScalaWcsSupport.wcsUser
  lazy val wcsPassword = ScalaWcsSupport.wcsPassword

  lazy val tagGeneratorTask = ScalaWcsSupport.tagGeneratorTask
  lazy val wcsSetupTask = ScalaWcsSupport.wcsSetupTask
  lazy val wcsDeployTask = ScalaWcsSupport.wcsDeployTask
  lazy val wcsCsdtTask = ScalaWcsSupport.wcsCsdtTask

  // parameters
  val commonDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "commons-logging" % "commons-logging" % "1.1.1",
    "org.specs2" %% "specs2" % "1.12.1" % "test")

  // remove then add those jars in setup
  val addFilterSetup = "scala-library*" || "scalawcs-core*"
  val removeFilterSetup = addFilterSetup
  //|| "slf4j-jdk14*"
  val unmanagedFilter = "commons-*" || "http-*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar"

  // jars to include as unmanaged
  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := unmanagedFilter

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp {
    base => file(base) / "WEB-INF" / "lib"
  }
  val unmanagedJarsTask = unmanagedJars in Compile <+= wcsCsdtJar map {
    jar => Attributed.blank(file(jar))
  }

  // common settings
  val commonSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.9.1",
    organization := "org.scalawcs",
    libraryDependencies ++= commonDependencies,
    // collect jars from WCS
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)

  // projects
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = commonSettings ++ Seq(
      name := "scalawcs-core",
      version := "0.2",
      tagGeneratorTask))

  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      name := "scalawcs-api",
      version := "0.2",
      libraryDependencies ++=
        (commonDependencies
          ++ Seq("org.scalawcs" %% "scalawcs-core" % "0.1"))))

  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      name := "scalawcs-app",
      version := "0.2",
      libraryDependencies ++=
        commonDependencies)) dependsOn (api)

  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = commonSettings ++
      Seq(
        name := "scalawcs-all",
        version := "0.2",
        libraryDependencies ++=
          (commonDependencies ++
            Seq("org.scalawcs" %% "scalawcs-core" % "0.1")),
        wcsSetupTask,
        wcsDeployTask,
        wcsCsdtTask) ++
        assemblySettings ++ Seq(
          assembleArtifact in packageScala := false,
          excludedJars in assembly <<= (fullClasspath in assembly))) dependsOn (app) aggregate (app, api, core)
}
