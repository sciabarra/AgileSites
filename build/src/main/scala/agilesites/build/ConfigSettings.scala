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
    sitesVersion    := asPropertyMap.value.getOrElse("sites.version", "11.1.1.8.0"),
    sitesHome       := asPropertyMap.value.getOrElse("sites.home", "../wcs/home"),
    sitesShared     := asPropertyMap.value.getOrElse("sites.shared", "../wcs/shared"),
    sitesWebapp     := asPropertyMap.value.getOrElse("sites.webapp", "../wcs/webapps/cs"),
    sitesUrl        := asPropertyMap.value.getOrElse("sites.url", "http://localhost:8181/cs"),
    sitesUser       := asPropertyMap.value.getOrElse("sites.user", "fwadmin"),
    sitesPassword   := asPropertyMap.value.getOrElse("sites.password", "xceladmin"))
}