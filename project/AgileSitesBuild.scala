package wcs.build

import sbt._
import Keys._

import com.typesafe.sbteclipse.plugin.EclipsePlugin._

import sbtassembly.Plugin._
import AssemblyKeys._

import sys.process._
import scala.xml.transform.RewriteRule
import giter8.ScaffoldPlugin.scaffoldSettings

object AgileSitesBuild extends Build with AgileSitesSupport {

  // jars to be added to the library setup
  val setupFilter =  "agilesites-api*" || "junit*" || "hamcrest*"

  ///  core dependencies - those are used for compiling
  val coreDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "junit" % "junit" % "4.4",
    "org.hamcrest" % "hamcrest-all" % "1.3",
    "org.springframework" % "spring" % "2.5.5",
    "org.springframework" % "spring-test" % "2.5.5",
    "commons-logging" % "commons-logging" % "1.1.1",
    "log4j" % "log4j" % "1.2.16",
    "commons-httpclient" % "commons-httpclient" % "3.1",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.apache.httpcomponents" % "httpcore" % "4.1.2",
    "org.apache.httpcomponents" % "httpmime" % "4.1.2",
    "org.apache.james" % "apache-mime4j" % "0.5",
    "junit" % "junit" % "4.11" % "test",
    "com.novocode" % "junit-interface" % "0.10" % "test")

  // configuring WCS jars as unmanaged lib from sites directory
  val unmanagedFilter = "log4j-*" || "slf4j*" || "spring-*" || "commons-*" || "http-*" || "jsoup*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" || "assetapi*" || "xstream*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar" || "ehcache-*" || "sites-*" || "esapi-*"

  /// END CHANGES
  val v = "1.8"

  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := unmanagedFilter

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp {
    base => file(base) / "WEB-INF" / "lib"
  }

  val unmanagedJarsTask = unmanagedJars in Compile <+= wcsCsdtJar map {
    jar => Attributed.blank(file(jar))
  }

  val coreSettings = Defaults.defaultSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
    resolvers += "Local Maven Repository" at "file:///"+(file("project").absolutePath)+"/repo",
    scalaVersion := "2.10.2",
    organization := "com.sciabarra",
    publishTo := Some(Resolver.file("repo",  new File( "project/repo" )) ),
    publishMavenStyle := true,
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask)
 
  import javadoc.JavadocPlugin.javadocSettings
  import javadoc.JavadocPlugin.javadocTarget

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = coreSettings ++ Seq(
      name := "agilesites-core",
      version <<= (wcsVersion) { x => x +  "_" + v },
      libraryDependencies ++= coreDependencies,
      publishArtifact in packageDoc := false,
      crossPaths := false,
	    javacOptions ++= Seq("-encoding", "UTF-8", "-g"),
      coreGeneratorTask,
      EclipseKeys.skipProject := true))

  // API
  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = coreSettings ++ Seq(
     name := "agilesites-api",
     version := v,
     libraryDependencies <++= (wcsVersion) { x => 
       Seq("com.sciabarra" % "agilesites-core" % (x + "_" + v) )
     },
     publishArtifact in packageDoc := false,
     javacOptions ++= Seq("-g"),
     crossPaths := false,
     wcsPackageJarTask,
     EclipseKeys.skipProject := true,
     EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))

 
  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = coreSettings ++ Seq(
      javacOptions ++= Seq("-g"), 
      name := "agilesites-app",
      version := v,
      libraryDependencies <++= (wcsVersion) { x => Seq(
           "com.sciabarra" % "agilesites-core" % (x + "_" + v),
           "com.sciabarra" % "agilesites-api" % v withSources())},
      wcsGenerateIndexTask,
      wcsCopyHtmlTask,
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))

  /// ALL
  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = coreSettings ++
      assemblySettings ++ 
      scaffoldSettings ++ Seq(
       javacOptions ++= Seq("-g"), 
       name := "agilesites-all",
       version := v,
       libraryDependencies <++= (wcsVersion) { x => Seq(
           "com.sciabarra" % "agilesites-core" % (x + "_" + v),
           "com.sciabarra" % "agilesites-api" % v withSources())},
       wcsCsdtTask,
       wcsVirtualHostsTask,
       wcsCopyJarsWebTask,
       wcsCopyJarsLibTask,
       wcsHelloTask,
       wcsCatalogManagerTask,
       wcsSetupTask,
       wcsWebappSatelliteTask,
       wcsSetupSatelliteTask,
       wcsDeployTask,
       wcsCopyStaticTask,
       wcsAssemblyJarTask,
       wcsUpdateAssetsTask,
       wcsLogTask,
       excludedJars in assembly <<= (fullClasspath in assembly),
       watchSources ++= ((file("app") / "src" / "main" / "static" ** "*").get),
       EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala,
       EclipseKeys.skipProject := false,
       wcsSetupOffline := {  println("Please use wcs-setup instead") },
       wcsSetupOnline := {  println("Please use wcs-deploy instead") },
       assembleArtifact in packageScala := false)) dependsOn (app) aggregate (app, api)
}
