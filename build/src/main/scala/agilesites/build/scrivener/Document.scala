package agilesites.build.scrivener

import scala.xml.Text
import scala.xml.pull._
import scala.io.Source
import java.io.File
import agilesites.build.util.Utils
import sbt._

/**
 * Parse the index of a scrivener document
 */
class Document(sourceFolder: File, targetFolder: File) extends TreeBuilder with Utils {

  val source = sourceFolder / "project.scrivx"
  //println(source)

  var stack = node(-1, "Root") :: Nil
  var inTitle = false
  val src = Source.fromFile(source)

  val reader = new XMLEventReader(src)

  for (event <- reader) {
    event match {
      case EvElemStart(_, "BinderItem", attrs, _) =>
        val tpe = attrs("Type").text
        val id = attrs("ID").text.toInt
        val tree = node(id, tpe)
        stack = tree :: stack
      //println(">>> push " + tree)

      case EvElemEnd(_, "BinderItem") =>
        val child :: parent :: rest = stack
        stack = addChild(parent, child) :: rest
      //println("<<< pop " + child)

      case EvElemStart(_, "Title", _, _) => inTitle = true

      case EvElemEnd(_, "Title") => inTitle = false

      case EvText(text) =>
        //println(text)
        if (inTitle) stack = setTitle(stack.head, text) :: stack.tail

      case x =>
      //println(x)
    }
  }

  //println(stack(0))

  val root = {
    findNode(stack(0), _.kind == "DraftFolder").getOrElse(node(0, "Empty"))
    .copy(name = targetFolder.getName)
  }

  def dump {
    treeDump(root)
  }

  def fileNodeList: Seq[Tree] = fileByNode(root, targetFolder.getParentFile)
}