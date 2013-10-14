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
  val v = "1.0.1" 

  // remove then add those jars in setup
  val addFilterSetup =  "agilesites-core*" || "junit*" 

  val removeFilterSetup = addFilterSetup || "scala-library*"

  // configuring WCS jars as unmanaged lib
  val unmanagedFilter = "log4j-*" || "slf4j*" || "spring-*" || "commons-*" || "http-*" || "jsoup*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" || "assetapi*" || "xstream*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar" || "ehcache-*" || "sites-*" || "esapi-*"

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
    "junit" % "junit" % "4.4",
    "org.springframework" % "spring" % "2.5.5",
    "org.springframework" % "spring-test" % "2.5.5",
    "commons-logging" % "commons-logging" % "1.1.1",
    "com.novocode" % "junit-interface" % "0.8" % "test",
    "log4j" % "log4j" % "1.2.16",
    "commons-httpclient" % "commons-httpclient" % "3.1",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.apache.httpcomponents" % "httpcore" % "4.1.2",
    "org.apache.httpcomponents" % "httpmime" % "4.1.2",
    "org.apache.james" % "apache-mime4j" % "0.5")

  val coreSettings = Defaults.defaultSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
    scalaVersion := "2.10.2",
    organization := "com.sciabarra",
    publishTo := Some(Resolver.file("repo",  new File( "project/repo" )) ),
    publishMavenStyle := true,
    version <<= (wcsVersion) { x => x +  "_" + v },
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)

  val commonSettings = coreSettings ++ Seq(
    resolvers += "Local Maven Repository" at "file:///"+(file("project").absolutePath)+"/repo",
    libraryDependencies <++= (version) {
      x =>
        coreDependencies ++ Seq("com.sciabarra" % "agilesites-core" % x)
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
      crossPaths := false,
	    javacOptions ++= Seq("-encoding", "UTF-8", "-g"),
      EclipseKeys.skipProject := true,
      coreGeneratorTask))

  // API
  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      name := "agilesites-api",
      scalacOptions ++= Seq("-deprecation"),
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))


  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      name := "agilesites-app",
      scalacOptions ++= Seq("-deprecation"),
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java,
      wcsCopyHtmlTask)) dependsOn (api)

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
      wcsGenerateIndexTask,
      excludedJars in assembly <<= (fullClasspath in assembly),
      watchSources ++= ((file("app") / "src" / "main" / "static" ** "*").getFiles),
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala,
      EclipseKeys.skipProject := false,
      assembleArtifact in packageScala := false)) dependsOn (app) aggregate (app, api)
}

