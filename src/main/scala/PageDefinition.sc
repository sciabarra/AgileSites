import wcs.build._
import scala.xml._

/////

object PageDefinition {

  //val filename = """A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\PageDefinition\1351275812156.xml"""
  val filename = """A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\main\scala\1351275812156.xml"""
                                                  //> filename  : String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\src\main\s
                                                  //| cala\1351275812156.xml

  val attr = (((XML.load(filename) \\ "attribute" filter { x => (x \ "@name").text == "Attributes" } apply 0) \
    "list") \ "row")                              //> attr  : scala.xml.NodeSeq = NodeSeq(<row>
                                                  //| <column name="assetid"><assetreference value="1351275812112" type="PageAttri
                                                  //| bute"/></column>
                                                  //| <column name="required"><string value="true"/></column>
                                                  //| <column name="ordinal"><integer value="4"/></column>
                                                  //| </row>, <row>
                                                  //| <column name="assetid"><assetreference value="1351275812112" type="PageAttri
                                                  //| bute"/></column>
                                                  //| <column name="required"><string value="true"/></column>
                                                  //| <column name="ordinal"><integer value="4"/></column>
                                                  //| </row>)

  (attr.map { row =>
    ((row \ "column") map { col =>
      "kv(\"" + (col \ "@name") + "\", " + (
        (col \ "_").head match {
          case n @ <assetreference/> => "ref(\"" + (n \ "@value") + "\")"
          case n @ <string/> => "\"" + (n \ "@value") + "\""
          case n @ <integer/> => (n \ "@value") + "L"
          case x => x.label
        }) +
        ")"
    }).mkString("\nrow(", ",", ")")
  }).mkString("list(", ",", ")")                  //> res0: String = list(
                                                  //| row(kv("assetid", ref("1351275812112")),kv("required", "true"),kv("ordinal",
                                                  //|  4L)),
                                                  //| row(kv("assetid", ref("1351275812112")),kv("required", "true"),kv("ordinal",
                                                  //|  4L)))

  // filter { x => ( x \\ "@name").text == "Attributes" }
  /*
   (attr \ "row") flatMap { row =>
    (row \ "column") map {
      col: Node => col \ "@name"
    }
  }
 */
  //.toSeq.mkString("\"", "\"," , "\"")

}