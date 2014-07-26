package agilesites.build.scrivener

object Playground extends agilesites.build.scrivener.Utils {

  val base = """A:\agilesites\2.0\"""

  val src = file(base + """book\content.scriv""")

  val tgt = file(base + """book\content""")

  val conv = file(file(file(file(base), "build"), "bin"), "unoconv")

  val python = """C:\Program Files (x86)\LibreOffice 4\program\python.exe"""

}