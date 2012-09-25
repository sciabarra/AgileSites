import sbt._
import Keys._
import sys.process._

object ScalaWcsBuild extends Build {

  lazy val wcsHome = ScalaWcsSupport.wcsHome
  lazy val wcsWebapp = ScalaWcsSupport.wcsWebapp
  lazy val wcsCsdtJar = ScalaWcsSupport.wcsCsdtJar

  lazy val wcsUrl = ScalaWcsSupport.wcsUrl
  lazy val wcsSite = ScalaWcsSupport.wcsSite
  lazy val wcsUser = ScalaWcsSupport.wcsUser
  lazy val wcsPassword = ScalaWcsSupport.wcsPassword

  lazy val tagGeneratorTask = ScalaWcsSupport.tagGeneratorTask
  lazy val wcsSetupTask = ScalaWcsSupport.wcsSetupTask
  lazy val wcsCsdtTask = ScalaWcsSupport.wcsCsdtTask

  // parameters
  val commonDependencies = Seq(
    "org.slf4j" % "slf4j-api" % "1.6.6",
    "org.eintr.loglady" %% "loglady" % "1.0.0",
    "commons-logging" % "commons-logging" % "1.1.1",
    "javax.servlet" % "servlet-api" % "2.5",
    "org.specs2" %% "specs2" % "1.12.1" % "test",
    "ch.qos.logback" % "logback-classic" % "1.0.7" % "test")

  // jars to include as unmanaged
  val includeFilterUnmanagedJars = includeFilter in unmanagedJars := "commons-*" || "http-*" ||
    "cs-*" || "wem-sso-api-*" || "rest-api-*" || "cas-client-*" ||
    "ics.jar" || "cs.jar" || "xcelerate.jar" || "gator.jar" || "visitor.jar"

  val unmanagedBaseTask = unmanagedBase in Compile <<= wcsWebapp { base => file(base) / "WEB-INF" / "lib" }
  val unmanagedJarsTask = unmanagedJars in Compile <+= wcsCsdtJar map { jar => Attributed.blank(file(jar)) }

  // projects
  lazy val core: Project = Project(
    id = "core",
    base = file("core"),
    settings = Defaults.defaultSettings ++ Seq(
      scalaVersion := "2.9.1",
      organization := "org.scalawcs",
      name := "scalawcs-core",
      version := "0.1",
      libraryDependencies ++= commonDependencies,
      unmanagedBaseTask,
      includeFilterUnmanagedJars,
      unmanagedJarsTask,
      tagGeneratorTask))
      
      
  lazy val app: Project = Project(
    id = "app",
    base = file("."),
    settings = Defaults.defaultSettings ++ Seq(
      scalaVersion := "2.9.1",
      organization := "org.scalawcs",
      name := "scalawcs-app",
      version := "0.2",
      libraryDependencies ++= commonDependencies ++
        Seq("org.scalawcs" %% "scalawcs-core" % "0.1"),
      unmanagedBaseTask,
      unmanagedJarsTask,
      includeFilterUnmanagedJars,
      fork := true,
      wcsSetupTask,
      wcsCsdtTask))
}
