scalaVersion := "2.10.4"

name := "agilesites2-setup"

version := "2.0"

unmanagedJars in Compile += Attributed.blank( file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")

libraryDependencies += "org.scalafx" %% "scalafx" % "1.0.0-R8"

libraryDependencies += "org.scalafx" %% "scalafxml-core" % "0.2"

fork  := true

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)

jfxSettings

JFX.mainClass := Some("agilesites.setup.Setup")

JFX.artifactBaseNameValue := "setup"