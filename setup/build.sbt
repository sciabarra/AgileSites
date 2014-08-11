name := "agilesites2-setup"

version := "1.9"

scalaVersion := "2.10.4"

offline  := true

fork  := true

externalResolvers := externalResolvers.value filter { _.name == "local" }

unmanagedJars in Compile += Attributed.blank( file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")
	
addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)

jfxSettings

JFX.mainClass := Some("agilesites.setup.Setup")

JFX.artifactBaseNameValue := "setup"