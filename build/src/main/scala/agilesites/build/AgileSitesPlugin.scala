package agilesites.build

import sbt._
import Keys._

object AgileSitesPlugin extends Plugin {

  lazy	val asGreeting = SettingKey[String]("as-greeting")
  lazy	val asGreetings = InputKey[Unit]("as-greetings")
 
 
  val asGreetingsInput = asGreetings := {
  	val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
  	val msg = asGreeting.value
  	for(arg <- args) {
  		println(s"${msg}, ${arg}")
  	}

  }

  lazy val helloCmd =
    Command.command("as-hello") { (state: State) =>
      println("Hello")
      state
    }

   val shellPromptTask = shellPrompt in ThisBuild := { 
   	  state => Project.extract(state).currentRef.project + "> " 
   }
 

  override lazy val settings = Seq(
  	shellPromptTask,
    asGreeting := "Ciao", 
  	asGreetingsInput,
  	commands ++= Seq(helloCmd))

}