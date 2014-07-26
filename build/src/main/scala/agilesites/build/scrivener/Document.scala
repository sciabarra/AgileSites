package agilesites.build.scrivener

import scala.xml.Text
import scala.xml.pull._
import scala.io.Source
import java.io.File

/**
 * Parse the index of a scrivener document
 */
class Document(sourceFolder: File) extends TreeBuilder with Utils {
  
  val source = file(sourceFolder, "project.scrivx")
  //println(source)
  
  var stack = node(-1, "Home") :: Nil
  var inTitle = false
  val src = Source.fromFile(source)

  val reader = new XMLEventReader(src)

  for (event <- reader) {
    event match {
      case EvElemStart(_, "BinderItem", attrs, _) =>
        val tree = node(attrs("ID").text.toInt, attrs("Type").text)
        stack = tree  :: stack
        println(">>> push "+tree)
        
      case EvElemEnd(_, "BinderItem") =>
        val child :: parent :: rest = stack
        //stack = parent.copy(children= node::parent.children) ::rest
        stack = addChild(parent, child) :: rest
        println("<<< pop "+child)
        
      case EvElemStart(_, "Title", _, _) => inTitle = true
     
      case EvElemEnd(_, "Title") => inTitle = false
   
      case EvText(text) => 
        println(text)
        if(inTitle) stack = setTitle(stack.head, text) :: stack.tail
        
      case x =>
        //println(x)
    }
  }
  
  def dump { treeDump(stack(0)) }

  //val map = treeMap(Map[String,Node](), "", stack(0))

}