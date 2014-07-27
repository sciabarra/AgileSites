package agilesites.build.scrivener
import java.io.File

object Playground extends TreeBuilder {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  val n = setTitle(node(0, "hello"), "Hello")     //> n  : agilesites.build.scrivener.Playground.Tree = Tree(0,hello,List(),hello,
                                                  //| Hello)
  val m = setTitle(node(1, "world"), "World")     //> m  : agilesites.build.scrivener.Playground.Tree = Tree(1,world,List(),world,
                                                  //| World)
  val m1 = setTitle(node(3, "world1"), "World1")  //> m1  : agilesites.build.scrivener.Playground.Tree = Tree(3,world1,List(),worl
                                                  //| d1,World1)

  val n1 = addChild(addChild(n, m), m1)           //> n1  : agilesites.build.scrivener.Playground.Tree = Tree(0,hello,List(Tree(1,
                                                  //| world,List(),world,World), Tree(3,world1,List(),world1,World1)),hello,Hello)
                                                  //| 
                          
	
	asFileList(n, new File("."))              //> res0: Seq[java.io.File] = List(.\hello)
	asFileList(n1, new File("."))             //> res1: Seq[java.io.File] = List(.\hello\world, .\hello\world1)
}