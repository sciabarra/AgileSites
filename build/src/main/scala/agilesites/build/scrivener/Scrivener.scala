package agilesites.build.scrivener

import java.io.File

/**
 * Scrivener export support
 *
 */
object Scrivener extends TreeBuilder {

  def export(sourceDocument: File, outputFolder: File)(unoPython: File, unoConv: File): Seq[File] = {
    val dc = new DocumentConverter(sourceDocument, outputFolder)(unoPython, unoConv)

    // show current state
    dc.dump

    // convert and return a list of converted files
    dc.convert
  }

}