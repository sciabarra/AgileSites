addSbtPlugin("com.sciabarra" % "agilesites2-build" % "1.9-M7")
//val plugin = project.in(file(".")).dependsOn( (file("..") / "build").toURI)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.1.2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")


