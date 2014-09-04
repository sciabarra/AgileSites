package agilesites.build

import sbt._
import Keys._
import scala.io.Source
import agilesites.build.util.G8Helpers

trait ScaffoldSettings {
  this: Plugin =>

  lazy val asTemplatePath = SettingKey[String]("AgileSites Generator Templates")

  lazy val asGenerate = inputKey[Unit](
    """|
         |Usage:
         | wcs-generate <template_name>
         |
         |The name of the template is the name of the folder located directly under `project/template`
         |Assuming your `template` folder has the following structure:
         |
         |    template
         |    |_ cselement
         |    |_ template
         |    |_ layout
         |
         |You have 3 different generations available.
         |
         |To generate a new template, just type `asGenerate <template>`.
         |It will ask for the variable values, and generate the correct code.
         |""".stripMargin)

  import complete._
  import complete.DefaultParsers._

  val parser: Def.Initialize[State => Parser[String]] =
    (baseDirectory) { (b) =>
      (state: State) =>
        val folder = b / "project" / "template"

        val templates = Option(folder.listFiles).toList.flatten
          .filter(f => f.isDirectory && !f.isHidden)
          .map(_.getName: Parser[String])

        (Space.+) ~> templates.reduce(token(_) | token(_))
    }

  /*
  val asGenerateTask = asGenerate <<= InputTask(parser) { (argTask: TaskKey[String]) =>
    (baseDirectory, argTask) map { (b, name) =>
      val folder = b / "project" / "template"
      val res = G8Helpers.applyRaw(folder / name, b, Nil)
      //println(ls)
      res.fold(
        e => sys.error(e),
        r => println("***\n*** asGenerate successful\n*** remember to asDeploy the new templates/cselements\n***"))
    }
  }
  */

  val asGenerateTask = asGenerate := {
    val b = baseDirectory.value
    val name = parser.parsed
    val folder = b / "project" / "template"
    val res = G8Helpers.applyRaw(folder / name, b, Nil)
    //println(ls)
    res.fold(
      e => sys.error(e),
      r => println("***\n*** asGenerate successful\n*** remember to asDeploy the new templates/cselements\n***"))
  }

  lazy val scaffoldSettings = Seq(asGenerateTask)

}