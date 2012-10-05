import sbt._
import Keys._
import sys.process._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

object ScalaWcsBuild extends Build {

  // settings
  lazy val wcsHome = ScalaWcsSupport.wcsHome
  lazy val wcsWebapp = ScalaWcsSupport.wcsWebapp
  lazy val wcsCsdtJar = ScalaWcsSupport.wcsCsdtJar

  lazy val wcsUrl = ScalaWcsSupport.wcsUrl
  lazy val wcsSite = ScalaWcsSupport.wcsSite
  lazy val wcsVersion = ScalaWcsSupport.wcsVersion
  lazy val wcsUser = ScalaWcsSupport.wcsUser
  lazy val wcsPassword = ScalaWcsSupport.wcsPassword

  lazy val wcsSetupTask = ScalaWcsSupport.wcsSetupTask
  lazy val wcsDeployTask = ScalaWcsSupport.wcsDeployTask
  lazy val wcsCsdtTask = ScalaWcsSupport.wcsCsdtTask
  lazy val wcsCopyStaticTask = ScalaWcsSupport.wcsCopyStaticTask
  lazy val coreGeneratorTask = ScalaWcsSupport.coreGeneratorTask
  
  // remove then add those jars in setup
  val addFilterSetup = "scala-library*" || "scalawcs-core*" 
  val removeFilterSetup = addFilterSetup 

  // configuring WCS jars as unmanaged lib
  val unmanagedFilter = "commons-*" || "http-*" || "jsoup-*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar"

  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := unmanagedFilter

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp {
    base => file(base) / "WEB-INF" / "lib"
  }
  val unmanagedJarsTask = unmanagedJars in Compile <+= wcsCsdtJar map {
    jar => Attributed.blank(file(jar))
  }

  /// COMMONS
  val coreDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "commons-logging" % "commons-logging" % "1.1.1",
    "org.specs2" %% "specs2" % "1.12.1" % "test")

  val commonSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.9.1",
    organization := "org.scalawcs", // collect jars from WCS
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= coreDependencies,
      name := "scalawcs-core",
      version := "0.3", // if you change this, fix dependencies, too!
      coreGeneratorTask))

  /// API 

  val commonDependencies = coreDependencies ++
    Seq("org.scalawcs" %% "scalawcs-core" % "0.3")

  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= commonDependencies,
      name := "scalawcs-api",
      version := "0.2"))

  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= commonDependencies,
      name := "scalawcs-app",
      version := "0.2",
      wcsCopyStaticTask)) dependsOn (api)

  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = commonSettings ++ assemblySettings ++ Seq(
      libraryDependencies ++= commonDependencies,
      name := "scalawcs-all",
      version := "0.2",
      wcsCsdtTask,
      wcsSetupTask,
      wcsDeployTask,
      EclipseKeys.skipProject := true,
      assembleArtifact in packageScala := false,
      excludedJars in assembly <<= (fullClasspath in assembly))) dependsOn(app) aggregate (app, api)

}
