package wcs.build

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.xml.Elem
import scala.xml.Node

@RunWith(classOf[JUnitRunner])
class AssetCompilerTest extends FunSuite {

  def unary_~(x: String) = println(x)

  implicit def any2str(any: Any) = any.toString

  test("Hello") {
    val l = <list>
              <row>
                <column name="assetid"><assetreference type="PageAttribute" value="1351275812112"/></column>
                <column name="required"><string value="true"/></column>
                <column name="ordinal"><integer value="4"/></column>
              </row>
              <row>
                <column name="assetid"><assetreference type="PageAttribute" value="1351275812112"/></column>
                <column name="required"><string value="true"/></column>
                <column name="ordinal"><integer value="4"/></column>
              </row>
            </list>

    val r = (l \ "row") flatMap { row =>
      (row \ "column") map {
    	  col: Node => col \ "@name"
      } 
      //.toSeq.mkString("\"", "\"," , "\"")
    }

    println(r)

  }

}