package agilesites.build

import sbt._
import Keys._
import java.io.File

trait JBakeSettings {
  this: Plugin =>

  val jbakeConfig = config("jbake")

  val jbakeClasspath = taskKey[Seq[File]]("jbake-classpath")
  val jbakeClasspathTask = jbakeClasspath <<= (update) map {
    report => report.select(configurationFilter("jbake"))
  }

  val jbake = inputKey[Int]("jbake")
  val jbakeTask = jbake := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    val jvmOpts = Seq("-cp", jbakeClasspath.value.mkString(File.pathSeparator))

    val forkOpt = ForkOptions(
      runJVMOptions = jvmOpts,
      workingDirectory = Some(baseDirectory.value))

    //println(forkOpt)

    Fork.java(forkOpt, "org.jbake.launcher.Main" +: args)
  }

  //val scrivenerExport = InputKey[Unit]("scrivener-export") := {

  val jBakeSettings = Seq(
    jbakeClasspathTask,
    jbakeTask,
    ivyConfigurations += jbakeConfig,
    libraryDependencies ++= Seq(
      "org.jbake" % "jbake-core" % "2.3.1" % "jbake",
      "commons-io" % "commons-io" % "2.4" % "jbake",
      "commons-configuration" % "commons-configuration" % "1.9" % "jbake",
      "args4j" % "args4j" % "2.0.23" % "jbake",
      "org.freemarker" % "freemarker" % "2.3.20" % "jbake",
      "com.orientechnologies" % "orient-commons" % "1.6.2" % "jbake",
      "com.orientechnologies" % "orientdb-core" % "1.6.2" % "jbake",
      "junit" % "junit" % "4.10" % "jbake",
      "org.assertj" % "assertj-core" % "1.6.0" % "jbake",
      "org.pegdown" % "pegdown" % "1.4.2" % "jbake",
      "org.asciidoctor" % "asciidoctor-java-integration" % "0.1.4" % "jbake",
      "org.eclipse.jetty" % "jetty-server" % "8.1.12.v20130726" % "jbake",
      "org.codehaus.groovy" % "groovy-all" % "2.3.4" % "jbake",
      "org.thymeleaf" % "thymeleaf" % "2.1.3.RELEASE" % "jbake",
      "org.slf4j" % "slf4j-api" % "1.7.5" % "jbake",
      "ch.qos.logback" % "logback-classic" % "1.0.9" % "jbake",
      "ch.qos.logback" % "logback-core" % "1.0.9" % "jbake"))
}