package agilesites.build

import sbt._
import Keys._

object AgileSitesPlugin extends Plugin
  with UtilSettings
  with ScrivenerSettings
  with JBakeSettings {

  override lazy val settings =
    utilSettings ++
      scrivenerSettings ++
      jBakeSettings
}