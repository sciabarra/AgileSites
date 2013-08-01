package wcs.build

import java.io.File
import wcs.core.WCS

class AssetCompilerManager(site: String, base: File) {

  def file(s: String) = new File(s)
  def file(f: File, s: String) = new File(f, s)
  
  val nsite = WCS.normalizeSiteName(site)
  val xmlBase = file(file(file(base, "export"), "stargaze"), nsite)
  val javaBase = file(file(file(file(file(file(base, "app"), "src"), "main"), "java"), nsite), "content")
  
  def compile(c: String, cid: Long) = {

    val xmlDir = file(xmlBase, c)
    val xmlFile = "%d.xml".format(cid)
    
    val javaDir = file(javaBase, WCS.normalizeSiteName(c))
    val javaFile = "%s%d.java".format(c,cid)
       
    javaDir.mkdirs()
    val ac = new AssetCompiler(site, c, cid, file(xmlDir, xmlFile), file(javaDir, javaFile))    
    ac.compile
   
  }

}