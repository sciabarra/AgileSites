package agilesites.build.scrivener

import java.io.File

/**
 * Scrivener export support
 *
 */
object Scrivener {

  def export(sourceDocument: File, targetFolder: File)(unoPython: File, unoConv: File) = {
    val dc = new DocumentConverter(sourceDocument, targetFolder)(unoPython, unoConv)

    dc.dump

    dc.convert
    
  }

}