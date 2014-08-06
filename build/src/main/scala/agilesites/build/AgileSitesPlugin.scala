package agilesites.build

import sbt._
import Keys._

object AgileSitesPlugin
  extends Plugin
  with UtilSettings
  with ConfigSettings
  with CoreSettings
  with ToolsSettings
  with JBakeSettings
  with ScrivenerSettings {

  lazy val jbakeScrivener = taskKey[Unit]("jbake-scrivener")
  val jbakeScrivenerTask = jbakeScrivener <<= (scrivenerExport, jbakeContent) map {
    (files, base) =>
      agilesites.build.util.JBake.convert(file(base), files)
  }

  //jbakeScrivenerTask <<= (scrivenerExport) map {
  // converted =>
  //   JBake.convert(file(jbakeContent.value), converted)
  //}

  override lazy val settings =
    utilSettings ++
      configSettings ++
      jBakeSettings ++
      scrivenerSettings ++
      toolSettings

}