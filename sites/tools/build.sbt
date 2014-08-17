name := "agilesites2-tools"

version := "1.9"

scalaVersion := "2.10.4"

fork  := true

jfxSettings

unmanagedJars in Compile += Attributed.blank( file(scala.util.Properties.javaHome) / "lib" / "jfxrt.jar")

addCompilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full)


libraryDependencies ++= Seq(
	"org.scalafx" %% "scalafx" % "1.0.0-R8",
	"org.scalafx" %% "scalafxml-core" % "0.2")
	

JFX.mainClass := Some("agilesites.tools.Main")

JFX.artifactBaseNameValue := "tools"

val deploy = taskKey[Unit]("deploy the tools to his appropriate location")

deploy := {
	JFX.packageJavaFx.value 
   val srcDir = file("target") / "scala-2.10" / "tools"  
   val dstDir = file("..") / "bin" 
   IO.copyFile(srcDir / "tools.jar", dstDir / "tools.jar")   
   IO.copyDirectory(srcDir / "lib", dstDir / "lib")
}
