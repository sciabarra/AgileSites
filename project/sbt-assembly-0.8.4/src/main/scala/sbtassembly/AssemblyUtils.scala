package sbtassembly

import sbt._

object AssemblyUtils {
  private val PathRE = "([^/]+)/(.*)".r

  /** Find the source file (and possibly the entry within a jar) whence a conflicting file came.
   * 
   * @param tempDir The temporary directory provided to a `MergeStrategy`
   * @param f One of the files provided to a `MergeStrategy`
   * @return The source jar or dir; the path within that dir; and true if it's from a jar.
   */
  def sourceOfFileForMerge(tempDir: File, f: File): (File, File, String, Boolean) = {
    val baseURI = tempDir.getCanonicalFile.toURI
    val otherURI = f.getCanonicalFile.toURI
    val relative = baseURI.relativize(otherURI)
    val PathRE(head, tail) = relative.getPath
    val base = tempDir / head

    if ((tempDir / (head + ".jarName")) exists) {
      val jarName = IO.read(tempDir / (head + ".jarName"), IO.utf8)
      (new File(jarName), base, tail, true)
    } else {
      val dirName = IO.read(tempDir / (head + ".dir"), IO.utf8)
      (new File(dirName), base, tail, false)
    } // if-else
  }
}
