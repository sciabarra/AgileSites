package agilesites.build

import sbt._
import Keys._
import agilesites.build.util.TagWrapperGenerator

trait ToolsSettings {
  this: Plugin =>

  lazy val sitesTagWrapperGen = inputKey[Unit]("Generate Tag Wrappers")
  lazy val sitesTagWrapperGenTask = sitesTagWrapperGen := {

    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed
    if (args.size == 0)
      println("usage: siteTagWrapperGen <sites-webapp-folder>")
    else {
      val tldDir = file(args.head) / "WEB-INF" / "futuretense_cs"
      if (!tldDir.isDirectory)
        println("no tld founds in " + tldDir)
      else {
        val dstDir = (javaSource in Compile).value / "wcs" / "core" / "tags"
        for {
          tld <- tldDir.listFiles
          if tld.getName.endsWith(".tld")
          val src = tld.getAbsolutePath
          val clsj = TagWrapperGenerator.tld2class(src)
          val dstj = file(dstDir / clsj + ".java")
        } yield {
          val bodyj = TagWrapperGenerator(src)
          IO.write(dstj, bodyj)
          println("+++ " + dstj)
          dstj
        }
      }
    }
  }

  val toolSettings = Seq(sitesTagWrapperGenTask)
}