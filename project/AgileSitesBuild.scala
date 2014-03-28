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

  // jars to be added to the wcs-setup
   val webappFilter=  "agilesites-core*" || "jcl-core*"

  // jars to be added to the library setup
  val setupFilter =  "agilesites-api*" || "junit*" || "hamcrest*" 

  ///  core dependencies - those are used for compiling
  val coreDependencies = Seq(
    "javax.servlet" % "servlet-api" % "2.5",
    "org.xeustechnologies" % "jcl-core" % "2.2.1",
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
    "org.apache.tomcat" % "tomcat-catalina" % "7.0.52",
    "org.apache.tomcat" % "tomcat-dbcp" % "7.0.52",
    "org.apache.tomcat.embed" % "tomcat-embed-core" % "7.0.52",
    "org.apache.tomcat.embed" % "tomcat-embed-logging-log4j" % "7.0.52",
    "org.apache.tomcat.embed" % "tomcat-embed-jasper" % "7.0.52",
    "org.hsqldb" % "hsqldb" % "1.8.0.10",
    "junit" % "junit" % "4.11",
    "com.novocode" % "junit-interface" % "0.10")
 

  // configuring WCS jars as unmanaged lib from sites directory
  val unmanagedFilter = "log4j-*" || "slf4j*" || "spring-*" || "commons-*" || "http-*" || "jsoup*" || "cs-*" ||
    "wem-sso-api-*" || "rest-api-*" || "cas-client-*" || "assetapi*" || "xstream*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar" || "ehcache-*" || "sites-*" || "esapi-*"

  /// END CHANGES
  val v = "1.8.1"

  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := unmanagedFilter

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp {
    base => file(base) / "WEB-INF" / "lib"
  }

  val toolsJar = file(System.getProperty("java.home")) / ".." / "lib" / "tools.jar"
  val classesJar = file(System.getProperty("java.home")) / ".." / "Classes" / "classes.jar"

  val unmanagedJarsTask = unmanagedJars in Compile <++= wcsCsdtJar map {
    jar => 

       val tools = if(toolsJar.exists) Attributed.blank(toolsJar) 
         else if(classesJar.exists) Attributed.blank(classesJar) 
         else throw new Exception("cannot locate java compiler - is JAVA_HOME a JDK instead of a JRE?")

       Seq(Attributed.blank(file(jar)), tools)
  }

  val coreSettings = Defaults.defaultSettings ++ 
    net.virtualvoid.sbt.graph.Plugin.graphSettings ++ Seq(
    resolvers += "Local Maven Repository" at "file:///"+(file("project").absolutePath)+"/repo",
    scalaVersion := "2.10.2",
    organization := "com.sciabarra",
    publishTo := Some(Resolver.file("repo",  new File( "project/repo" )) ),
    publishMavenStyle := true,
    javacOptions in Compile := Seq("-encoding", "UTF-8", "-g"), 
    javacOptions in Compile in doc := Seq("-encoding", "UTF-8"),
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    version <<= (wcsVersion) { x => x +  "_" + v },
    unmanagedJarsTask)

  val libdepsSettings = Seq(
     libraryDependencies <++= (wcsVersion) { x => Seq(
       "com.novocode" % "junit-interface" % "0.10",
           "com.sciabarra" % "agilesites-core" % (x + "_" + v),
           "com.sciabarra" % "agilesites-api" % (x + "_" + v) withSources())})
   

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = coreSettings ++ Seq(
      name := "agilesites-core",
      libraryDependencies ++= coreDependencies,
      publishArtifact in packageDoc := false,
      crossPaths := false,
      coreGeneratorTask,
      EclipseKeys.skipProject := true))

  // API
  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = coreSettings ++ Seq(
     name := "agilesites-api",   
     libraryDependencies <++= (wcsVersion) { x => 
       Seq("com.sciabarra" % "agilesites-core" % (x + "_" + v) )
     },
     publishArtifact in packageDoc := false,
     crossPaths := false,
     wcsPackageJarTask,
     EclipseKeys.skipProject := true,
     EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))

  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = coreSettings ++
      libdepsSettings ++ Seq(
      name := "agilesites-app",
      wcsGenerateIndexTask,
      wcsCopyHtmlTask,
      EclipseKeys.projectFlavor := EclipseProjectFlavor.Java))

  /// ALL
  lazy val all: Project = Project(
    id = "all",
    base = file("."),
 
    settings = coreSettings ++
      libdepsSettings ++
      assemblySettings ++ 
      net.virtualvoid.sbt.graph.Plugin.graphSettings ++
      scaffoldSettings ++ Seq(
        name := "agilesites-all",      
        wcsCsdtTask,
        wcsVirtualHostsTask,
        wcsCopyJarsWebTask,
        wcsCopyJarsLibTask,
        wcsHelloTask,
        wcsCatalogManagerTask,
        wcsSetupTask,
        wcsPopulateTask,
        wcsWebappSatelliteTask,
        wcsSetupSatelliteTask,
        wcsDeployTask,
        wcsCopyStaticTask,
        wcsAssemblyJarTask,
        wcsUpdateAssetsTask,
        wcsLogTask,
        wcsServeTask,
        excludedJars in assembly <<= (fullClasspath in assembly),
        watchSources ++= ((file("app") / "src" / "main" / "static" ** "*").get),
        EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala,
        EclipseKeys.skipProject := false,
        assembleArtifact in packageScala := false
      )) dependsOn (app) aggregate (app, api)
}
