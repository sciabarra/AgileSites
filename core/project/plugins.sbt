lazy val plugin = file("..") / "build"

lazy val root = project.in(file(".")).dependsOn(plugin.toURI)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")
