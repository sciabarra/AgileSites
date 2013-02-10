import sbt._
import Keys._
import sys.process._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys

object AgileWcsBuild extends Build {

  val agileVersion = "0.3"

  // settings
  lazy val wcsHome = AgileWcsSupport.wcsHome
  lazy val wcsWebapp = AgileWcsSupport.wcsWebapp
  lazy val wcsCsdtJar = AgileWcsSupport.wcsCsdtJar

  lazy val wcsUrl = AgileWcsSupport.wcsUrl
  lazy val wcsSites = AgileWcsSupport.wcsSites
  lazy val wcsVersion = AgileWcsSupport.wcsVersion
  lazy val wcsUser = AgileWcsSupport.wcsUser
  lazy val wcsPassword = AgileWcsSupport.wcsPassword

  lazy val wcsConfigTask = AgileWcsSupport.wcsConfigTask
  lazy val wcsSetupTask = AgileWcsSupport.wcsSetupTask
  lazy val wcsDeployTask = AgileWcsSupport.wcsDeployTask
  lazy val wcsCsdtTask = AgileWcsSupport.wcsCsdtTask
  lazy val wcsCmTask = AgileWcsSupport.wcsCmTask
  lazy val wcsCopyStaticTask = AgileWcsSupport.wcsCopyStaticTask
  lazy val wcsCopyHtmlTask = AgileWcsSupport.wcsCopyHtmlTask
  lazy val wcsPackageJarTask = AgileWcsSupport.wcsPackageJarTask
  lazy val wcsUpdateAssetsTask = AgileWcsSupport.wcsUpdateAssetsTask
  lazy val coreGeneratorTask = AgileWcsSupport.coreGeneratorTask

  // remove then add those jars in setup
  val addFilterSetup = "scala-library*" || "agilewcs-core*" || "junit*" || "specs2*"
  val removeFilterSetup = addFilterSetup

  // configuring WCS jars as unmanaged lib
  val unmanagedFilter = "spring-*" || "commons-*" || "http-*" || "jsoup*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" || "assetapi*" || "xstream*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar" || "ehcache-*" || "slf4j*"

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
    "org.specs2" %% "specs2" % "1.13",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.apache.httpcomponents" % "httpcore" % "4.1.2",
    "org.apache.httpcomponents" % "httpmime" % "4.1.2",
    "org.apache.james" % "apache-mime4j" % "0.5",
    "junit" % "junit" % "4.8.2",
    "com.novocode" % "junit-interface" % "0.8" % "test")

  val commonSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0",
    organization := "com.sciabarra", // collect jars from WCS
    compileOrder := CompileOrder.Mixed,
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = commonSettings ++ Seq(
      libraryDependencies ++= coreDependencies,
      publishArtifact in packageDoc := false,
      name := "agilewcs-core",
      version <<= (wcsVersion) { v => agileVersion + "_" + v },
      // if you change this, fix dependencies, too!
      coreGeneratorTask))

  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      //libraryDependencies ++= commonDependencies,
      libraryDependencies <++= (wcsVersion) {
        v =>
          val nv = agileVersion + "_" + v
          coreDependencies ++ Seq("com.sciabarra" %% "agilewcs-core" % nv)
      },
      name := "agilewcs-api",
      version := agileVersion))

  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      //libraryDependencies ++= commonDependencies,
      libraryDependencies <++= (wcsVersion) {
        v =>
          val nv = agileVersion + "_" + v
          coreDependencies ++ Seq("com.sciabarra" %% "agilewcs-core" % nv)
      },
      wcsCopyHtmlTask,
      name := "agilewcs-app",
      version := agileVersion)) dependsOn (api)

  /// ALL
  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = commonSettings ++ assemblySettings ++ Seq(
      //libraryDependencies ++= commonDependencies,
      libraryDependencies <++= (wcsVersion) {
        v =>
          val nv = agileVersion + "_" + v
          coreDependencies ++ Seq("com.sciabarra" %% "agilewcs-core" % nv)
      },
      name := "agilewcs-all",
      version := agileVersion,
      wcsCsdtTask,
      wcsCmTask,
      wcsConfigTask,
      wcsSetupTask,
      wcsDeployTask,
      wcsCopyStaticTask,
      wcsPackageJarTask,
      wcsUpdateAssetsTask,
      EclipseKeys.skipProject := true,
      assembleArtifact in packageScala := false,
      excludedJars in assembly <<= (fullClasspath in assembly))) dependsOn (app) aggregate (app, api)
}
