package agilesites.build

import sbt._
import Keys._
import agilesites.build.scrivener.Scrivener
import agilesites.build.util.JBake
import scala.language.postfixOps
 

trait ScrivenerSettings {

  this: Plugin =>

  val scrivenerExport = taskKey[Seq[java.io.File]]("scrivener-export") 
  val scrivenerExportTask = scrivenerExport := {

    //val args: Seq[String] = Def.spaceDelimited("<arg>").parsed

    val src = file(scrivenerSourceDocumentKey.value)
    val tgt = file(scrivenerOutputFolderKey.value)
    val upyth = file(unoPythonKey.value)
    val uconv = file(unoConvKey.value)

    Scrivener.export(src, tgt)(upyth, uconv)
  }

  // keys and settings with defaults

  lazy val unoPythonKey = settingKey[String]("uno-python")
  lazy val unoConvKey = settingKey[String]("uno-conv")
  lazy val scrivenerSourceDocumentKey = settingKey[String]("scrivener-source-document")
  lazy val scrivenerOutputFolderKey = settingKey[String]("scrivener-output-folder")

  val scrivenerSettings = Seq(
    scrivenerOutputFolderKey := "output",
    scrivenerSourceDocumentKey := "content.scriv",
    unoPythonKey := """C:\Program Files (x86)\LibreOffice 4\program\python.exe""",
    unoConvKey := """C:\Program Files (x86)\LibreOffice 4\\unoconv""",
    scrivenerExportTask)
}