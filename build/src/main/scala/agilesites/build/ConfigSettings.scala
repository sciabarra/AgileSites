package agilesites.build

import sbt._
import Keys._

trait ConfigSettings {
  this: Plugin with UtilSettings =>

  val sitesVersion  = settingKey[String]("Sites or Fatwire Version Number")
  val sitesHome     = settingKey[String]("Sites Home Directory")
  val sitesShared   = settingKey[String]("Sites Shared Directory")
  val sitesWebapp   = settingKey[String]("Sites Webapp Directory")
  val sitesUrl      = settingKey[String]("Sites URL")
  val sitesUser     = settingKey[String]("Sites user ")
  val sitesPassword = settingKey[String]("Sites user password")

  val configSettings = Seq(
    sitesVersion    := asPropertyMap.value("sites.version"),
    sitesHome       := asPropertyMap.value("sites.home"),
    sitesShared     := asPropertyMap.value("sites.shared"),
    sitesWebapp     := asPropertyMap.value("sites.webapp"),
    sitesUrl        := asPropertyMap.value("sites.url"),
    sitesUser       := asPropertyMap.value("sites.user"),
    sitesPassword   := asPropertyMap.value("sites.password"))
}