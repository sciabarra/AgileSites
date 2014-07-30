package agilesites.build.scrivener

import java.io.File

class DocumentConverter(sourceDocument: File, targetFolder: File)(unoPython: File, unoConv: File)
  extends Utils {

  val doc = new Document(sourceDocument, targetFolder)
  val source: File = file(file(sourceDocument, "Files"), "Docs")

  def rtf2html(rtf: File, tgtDir: File, title: String) {
    import scala.sys.process._

    val tgt = file(tgtDir, rtf.getName().split("\\.").head + ".html")

    val index = file(tgtDir, "index.html")

    if (rtf.exists()) {

      
      //println("rtf:" + rtf + "@" + rtf.lastModified + "\ntgt" + tgt + "@" + tgt.lastModified )
      // convert rtf to html
      if (!tgt.exists() || rtf.lastModified() > tgt.lastModified()) {
        val cmd = s""""${unoPython}" "${unoConv}" -f html -o "${tgtDir}" "${rtf}""""
        println(cmd)
        cmd !

      }

      // extract from html the jbake file format
      if (!index.exists || tgt.lastModified > index.lastModified) {
        println("html:" + index)
        html2jbake(title, tgt, index)
      }

    }

  }

  def html2jbake(title: String, input: File, output: File) {
    import org.jsoup.Jsoup
    val doc = Jsoup.parse(input, "UTF-8", "http://localhost:8181")
    val body = doc.select("BODY")
    val fw = new java.io.FileWriter(output)
    fw.write(s"""title=${title}
type=page
status=published
~~~~~~

""")
	fw.write(body.html)
    fw.close
  }

  def convert() {
    for ((tgtFile, node) <- doc.fileNodeList) {
      //println(tgtFile + " (" + node.kind + ")")
      if (node.kind == "Text") {
        val srcFile = new File(source, node.id + ".rtf")
        //println(s"${srcFile} -> ${tgtFile}")

        rtf2html(srcFile, tgtFile, node.title)
      }
    }
  }

  def dump = doc.dump

  //def get(path: String): Html = Html(loadRtfAsHtml(map.get(path)))

  //def kind(path: String): String = map.kind(path)

}