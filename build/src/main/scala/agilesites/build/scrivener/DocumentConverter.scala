package agilesites.build.scrivener

import java.io.File

class DocumentConverter(sourceDocument: File, targetFolder: File)(unoPython: File, unoConv: File)
  extends Converter(unoPython, unoConv, sourceDocument, targetFolder)
  with Utils {

  val doc = new Document(sourceDocument)
 
  def dump = doc.dump
  
  //def get(path: String): Html = Html(loadRtfAsHtml(map.get(path)))

  //def kind(path: String): String = map.kind(path)

}