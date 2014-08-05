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
        //log.info("loading "+prpFile)
        prp.load(new java.io.FileInputStream(prpFile))
      }
    }
    if(prp.size == 0)
      throw new Error("FATAL: agilesites.properties not found")  
    prp.asScala.toMap
  }

  val asShellPromptTask = shellPrompt in ThisBuild := {
    state => Project.extract(state).currentRef.project + "> "
  }

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