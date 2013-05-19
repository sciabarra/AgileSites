package javadoc

import sbt._
import Keys._

object JavadocPlugin extends Plugin {
    
  val javadoc = TaskKey[File]("javadoc", "Generate java api docs from java sources.")
  val javadocSubpackages = TaskKey[Seq[String]]("javadoc-subpackages", "Package names to recursively document.")
  val javadocOptions = TaskKey[Seq[String]]("javadoc-options", "Extra options for Javadoc.")
   
  val javadocTask = javadoc <<= (baseDirectory, javaSource in Compile, javadocSubpackages, javadocOptions, streams) map { 
    (base, source, packages, options, s) =>
    val target = base / "javadoc"
    target.mkdirs
    println("javadoccing like a boss.")
    println("Target dir: "+target)
    println("Source dir: "+source)
    println("Options: "+options.mkString(" "))
    val cmd = <x>javadoc -sourcepath {source} -d {target} -subpackages {packages.mkString(":")} {options.mkString(" ")}</x>
    println("Executing: "+cmd.text)
    cmd ! s.log
    target
  }

  val javadocSettings = Seq(
    javadocOptions := Seq("-link", "http://download.oracle.com/javase/6/docs/api/"),
    javadocSubpackages <<= (javaSource in Compile) map {
      src => src.list().toSeq
    },
    javadocTask
  )
}
