package agilesites.build

import sbt._
import Keys._
import agilesites.build._
import jbake._

object AgileSitesPlugin extends Plugin
  with UtilSettings
  with JBakeSettings
  with ScrivenerSettings {

  lazy val jbakeScrivener = taskKey[Unit]("jbake-scrivener")
  val jbakeScrivenerTask = jbakeScrivener <<= (scrivenerExport, jbakeContent) map {
    (files, base) =>
      JBake.convert(file(base), files)
  }

  /*
  jbakeScrivenerTask <<= (scrivenerExport) map {
    converted =>
      //JBake.convert(file(jbakeContent.value), converted)
      ()
  }*/

  override lazy val settings =
    utilSettings ++
      jBakeSettings ++
      scrivenerSettings ++
      Seq(jbakeScrivenerTask)

}