package agilesites.build.scrivener

import org.scalatest._

import java.io.File

class TreeBuilderTest extends FlatSpec with Matchers with TreeBuilder {
  val n = setTitle(node(0, "hello"), "Hello")
  val m = setTitle(node(1, "world"), "World")
  val m1 = setTitle(node(3, "world1"), "World1")
  val n1 = addChild(n, m)
  val n2 = addChild(addChild(n, m), m1)

  "a tree" should "construct" in {
    assert(n.id === 0)
    assert(n.children.size === 0)
    assert(m.id === 1)
    assert(n1.children.size === 1)
    treeDump(n1)
  }

  "findTree" should "find top" in {
    val f = findNode(m, _.kind == "world")
    assert(f.isDefined)
    assert(f.get.kind == "world")
    //treeDump(f.getOrElse(node(-1, "Empty")))
  }

  "asFileList" should "find paths" in {
    val n = setTitle(node(0, "hello"), "Hello")

    val m = setTitle(node(1, "world"), "World")
    val m1 = setTitle(node(3, "world1"), "World1")
    val n1 = addChild(addChild(n, m), m1)

    assert(asFileList(n, new File(".")) === Seq(new File(".\\hello")))
    assert(asFileList(n2, new File(".")) === Seq(new File(".\\hello\\world"), new File(".\\hello\\world1")))
  }

}