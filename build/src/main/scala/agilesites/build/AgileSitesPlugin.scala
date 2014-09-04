package agilesites.build

import sbt._
import Keys._

object AgileSitesPlugin
  extends Plugin
  with ConfigSettings
  with UtilSettings
  with ToolsSettings
  with TomcatSettings
  with SetupSettings
  with DeploySettings 
  with ScaffoldSettings
  //with JBakeSettings
  //with ScrivenerSettings 
  {

  override lazy val settings =
    configSettings ++
      utilSettings ++
      toolsSettings ++
      tomcatSettings ++
      deploySettings ++
      scaffoldSettings
  // val bookSettings = jBakeSettings ++ scrivenerSettings
}