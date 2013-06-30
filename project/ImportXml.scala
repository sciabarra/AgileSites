package wcs.build

import java.io._
import scala.xml._
import scala.Array.fallbackCanBuildFrom
import scala.collection.immutable.ListSet
import java.net._

class ImportXml(url: String, sites: String, user: String, pass: String) {

  import ImportXml.Aid
  import ImportXml.AidDepMap

  val repo = new File(new File(new File("export"), "stargaze"), sites)

  // load a specific xml 
  def load(c: String, cid: Long) = {
    //val xml = repo / c / (cid + ".xml")
    val xml = new File(new File(repo, c), cid + ".xml")
    XML.loadFile(xml)
  }

  val ignoredTypes = Set("SiteEntry", "Template", "CSElement")

  // process all the nodes
  def process[A](action: (NodeSeq => A)) = {
    for {
      dir <- repo.listFiles if dir.isDirectory && !ignoredTypes.contains(dir.getName)
      file <- dir.listFiles if file.getName.endsWith(".xml")
    } yield {
      action(XML.loadFile(file))
    }
  }

  // create a map of dependencies
  def depsMap = process { x => ImportXml.aid(x) -> ImportXml.deps(x) } toMap

  // return a list of dependencies in order and a list of cyclic dependencies
  def orderedDeps: Tuple2[List[Aid], List[Aid]] = {
    
    val alldeps = depsMap

    def go(deps: List[Aid], collected: ListSet[Aid], uncollected: AidDepMap): Tuple2[ListSet[Aid], AidDepMap] =
      deps match {
        case Nil => (collected, uncollected)
        case head :: tail =>
          val curdeps = alldeps(head)
          val curdepsCollected = curdeps.foldLeft(true) { (x, y) => x && collected.contains(y) }
          if (curdepsCollected) {
            go(tail, collected + head, uncollected)
          } else {
            go(tail, collected, uncollected + (head -> curdeps))
          }
      }

    def loop(collected: ListSet[Aid], uncollected: AidDepMap): Tuple2[List[Aid], List[Aid]] =
      {
        val (c, u) = go(uncollected.keys.toList, collected, Map())
        if (c.size < collected.size)
          loop(c, u)
        else
          (c.toList.reverse, u.keys.toList)
      }

    loop(ListSet(), alldeps )
  }

  def importAsset(aid: Aid) = {

    val subtype = load(aid._1, aid._2) \\ "document" \ "asset" \ "@subtype"

    val req = "%s/ContentServer?pagename=AAAgileImport&user=%s&pass=%s&site=%s&c=%s&cid=%s&subtype=%s"
      .format(url, user, pass, sites, aid._1, aid._2.toString, if (subtype == null) "" else subtype.text)

    println(req)

    val scan = new java.util.Scanner(new URL(req).openStream(), "UTF-8")
    val res = scan.useDelimiter("\\A").next()
    scan.close

    //println(res)

  }

  def importAssets(assets: List[Aid]) = {

    for { a <- assets } yield {
      println("*** %s ***".format(a))
      importAsset(a)
    }

    //for (a <- assets)
    //  println(a)
  }
}

object ImportXml {

  type Aid = Tuple2[String, Long]

  type Asid = Tuple3[String, Long, String]

  type AidDepMap = Map[Aid, Seq[Aid]]

  // factory method
  def apply(url: String, site: String, user: String, pass: String) = new ImportXml(url, site, user, pass)

  // get dependencies of a node
  def deps(x: NodeSeq) = {
    (x \\ "assetreference") map { y =>

      val c = y \\ "@type" text
      val cid = (y \\ "@value" text) toLong

      (c, cid)
    }
  }

  // get the asset id of a node
  def aid(x: NodeSeq): Aid = {

    val c = (x \\ "document" \ "asset" \ "@type" toString)
    val cid = (x \\ "document" \ "asset" \ "@id" toString) toLong

    (c, cid)
  }

  def asid(x: NodeSeq): Asid = {

    val c = (x \\ "document" \ "asset" \ "@type" toString)
    val cid = (x \\ "document" \ "asset" \ "@id" toString) toLong
    val subtype = (x \\ "document" \ "asset" \ "@subtype" toString)

    (c, cid, subtype)
  }

}