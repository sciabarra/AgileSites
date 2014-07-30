//addSbtPlugin("com.sciabarra" % "agilesites2-build" % "2.0-SNAPSHOT")

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"

lazy val plugin = file("..") / "build"

lazy val root = project.in(file(".")).dependsOn(plugin.toURI)


addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")


