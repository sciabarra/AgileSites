package agilesites.build

import sbt._
import Keys._

trait UtilSettings {
  this: Plugin =>

  // where the property files are
  val asProperties = settingKey[Seq[String]]("AgileSites Property files")

  // read all the properties in a single property map
  lazy val asPropertyMap = settingKey[Map[String, String]]("AgileSites Property Map")
  val asPropertyMapTask = asPropertyMap := {

    import java.util.Properties
    import scala.collection.JavaConverters._

    val prp = new Properties

    for (prpFileName <- asProperties.value) {
      val prpFile = file(prpFileName)

      if (!prpFile.exists) {
        //log.info("not found property file " + prpFile)
      } else {
        System.out.println("loading " + prpFile)
        prp.load(new java.io.FileInputStream(prpFile))
      }
    }
    val map = prp.asScala.toMap
    for ((k, v) <- map)
      println(s"${k}=${v}")
    map
  }

  // display a prompt with the project name
  val asShellPromptTask = shellPrompt in ThisBuild := {
    state => Project.extract(state).currentRef.project + "> "
  }

  // sample command
  lazy val asHelloCommand =
    Command.command("as-hello") { (state: State) =>
      println("Hello")
      state
    }

  val utilSettings = Seq(asShellPromptTask,
    commands += asHelloCommand,
    asProperties := Seq("agilesites.properties"),
    asPropertyMapTask)
}