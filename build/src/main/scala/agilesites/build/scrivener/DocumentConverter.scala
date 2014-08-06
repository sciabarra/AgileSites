package agilesites.build.scrivener

import java.io.File

class DocumentConverter(sourceDocument: File, targetFolder: File)(unoPython: File, unoConv: File)
  extends Utils with TreeBuilder {

  val doc = new Document(sourceDocument, targetFolder)
  val source: File = file(file(sourceDocument, "Files"), "Docs")

  /**
   * Convert a rtf file in an html file via Libreoffice - only if source not modified
   */
  def rtf2html(rtf: File, tgtDir: File, title: String) = {
    import scala.sys.process._

    val tgt = file(tgtDir, rtf.getName().split("\\.").head + ".html")

    if (rtf.exists()) {

      //println("rtf:" + rtf + "@" + rtf.lastModified + "\ntgt" + tgt + "@" + tgt.lastModified )
      // convert rtf to html
      if (!tgt.exists() || rtf.lastModified() > tgt.lastModified()) {
        val cmd = s""""${unoPython}" "${unoConv}" -f html -o "${tgtDir}" "${rtf}""""
        println(cmd)
        cmd.!
      }
    }
    
    tgt

  }

 
  def convert = {
    for {
      node <- doc.fileNodeList
      if node.kind == "Text"
    } yield {
      //println(tgtFile + " (" + node.kind + ")")
      val tgtFile = node.file.get
      val srcFile = new File(source, node.id + ".rtf")
      //println(s"${srcFile} -> ${tgtFile}")
      val r = rtf2html(srcFile, tgtFile, node.title)
      //node.copy(file = Some(r)).asInstanceOf[Tree]
      r
    }
  }

  def dump = doc.dump

  //def get(path: String): Html = Html(loadRtfAsHtml(map.get(path)))

  //def kind(path: String): String = map.kind(path)

}