import wcs.build._
import java.io.File
import scala.xml._
import org.apache.commons.lang.StringEscapeUtils._

object AssetCompilerWs {

  //val acm = new AssetCompilerManager("Demo", new File("A:\\Dropbox\\Products\\sciabarra\\AgileSites\\1.1"))
  //acm.compile("AttrTypes", 1351275812122l)

  //val filename = """A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\AttrTypes\1351275812122.xml"""
  val filename = """A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\stargaze\demo\Page\1351275812113.xml"""
                                                  //> filename  : String = A:\Dropbox\Products\sciabarra\AgileSites\1.1\export\sta
                                                  //| rgaze\demo\Page\1351275812113.xml

  val doc = XML.load(filename)                    //> doc  : scala.xml.Elem = <document>
                                                  //| <asset subtype="Content1" type="Page" id="1351275812113">
                                                  //| <attribute name="Attribute_Summary"><string value="&lt;div&gt;
                                                  //| 	This is a SUMMARY.&lt;/div&gt;
                                                  //| "/></attribute>
                                                  //| <attribute name="flextemplateid"><assetreference value="1351275812156" type=
                                                  //| "PageDefinition"/></attribute>
                                                  //| <attribute name="fw_uid"><string value="4adc5be5-d187-4c2b-a299-670a3011382f
                                                  //| "/></attribute>
                                                  //| <attribute name="updateddate"><date value="2013-06-10 05:28:36.169"/></attri
                                                  //| bute>
                                                  //| <attribute name="status"><string value="ED"/></attribute>
                                                  //| <attribute name="subtype"><string value="Content"/></attribute>
                                                  //| <attribute name="updatedby"><string value="fwadmin"/></attribute>
                                                  //| <attribute name="createdby"><string value="fwadmin"/></attribute>
                                                  //| <attribute name="template"><string value="DmContentLayout"/></attribute>
                                                  //| <attribute name="createddate"><date value="2013-06-28 13:19:51.145"/></attri
                                                  //| bute>
                                                  //| <attribute name="descripti
                                                  //| Output exceeds cutoff limit.

  doc \ "asset" \ "attribute" flatMap (_.attribute("name"))
                                                  //> res0: scala.collection.immutable.Seq[Seq[scala.xml.Node]] = List(Attribute_S
                                                  //| ummary, flextemplateid, fw_uid, updateddate, status, subtype, updatedby, cre
                                                  //| atedby, template, createddate, description, name, Publist)

  val attributes /*: List[Tuple2[String, Node]]*/ = (doc \ "asset" \ "attribute").
    flatMap { n => Some(n.attribute("name").get.head.text -> n.child(0)) }.
    map { x =>
      x._2.label match {
        case "string" => (x._1, x._2)
        case "date" => (x._1, x._2)
        case "int" => (x._1, x._2)
        case "array" => (x._1, x._2)
        case "file" => (x._1, x._2)
        case _ => "/*????*/"
      }
    } toList                                      //> attributes  : List[java.io.Serializable] = List((Attribute_Summary,<string 
                                                  //| value="&lt;div&gt;
                                                  //| 	This is a SUMMARY.&lt;/div&gt;
                                                  //| "/>), /*????*/, (fw_uid,<string value="4adc5be5-d187-4c2b-a299-670a3011382f
                                                  //| "/>), (updateddate,<date value="2013-06-10 05:28:36.169"/>), (status,<strin
                                                  //| g value="ED"/>), (subtype,<string value="Content"/>), (updatedby,<string va
                                                  //| lue="fwadmin"/>), (createdby,<string value="fwadmin"/>), (template,<string 
                                                  //| value="DmContentLayout"/>), (createddate,<date value="2013-06-28 13:19:51.1
                                                  //| 45"/>), (description,<string value="Home Page1"/>), (name,<string value="Ho
                                                  //| me1"/>), (Publist,<array>
                                                  //| <integer value="1351275793143"/></array>))

  val node = (( doc \ "asset" \ "attribute" apply(12) ) \ "array")
                                                  //> node  : scala.xml.NodeSeq = NodeSeq(<array>
                                                  //| <integer value="1351275793143"/></array>)
  
  
  node.iterator                                   //> res1: Iterator[scala.xml.Node] = non-empty iterator
                        
 // node \\ "value"
                                                  
  val name = (doc \\ "attribute").filter( x => (x \ "@name").text == "name") \\ "@value" text
                                                  //> name  : String = Home1

  
}