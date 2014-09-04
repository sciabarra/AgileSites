val libDeps = Seq(
   "org.scalatest" %% "scalatest" % "2.2.0" % "test",
   "org.jsoup" % "jsoup" % "1.7.3",
   "commons-httpclient" % "commons-httpclient" % "3.1",
   "commons-io" % "commons-io" % "2.4",
   "org.clapper" % "scalasti_2.10" % "1.0.0",
   "com.jcraft" % "jsch" % "0.1.51",
   "org.scalafx" %% "scalafx" % "1.0.0-R8",
   "org.scalafx" %% "scalafxml-core" % "0.2") 

// dependencies need to preload in the local repo
val tomcat = config("tomcat")

val tomcatVersion = "7.0.52"

val hsqlVersion = "1.8.0.10"

def tomcatDeps(tomcatConfig: String) = Seq(
    "org.apache.tomcat" % "tomcat-catalina" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat" % "tomcat-dbcp" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-logging-log4j" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion % tomcatConfig,
    "org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion % tomcatConfig,
    "org.hsqldb" % "hsqldb" % hsqlVersion % tomcatConfig)

val btsettings = bintrayPublishSettings ++ Seq(
	bintray.Keys.bintrayOrganization in bintray.Keys.bintray := Some("sciabarra"),
	bintray.Keys.repository in bintray.Keys.bintray := "sbt-plugins",
	licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),
	publishMavenStyle := false,
	publishArtifact in packageDoc := false,
	publishArtifact in Test := false)

val mysettings = Seq(name := "agilesites2-build",
	organization := "com.sciabarra",
	sbtPlugin := true,
	version := "1.9-M4",
	scalaVersion := "2.10.4",
	scalacOptions += "-deprecation",
	libraryDependencies ++= libDeps ++ tomcatDeps("tomcat"))

val root = project.in(file(".")).
	configs(tomcat).
	settings(btsettings: _*).
	settings(mysettings : _*)
