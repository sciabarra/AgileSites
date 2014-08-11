package agilesites.build

import sbt._
import Keys._

object AgileSitesPlugin
  extends Plugin
  with UtilSettings
  with ConfigSettings
  with ToolsSettings 
  with TomcatSettings
  //with JBakeSettings
  //with ScrivenerSettings 
  {

  override lazy val settings =
    utilSettings ++
      configSettings ++
      toolsSettings ++
      tomcatSettings

  // val bookSettings = jBakeSettings ++ scrivenerSettings
}