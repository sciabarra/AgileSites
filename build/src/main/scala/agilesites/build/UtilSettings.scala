package agilesites.build

import sbt._
import Keys._

trait UtilSettings {
  this: Plugin =>

  val shellPromptTask = shellPrompt in ThisBuild := {
    state => Project.extract(state).currentRef.project + "> "
  }

  
  lazy val helloCommand =
    Command.command("hello") { (state: State) =>
      
      println("Hello")
      state
    }

  val utilSettings = Seq(shellPromptTask, commands += helloCommand)
}