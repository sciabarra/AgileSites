package wcs.build

import sbt._
import Keys._
import sys.process._
import sbtassembly.Plugin._
import AssemblyKeys._
import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import scala.xml.transform.RewriteRule

object AgileWcsBuild extends Build with AgileWcsSupport {

  val v = "0.3"

  // remove then add those jars in setup
  val addFilterSetup = "scala-library*" || "agilewcs-core*" || "junit*" //|| "specs2*"
  
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
    "junit" % "junit" % "4.8.2",
    "com.novocode" % "junit-interface" % "0.8" % "test",
    "org.specs2" %% "specs2" % "1.13",
    "org.apache.httpcomponents" % "httpclient" % "4.1.2",
    "org.apache.httpcomponents" % "httpcore" % "4.1.2",
    "org.apache.httpcomponents" % "httpmime" % "4.1.2",
    "org.apache.james" % "apache-mime4j" % "0.5")

  val coreSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0",
    organization := "com.sciabarra",
    version <<= (wcsVersion) { x => v + "_" + x },
    includeFilterUnmanagedJars,
    unmanagedBaseTask,
    unmanagedJarsTask
    /*
    // ugly stuff to rename the generated project name adding the version number
    ,EclipseKeys.projectTransformerFactories := Seq(new EclipseTransformerFactory[RewriteRule] {
      import scala.xml.Node
      import com.typesafe.sbteclipse.core.Validation
      import scalaz.Scalaz._
      override def createTransformer(ref: ProjectRef, state: State): Validation[RewriteRule] = {
        val rule = new RewriteRule {
          override def transform(n: Node): Seq[Node] =
            if (n.label == "name")
              <name>{ n.text + "_" + v }</name>
            else n
        }
        rule.success
      }
    })*/)

  val commonSettings = coreSettings ++ Seq(
    libraryDependencies <++= (version) {
      x =>
        coreDependencies ++ Seq("com.sciabarra" %% "agilewcs-core" % x)
    })

  /// CORE
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = coreSettings ++ Seq(
      libraryDependencies ++= coreDependencies,
      publishArtifact in packageDoc := false,
      name := "agilewcs-core",
      coreGeneratorTask))

  // API
  lazy val api: Project = Project(
    id = "api",
    base = file("api"),
    settings = commonSettings ++ Seq(
      name := "agilewcs-api"))

  /// APP 
  lazy val app: Project = Project(
    id = "app",
    base = file("app"),
    settings = commonSettings ++ Seq(
      name := "agilewcs-app")) dependsOn (api)

  /// ALL
  lazy val all: Project = Project(
    id = "all",
    base = file("."),
    settings = commonSettings ++ assemblySettings ++ Seq(
      name := "agilewcs-all",
      wcsCsdtTask,
      wcsCmTask,
      wcsSetupTask,
      wcsDeployTask,
      wcsCopyStaticTask,
      wcsPackageJarTask,
      wcsUpdateAssetsTask,
      excludedJars in assembly <<= (fullClasspath in assembly),
      EclipseKeys.skipProject := true,
      assembleArtifact in packageScala := false)) dependsOn (app) aggregate (app, api)

}

