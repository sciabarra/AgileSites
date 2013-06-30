package wcs.build

import org.scalatest._
import scala.collection.immutable.ListMap
import scala.collection.immutable.ListSet

class ImportXmlTest extends FunSuite {

  import ImportXml._

  val im = ImportXml("http://localhost:8080/cs", "Demo", "fwadmin", "xceladmin")

  ignore("process") {
    var n = 0

    val l = im.process { x =>
      n = n + 1

      val c = x \\ "document" \ "asset" \ "@type" toString
      val cid = x \\ "document" \ "asset" \ "@id" toString

      val s = "%s:%s\n".format(c, cid)

      //println(s) 

      s
    }

    assert(l.size > 0)
    assert(l.size === n)
  }

  ignore("load") {
    val x = im.load("Page", 1351275807810L)

    val (c, cid) = ImportXml.aid(x)

    val d = ImportXml.deps(x)

    assert(c === "Page")
    assert(cid === 1351275807810L)
    assert(d.size === 4)
    val (c0, cid0) = d.last
    assert(c0 === "PageDefinition")
    assert(cid0 === 1351275807856L)

  }

  ignore("ordered by deps - imperative") {
    val alldeps = im.depsMap

    var collect = ListSet[ImportXml.Aid]()

    for (k <- alldeps.keys) {
      val curdeps = alldeps(k)
      val curdepsCollected = curdeps.foldLeft(true) { (x, y) => x && collect.contains(y) }

      if (curdepsCollected)
        collect += k
    }
  }

  ignore("ordered by deps - functional") {

    val (collected, uncollected) = im.orderedDeps
    println("=== collected ===" + collected)
    println("=== uncollected ===" + uncollected)

  }
  
  ignore("post all") {
    val (collected, uncollected) = im.orderedDeps
    im.importAssets(collected)
  }
  
  ignore("post one") {
    val (collected, uncollected) = im.orderedDeps
    val aid = collected(0)
    println(aid)
    im.importAsset(aid)
  }
  
  ignore("post Page") {
    im.importAsset("PageAttribute",1351275810556L)
    //im.importAsset("Template",1351275807801L)
  }
  
  ignore("import all") {

    val (collected, uncollected) = im.orderedDeps
    im.importAssets(collected)
    
  }
  
}