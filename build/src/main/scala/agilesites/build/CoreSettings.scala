package agilesites.build

import sbt._
import Keys._
import agilesites.build.util.TagWrapperGenerator

trait CoreSettings {
  this: Plugin with ConfigSettings =>

  val asCoreWrapperGeneratorTask = Def.task {

    val dstDir = (sourceManaged in Compile).value
    val srcDir = sitesWebapp.value
    val base = baseDirectory.value
    val version = sitesVersion.value

    // generate tags
    val tlds = file(srcDir) / "WEB-INF" / "futuretense_cs"
    val l = if (tlds.isDirectory) for {
      tld <- tlds.listFiles
      if tld.getName.endsWith(".tld")
      val src = tld.getAbsolutePath
      val clsj = TagWrapperGenerator.tld2class(src)
      val dstj = file(dstDir / clsj + ".java")
      // if tld.getName.equalsIgnoreCase("asset.tld") // select only one for debug generator
    } yield {
      if (!dstj.exists) {
        val bodyj = TagWrapperGenerator(src)
        IO.write(dstj, bodyj)
        println("+++ " + dstj)
      }
      dstj
    }
    else Array[File]()

    // copy versioned class
    val src = base / "src" / "main" / "version" / version
    if (!src.isDirectory)
      throw new RuntimeException("wrong path in build.sbt or unsupported version ")

    val ll = for {
      file <- src.listFiles
      dfile = dstDir / file.getName
    } yield {
      println("+++ " + dfile)
      IO.copyFile(file, dfile)
      dfile
    }

    // return files generated and copied
    l.toSeq ++ ll.toSeq
  }

}