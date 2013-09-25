package wcs.build

import java.io.File

class AssetCompilerManager(site: String, base: File, xmlDir: Option[File] = None, javaDir: Option[File]= None) extends Utils {
  
  def file(s: String) = new File(s)
  def file(f: File, s: String) = new File(f, s)

  val nsite = normalizeSiteName(site)

  val xmlBase = xmlDir getOrElse  file(file(file(base, "export"), "stargaze"), nsite)
  val javaBase = javaDir getOrElse file(file(file(file(file(file(base, "app"), "src"), "main"), "java"), nsite), "content")

  def compile(c: String, cid: Long) = {

    val xmlDir = file(xmlBase, c)
    val xmlFile = "%d.xml".format(cid)

    val javaDir = file(javaBase, normalizeSiteName(c))
    val javaFile = "%s%d.java".format(c, cid)

    javaDir.mkdirs()
    val ac = new AssetCompiler(site, c, cid, file(xmlDir, xmlFile), file(javaDir, javaFile))
    ac.compile

  }

  def compileAll() = {
    for {
      dir <- xmlBase.listFiles.filter(_.isDirectory)
      id <- dir.listFiles.filter(_.getName.endsWith("xml")).map(_.getName.dropRight(4).toLong)
    } yield compile(dir.getName, id)
  }

  def compileDir(c:String) = {
    val xmlDir = file(xmlBase, c)
    for { a <- xmlDir.list.filter(_.endsWith(".xml")) } yield {
      compile(c, a.toLong)
    }
  }



  def compileAll() = {
    for {
      dir <- xmlBase.listFiles.filter(_.isDirectory)
      id <- dir.listFiles.filter(_.getName.endsWith("xml")).map(_.getName.dropRight(4).toLong)
    } yield compile(dir.getName, id)
  }



}