package agilesites.build.util

import java.io.File
import agilesites.build.scrivener.TreeBuilder
import org.jsoup.Jsoup
import scala.Array.canBuildFrom

object JBake extends TreeBuilder {

  /**
   * Convert all the html files in JBake ready format
   * (removing head and adding the prefix)
   *
   */
  def convert(outputFolder: File, inputFiles: Seq[File]) {
    for (in <- inputFiles) {

      val ls = in.toString().split(File.separatorChar)

      val ls1 = (ls.tail.init) ++ Array("index.html")

      val out = ls1.foldLeft(outputFolder)(new File(_, _))

      html2jbake(in, out)
    }
  }

  /**
   * Convert an html to jbake format (if not modified)
   */

  def html2jbake(input: File, output: File) {
    import org.jsoup.Jsoup

    if (!output.exists || input.lastModified > output.lastModified) {
      val doc = Jsoup.parse(input, "UTF-8", "http://localhost:8181")
      val title = doc.select("TITLE").text
      val body = doc.select("BODY").html()

      val html = s"""title=${title}
type=page
status=published
~~~~~~

${body}
"""

      output.getParentFile().mkdirs()
      val fw = new java.io.FileWriter(output)
      fw.write(html)
      fw.close
    }
  }
}