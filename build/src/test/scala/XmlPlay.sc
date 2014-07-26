
import scala.xml.XML

object XmlSamples {
  
	val x = <a><b id="1"><c id="2">d</c></b></a>
                                                  //> x  : scala.xml.Elem = <a><b id="1"><c id="2">d</c></b></a>
  
  x \\ "_"  filter { _.attributes.get("id").getOrElse("").toString == "2"}
                                                  //> res0: scala.xml.NodeSeq = NodeSeq(<c id="2">d</c>)
  
 
}