//addSbtPlugin("com.sciabarra" % "agilesites2-build" % "1.9-M3")
val plugin = project.in(file(".")).dependsOn( (file("..") / "build").toURI)

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")






