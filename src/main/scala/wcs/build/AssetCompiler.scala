package wcs.build

import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.codec.binary.Base64

import wcs.core.WCS
import scala.xml.XML
import scala.xml.Node
import java.io.File

/**
 * Compile
 */
class AssetCompiler(site: String, c: String, cid: Long, xmlIn: File, javaOut: File) extends Utils {

  import AssetCompiler._

  val doc = XML.loadFile(xmlIn)

  val name = (doc \\ "attribute").filter(x => (x \ "@name").text == "name") \\ "@value" text

  val attributeNames = doc \ "asset" \ "attribute" flatMap (_.attribute("name"))

  val assetReferences = doc \\ "assetreference" map { x =>
    "id(\"" + ((x \ "@type").text) +
      "\"," + ((x \ "@value").text + "L)")
  }

  val attributeData = (doc \ "asset" \ "attribute"). // extract attributes
    flatMap { n => Some(n.attribute("name").get.head.text -> n.child(0)) }. // List of (String,Node)  
    map { x => // convert a tuple in code
      x._2.label match {
        case "string" => stringValue(x._1, x._2)
        case "date" => dateValue(x._1, x._2)
        case "int" => intValue(x._1, x._2)
        case "array" => arrayValue(x._1, x._2)
        case "file" => fileValue(x._1, x._2)
        case "list" => listValue(x._1, x._2)
        case "assetreference" => refValue(x._1, x._2)
        case label => "\t\t/* ???? %s */".format(label)
      }
    } toList

  def javaClass = """package %nsite%.content.%nc%;
    
import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;

@AddIndex("%nsite%/contents.txt")
public class %c%%cid% extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
%attributeData%
	}
    
	public static AssetSetup setup() {
		return new %c%%cid%();
	}

	public %c%%cid%() {
		super(%cid%l, "%c%", "", "%name%");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list(%attributeNames%);
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) %references%
	}
    
}
""".replaceAll("%site%", site).
    replaceAll("%nsite%", WCS.normalizeSiteName(site)).
    replaceAll("%c%", c).
    replaceAll("%nc%", WCS.normalizeSiteName(c)).
    replaceAll("%name%", name).
    replaceAll("%c%", c).
    replaceAll("%cid%", cid.toString()).
    replaceAll("%attributeData%", attributeData.mkString("\n")).
    replaceAll("%attributeNames%", attributeNames.mkString("\"", "\",\"", "\"")).
    replaceAll("%subtype%", "subtype").
    replaceAll("%references%", assetReferences.mkString("list(", ",", ");")).
    toString();

  def compile = writeFile(javaOut, javaClass)

  override def toString = javaOut.getName
}

object AssetCompiler {

  def toHex(c: Char) = {
    val i = c.asInstanceOf[Int]
    if (i < 15) "\\x0" + Integer.toHexString(i)
    else if (i < 255) "\\x" + Integer.toHexString(c)
    else if (i > 255) "\\u%04x".format(i)
  }

  def escape(s: String) = s.toList.map {
    case '\t' => """\t"""
    case '\b' => """\b"""
    case '\n' => """\n"""
    case '\r' => """\r"""
    case '\f' => """\f"""
    case '\'' => """\'"""
    case '\"' => """\""""
    case '\\' => """\\"""
    case c if (c < ' ' || c > 255) => toHex(c)
    case c => "" + c
  }.mkString

  def escapeJava(s: String) = escape(escape(s))

  // this one does not work well
  //StringEscapeUtils.escapeJava(StringEscapeUtils.escapeJava(s))// escapeJava escapes too much

  def stringValue(m: String, n: Node): String = "\t\t" +
    """data.getAttributeData("%s").setData("%s");""".
    format(escapeJava(m), escapeJava(n.attribute("value").get.head.text))

  def dateValue(m: String, n: Node): String = "\t\t" +
    """data.getAttributeData("%s").setData(date("%s"));""".
    format(escapeJava(m), escapeJava(n.attribute("value").get.head.text))

  def intValue(m: String, n: Node): String = "\t\t" +
    """data.getAttributeData("%s").setData(Integer.valueOf(%s));""".
    format(escapeJava(m), escapeJava(n.attribute("value").get.head.text))

  def refValue(m: String, n: Node): String = "\t\t" +
    """data.getAttributeData("%s").setData(ref("%s",%sl));""".
    format(escapeJava(m),
      escapeJava(n.attribute("type").get.head.text),
      escapeJava(n.attribute("value").get.head.text))

  val isTextRe = """.*\.(txt|htm|html|css|js)$""".r

  def fileValue(m: String, n: Node) = "\t\t" +
    """data.getAttributeData("""" + m +
    """").setData(""" +
    ((n \\ "@name").text match {

      case name @ isTextRe(ext) =>
        val decoded = new String(Base64.decodeBase64(n.text.trim))
        "blob(\"" +
          escapeJava(name) +
          "\",\n\"" +
          escapeJava(decoded).split("""\\n""").mkString("\\n\"+\n\"") +
          "\"));"

      case name =>
        "blob(\"" +
          escapeJava(name) +
          "\",base64(\n" +
          (n.text.trim).grouped(72).toList.mkString("\"", "\"+\n\"", "\"") +
          ")));"

    })

  def arrayValue(m: String, n: Node): String = "\t\t" +
    "data.getAttributeData(\"" +
    escapeJava(m) +
    "\").setDataAsList(list(" + (
      n.child.tail map { // get children of <array><xxx value="..."></array> then match for each xxx
        case n @ <integer/> => (n \\ "@value").text + "L"
        case n @ <string/> => """"%s"""".format(escapeJava((n \\ "@value").text))
        case n @ <struct/> => """map(...)"""
        case n => "???"
      }).mkString(",") +
      "));"

  def listValue1(m: String, n: Node): String = "\t\t" +
    "data.getAttributeData(\"" +
    escapeJava(m) +
    "\").setDataAsList(list(" + (
      n.child.tail map { // get children of <list><row>...</row></list> 
        row =>
          "ROW"
          row.child.tail.map {
            case n @ <integer/> => (n \\ "@value").text + "L"
            case n @ <string/> => """"%s"""".format(escapeJava((n \\ "@value").text))
            case n @ <assetreference/> => """ref(...)"""
            case n => "???"
          }.mkString("\nrow(", ",", ")")
      }).mkString(",") +
      "));"

  def listValue(m: String, n: Node): String = "\t\t" +
    "data.getAttributeData(\"" +
    escapeJava(m) +
    "\").setDataAsList(" + (
      ((n \ "row").map { row =>
        ((row \ "column") map { col =>
          "\n\t\t\t\tkv(\"" + (col \ "@name") + "\", " + (
            (col \ "_").head match {
              case n @ <assetreference/> => "ref(\"" + (n \ "@type") + "\","+ (n \ "@value") + "L)"
              case n @ <string/> => "\"" + (n \ "@value") + "\""
              case n @ <integer/> => (n \ "@value") + "L"
              case x => x.label
            }) +
            ")"
        }).mkString("\n\t\t\trow(", ",", ")")
      }).mkString("list(", ",", ")")) + ");"
 
}