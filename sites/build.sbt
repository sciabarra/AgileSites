val v = "1.9-M1"

val deploy = taskKey[Unit]("deploy to bin/setup.jar")

deploy := {  
   val src = (Keys.`package` in Compile).value 
   val dst = file("bin") / "setup.jar"
   IO.copyFile(src, dst) 
   println(s"+++ ${dst} (${src})")
}

val sites = project.in(file(".")).settings(assemblySettings: _*).settings(
	name := "agilesites2-sites",
	version := v,
	scalaVersion := "2.10.4",
	autoScalaLibrary :=  false,
	offline  := true,
	libraryDependencies ++= tomcatDeps("compile"),
	externalResolvers := externalResolvers.value filter { _.name == "local" },
	packageBin := file("bin") / "setup.jar")
	
val core118 = project.in(file("tmp")/"core118").settings(assemblySettings: _*).settings(
	AssemblyKeys.outputPath := file("bin"),
	autoScalaLibrary := false,
	libraryDependencies += "com.sciabarra" % "agilesites2-core" % ("11.1.1.8.0" + "_" + v))

val api118 = project.in(file("tmp")/"api118").settings(assemblySettings: _*).settings(
	AssemblyKeys.outputPath := file("bin"),
	autoScalaLibrary := false,
	libraryDependencies += "com.sciabarra" % "agilesites2-api" % ("11.1.1.8.0" + "_" + v))

val core116 = project.in(file("tmp")/"core116").settings(assemblySettings: _*).settings(
	AssemblyKeys.outputPath := file("bin"),
	autoScalaLibrary := false,
	libraryDependencies += "com.sciabarra" % "agilesites2-core" % ("11.1.1.6.0" + "_" + v))

val api116 = project.in(file("tmp")/"api116").settings(assemblySettings: _*).settings(
    AssemblyKeys.outputPath := file("bin"),
    autoScalaLibrary := false, 
	libraryDependencies += "com.sciabarra" % "agilesites2-api" % ("11.1.1.6.0" + "_" + v))

val core762 = project.in(file("tmp")/"core762").settings(assemblySettings: _*).settings(
    AssemblyKeys.outputPath := file("bin"),
    autoScalaLibrary := false,
	libraryDependencies += "com.sciabarra" % "agilesites2-core" % ("7.5.0" + "_" + v))

val api762 = project.in(file("tmp")/"api762").settings(assemblySettings: _*).settings(
    AssemblyKeys.outputPath := file("bin"),
    autoScalaLibrary := false,
	libraryDependencies += "com.sciabarra" % "agilesites2-api" % ("7.5.0" + "_" + v))
