package agilesites.build.scrivener

import java.io.File
import java.io.FileReader
import javax.swing.JEditorPane
import java.io.StringWriter
import scala.io.Source

// C:\Program Files (x86)\LibreOffice 4\program\python.exe a:\seabird\seabird-blog\script\\unoconv

/**
 * Convert a source rtf from a Scrivener document file in a html file using Libre Office 
 */
class Converter(unoPython: File, unoConv: File, sourceDocument: File, targetFolder: File) extends Utils {

  val target = targetFolder
  val source: File = file(file(sourceDocument, "Files"), "Docs")

  val canConvertIt = unoPython.exists

  def loadRtfAsHtml(filename: String) = {

    val fileRtf = file(source, filename)
    val fileHtml = file(target, filename.split("\\.")(0) + ".html")

    if (canConvertIt)
      if (!fileHtml.exists || fileRtf.lastModified > fileHtml.lastModified)
        rtf2html(fileRtf)

    if (fileHtml.exists)
      readFile(fileHtml)
    else s"ERROR: missing ${fileHtml}"
  }

  def rtf2html(rtf: File) {
    import scala.sys.process._

    val cmd = s""""${unoPython}" "${unoConv}" -f html -o "${target}" "${rtf}""""
    println(cmd)
    cmd !
  }
}