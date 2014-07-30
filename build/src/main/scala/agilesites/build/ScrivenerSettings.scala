package agilesites.build

import sbt._
import Keys._
import agilesites.build.scrivener.DocumentConverter
import agilesites.build.scrivener.Scrivener

trait ScrivenerSettings {

  this: Plugin =>

  val scrivenerExport = InputKey[Unit]("scrivener-export") := {
    val args: Seq[String] = Def.spaceDelimited("<arg>").parsed

    val src = file(scrivenerSourceDocumentKey.value)
    val tgt = file(scrivenerTargetFolderKey.value)
    val upyth = file(unoPythonKey.value)
    val uconv = file(unoConvKey.value)

    val result = Scrivener.export(src, tgt)(upyth, uconv)

    println(result)
  }

  // keys and settings with defaults

  lazy val unoPythonKey = settingKey[String]("uno-python")
  lazy val unoConvKey = settingKey[String]("uno-conv")
  lazy val scrivenerSourceDocumentKey = settingKey[String]("scrivener-source-document")
  lazy val scrivenerTargetFolderKey = settingKey[String]("scrivener-target-folder")

  val scrivenerSettings = Seq(
    scrivenerTargetFolderKey := "content",
    scrivenerSourceDocumentKey := "content.scriv",
    unoPythonKey := """C:\Program Files (x86)\LibreOffice 4\program\python.exe""",
    unoConvKey := """C:\Program Files (x86)\LibreOffice 4\\unoconv""",
    scrivenerExport)

}