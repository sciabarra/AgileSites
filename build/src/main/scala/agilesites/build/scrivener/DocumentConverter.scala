package agilesites.build.scrivener

import java.io.File

class DocumentConverter(sourceDocument: File, targetFolder: File)(unoPython: File, unoConv: File)
  extends Utils {

  val doc = new Document(sourceDocument)
  val source: File = file(file(sourceDocument, "Files"), "Docs")

  def rtf2html(rtf: File, target: File) {
    import scala.sys.process._
    val cmd = s""""${unoPython}" "${unoConv}" -f html -o "${target}" "${rtf}""""
    println(cmd)
    cmd !
  }

  def convert() {
    for ((tgtFile, node) <- doc.fileList(targetFolder)) {
      println(tgtFile + " (" + node.kind + ")")
      if (node.kind == "Text") {
        val srcFile = new File(source, node.id + ".rtf")
        rtf2html(srcFile, tgtFile)
      }
    }
  }

  def dump = doc.dump

  //def get(path: String): Html = Html(loadRtfAsHtml(map.get(path)))

  //def kind(path: String): String = map.kind(path)

}