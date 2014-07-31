package agilesites.build.scrivener

import java.io.File
/**
 * Build a tree
 */
trait TreeBuilder {

  case class Tree(id: Int, kind: String, children: List[Tree] = Nil, name: String = "", title: String = "", file: Option[File]= None)

  def node(id: Int, kind: String) = Tree(id, kind)

  def addChild(parent: Tree, child: Tree) = parent.copy(children = (parent.children ++ (child :: Nil)))

  def setTitle(tree: Tree, _title: String) =
     tree.copy(name = _title.toLowerCase().replace(" ", "-"), title = _title)

  /**
   * Dump the tree
   */
  def treeDump(node: Tree, level: Int = 1) {
    println(("  " * level) + s"[${node.id}:${node.kind}] ${node.title}")
    for (child <- node.children)
      treeDump(child, level + 1)
  }

  /**
   * Find a node matching a condition
   */
  def findNode(node: Tree, select: Tree => Boolean): Option[Tree] = {
    if (select(node))
      Some(node)
    else {
      val l = for (child <- node.children)
        yield findNode(child, select)
      val ll = l.filter(_.isDefined)
      if (ll.size == 0)
        None
      else
        ll(0)
    }
  }

  /**
   * Construct a list of files with references to nodes
   */
  def fileByNode(node: Tree, parent: File): Seq[Tree] = {
    val dir = new File(parent, node.name)
    val file: File = new File(parent, node.name)
    if (node.children.size == 0) {
      //Seq((file, node))
      Seq(node.copy(file=Some(file)))
    } else {
      val ls = for {
        child <- node.children
        sub <- fileByNode(child, dir)
      } yield {
        sub
      }
      ls ++ Seq(node.copy(file=Some(file)))
    }
  }
}
