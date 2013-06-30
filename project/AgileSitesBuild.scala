package wcs.build

import sbt._
import Keys._
import sys.process._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import scala.xml.transform.RewriteRule
import giter8.ScaffoldPlugin.scaffoldSettings

object AgileSitesBuild extends Build with AgileSitesSupport {

  // if you change this 
  // remember to update the agilesites scripts
  val v = "1.2"

  // configuring WCS jars as unmanaged lib
  val unmanagedFilter = "log4j-*" || "slf4j*" || "spring-*" || "commons-*" || "http-*" || "jsoup*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" || "assetapi*" || "xstream*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar" || "ehcache-*"

  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := unmanagedFilter

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp {
    base => file(base) / "WEB-INF" / "lib"
  }

  val unmanagedJarsTask = unmanagedJars in Compile <+= wcsCsdtJar map {
    jar => Attributed.blank(file(jar))
  }

  ///  core dependencies - those are used for compiling
  val coreDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "junit" % "junit" % "4.8.2",
    "org.springframework" % "spring" % "2.5.5",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test",
    "commons-logging" % "commons-logging" % "1.1.1",
    "log4j" % "log4j" % "1.2.16",
    "commons-httpclient" % "commons-httpclient" % "3.1",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.apache.httpcomponents" % "httpcore" % "4.1.2",
    "org.apache.httpcomponents" % "httpmime" % "4.1.2",
    "org.apache.james" % "apache-mime4j" % "0.5",
    "rhino" % "js" % "1.7R2",
    "org.scalatest" %% "scalatest" % "2.0.M5b",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1")

  /// which jars you actually use at runtime
  /// that are copied by the wcs-setup-offline  
  val addFilterSetup = "agilesites-core*" ||
    "scala-library*" ||
    "junit*" ||
    "scalatest*" ||
    "scalamock*" ||
    "js-*";

  val removeFilterSetup = addFilterSetup

  val coreSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0",
    organization := "com.sciabarra",
    version <<= (wcsVersion) { x => v + "_" + x },
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)

  val commonSettings = coreSettings ++ Seq(
    libraryDependencies <++= (version) {
      x =>
        coreDependencies ++ Seq("com.sciabarra" %% "agilesites-core" % x)
    })

  import javadoc.JavadocPlugin.javadocSettings
  import javadoc.JavadocPlugin.javadocTarget

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = coreSettings ++ Seq(
      libraryDependencies ++= coreDependencies,
      publishArtifact in packageDoc := false,
      name := "agilesites-core",
      //EclipseKeys.skipProject := true,
      coreGeneratorTask))

  // API
  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      name := "agilesites-api",
      wcsGenerateIndexTask,
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))

  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      name := "agilesites-app",
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java,
      wcsCopyHtmlTask)) dependsOn (api)

  // Scala API and APP
  lazy val scala: Project = Project(
    id = "scala",
    base = file("scala"),
    settings = commonSettings ++ Seq(
      name := "agilesites-scala",
      wcsCopyHtmlTask,
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala)) dependsOn (api)

  /// ALL
  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = commonSettings ++ assemblySettings ++ scaffoldSettings ++ Seq(
      name := "agilesites-all",
      wcsCsdtTask,
      wcsVirtualHostsTask,
      wcsSetupOnlineTask,
      wcsSetupOfflineTask,
      wcsDeployTask,
      wcsCopyStaticTask,
      wcsPackageJarTask,
      wcsUpdateAssetsTask,
      wcsLogTask,
      wcsImportTask,
      wcsExportTask,
      excludedJars in assembly <<= (fullClasspath in assembly),
      //EclipseKeys.skipProject := true,
      assembleArtifact in packageScala := false)) dependsOn (app, scala) aggregate (app, api, scala)
}

