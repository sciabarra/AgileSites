package agilesites.build.scrivener
/**
 * Build a tree 
 */
trait TreeBuilder {

  case class Tree(id: Int, ty: String, children: List[Tree] = Nil, name: String = "", title: String = "")

  def node(id: Int, ty: String) = Tree(id, ty)

  def addChild(parent: Tree, child: Tree) = parent.copy(children = (parent.children ++ (child :: Nil)))

  def setTitle(tree: Tree, _title: String) = tree.copy(name = _title.toLowerCase().replace(" ", "-"), title = _title)
  
  def treeDump(node: Tree, level: Int = 1) {
    println(("  " * level) + s"[${node.id}:${node.ty}] ${node.title}")
    for (child <- node.children)
      treeDump(child, level + 1)
  }
  
  /*
  def treeMap(map: Map[String, Tree], path: String, tree: Tree): Map[String, Tree] = {
    val npath = path + "/" + tree.name
    var nmap = map + (npath -> tree)
    for (child <- tree.children)
      nmap = treeMap(nmap, npath, child)
    nmap
  }
  */
}
