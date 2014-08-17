name := "agilesites2-sites"

version := "1.9"

scalaVersion := "2.10.4"

offline  := true

externalResolvers := externalResolvers.value filter { _.name == "local" }

packageBin := file("bin") / "setup.jar"

val deploy = taskKey[Unit]("deploy to bin/setup.jar")

deploy := {  
   val src = (Keys.`package` in Compile).value 
   val dst = file("bin") / "setup.jar"
   IO.copyFile(src, dst) 
   println(s"+++ ${dst} (${src})")
}
