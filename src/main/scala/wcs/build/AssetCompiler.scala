package wcs.build

import org.apache.commons.lang.StringEscapeUtils
import org.apache.commons.codec.binary.Base64

import scala.xml.XML
import scala.xml.Node
import java.io.File
import sun.misc.BASE64Decoder

/**
 * Compile
 */
class AssetCompiler(site: String, c: String, cid: Long, xmlIn: File, javaOut: File, basePackage: String) extends Utils {

  import AssetCompiler._

  val doc = scala.xml.Utility.trim(XML.loadFile(xmlIn))

  val name = (doc \\ "attribute").filter(x => (x \ "@name").text == "name") \\ "@value" text

  val subtype = doc \\ "asset" \\ "@subtype" text

  val attributeNames = (doc \ "asset" \ "attribute").filter(x => !((x \ "@name").text startsWith "Group_")) flatMap { n =>
    val attr0 = n.attribute("name").get.head.text

    val attr1 = if (attr0.startsWith("Attribute_"))
      attr0.substring("Attribute_".length())
    else attr0
    Some(attr1)
  }

  val assetReferences = doc \\ "assetreference" map { x =>
    "id(\"" + ((x \ "@type").text) +
      "\"," + ((x \ "@value").text + "L)")
  }

  // removed self reference from parents until we implement the two-phase import
  val assetParents = ((doc \\ "attribute").filter(x => (x \ "@name").text startsWith  "Group_") \\ "assetreference").filter(x => (x \ "@value").text != ""+cid) map { x =>
    "ref(\"" + ((x \ "@type").text) +
      "\"," + ((x \ "@value").text + "L)")
  }

  val attributeData = (doc \ "asset" \ "attribute").filter(x => !((x \ "@name").text startsWith "Group_")).
    flatMap { n =>
    val attr0 = n.attribute("name").get.head.text

    val attr1 = if (attr0.startsWith("Attribute_"))
      attr0.substring("Attribute_".length())
    else attr0

    if (attr1 != "Publist") Some(attr1 -> n.child(0))
    else None
  }. // List of (String,Node)
    map { x => // convert a tuple in code
    x._2.label match {
      case "array" => arrayValue(x._1, x._2)
      case "list" => listValue(x._1, x._2)
      case "assetreference" => refValue(x._1, x._2)
      case "string" => stringValue(x._1, x._2)
      case "date" => dateValue(x._1, x._2)
      case "int" => intValue(x._1, x._2)
      case "integer" => intValue(x._1, x._2)
      case "file" => fileValue(x._1, x._2)
      case label => "\t\t/* ???? %s %s */".format(x._1, label)
    }
  } toList

  def binaryData =  (doc \ "asset" \ "attribute" \ "file").map { file =>
    ((file \\ "@name").text, new BASE64Decoder().decodeBuffer(file.text))
  }

  def javaClass = """package %nsite%.%package%.%nc%;

import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import wcs.java.util.AssetReferences;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("%nsite%/contents.txt")
public class %c%%cid% extends %c%Setup {

    @Override
    public void setAssetData(MutableAssetData data) {
        this.assetData = data;
        data.getAttributeData("Publist").setDataAsList(list(getSite()));
        %attributeData%
    }

    public static AssetSetup setup() {
        return new %c%%cid%();
    }

    public %c%%cid%() {
        super(%cid%l, "%c%", "%subtype%", "%name%");
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAssetAttributes() {
        return (List<String>)list(%attributeNames%);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Id> getAssetReferences() {
        return (List<Id>) %references%
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AssetId> getAssetParents() {
        return (List<AssetId>) %parents%
    }

}
                  """.replaceAll("%site%", site).
    replaceAll("%nsite%", normalizeSiteName(site)).
    replaceAll("%c%", c).
    replaceAll("%nc%", normalizeSiteName(c)).
    replaceAll("%package%", basePackage).
    replaceAll("%name%", name).
    replaceAll("%c%", c).
    replaceAll("%cid%", cid.toString()).
    replaceAll("%attributeData%", attributeData.mkString("\n")).
    replaceAll("%attributeNames%", attributeNames.mkString("\"", "\",\"", "\"")).
    replaceAll("%subtype%", subtype).
    replaceAll("%references%", assetReferences.mkString("list(", ",", ");")).
    replaceAll("%parents%", assetParents.mkString("list(", ",", ");")).
    toString();

  def baseJavaClass = """package %nsite%.%package%.%nc%;

import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.data.AssetId;

public abstract class %c%Setup extends AssetSetup {

    protected MutableAssetData assetData;

    @Override
    public void setData(MutableAssetData data) {
        setAssetData(data);
    }

    public abstract void setAssetData(MutableAssetData data);

    public %c%Setup(long id, String type, String subtype, String name) {
        super(id, type, subtype, name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAttributes() {
        return getAssetAttributes();
    }

    public abstract List<String> getAssetAttributes();

    @Override
    @SuppressWarnings("unchecked")
    public List<Id> getReferences() {
        return getAssetReferences();
    }

    public abstract List<Id> getAssetReferences();

    @Override
    @SuppressWarnings("unchecked")
    public List<AssetId> getParents() {
        return getAssetParents();
    }

    public abstract List<AssetId> getAssetParents();
}
                      """.replaceAll("%site%", site).
    replaceAll("%nsite%", normalizeSiteName(site)).
    replaceAll("%package%", basePackage).
    replaceAll("%c%", c).
    replaceAll("%nc%", normalizeSiteName(c)).
    toString();

  def compile =  {
    val baseClassFile = new File(javaOut.getParent(), c+"Setup.java")
    if (!baseClassFile.exists()) {
      writeFile(baseClassFile, baseJavaClass)
    }
    writeFile(javaOut, javaClass)
    for(data <- binaryData){
      val binFileName = new File(normalizeFilename(data._1)).getPath
      writeFile(xmlIn.getParentFile.getParentFile.getParentFile + "/bin", binFileName, data._2)

    }
  }


  override def toString = javaOut.getName
}

object AssetCompiler {

  def normalizeFilename (filename: String) = {
    filename.replace("\\","/").split("/").takeRight(3).mkString("","/","")
  }

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
        val decoded = new String(Base64.decodeBase64(n.text.trim.getBytes))
        "blob(\"" +
          escapeJava(name) +
          "\",\n\"" +
          escapeJava(decoded).split("""\\n""").mkString("\\n\"+\n\"") +
          "\"));"

      case name =>
        "file(\"" + escapeJava(normalizeFilename(name)) + "\"));"
    })

  def arrayValue(m: String, n: Node): String = "\t\t" +
    "data.getAttributeData(\"" +
    escapeJava(m) +
    "\").setDataAsList(list(" + (
    n.child map { // get children of <array><xxx value="..."></array> then match for each xxx
      case n @ <integer/> => (n \\ "@value").text + "L"
      case n @ <string/> => """"%s"""".format((n \\ "@value").text)
      // assetapi doesn't support ordered lists for attributes
      //case <struct>{n @ _*}</struct> => structToMap(n)
      case <struct>{n @ _*}</struct> => "ref(\"" + (n \ "assetreference" \ "@type") + "\"," + (n \ "assetreference" \ "@value") + "L)"
      case n @ <assetreference/> => "ref(\"" + (n \ "@type") + "\"," + (n \ "@value") + "L)"
      case x => "???"
    }).mkString(",") +
    "));"

  def structToMap (n: Seq[Node]): String = (
    n  map {
      col =>
        "\n\t\t\t\tkv(\"" + (col \ "@name") + "\", " + (
          (col \ "_").head match {
            case n @ <assetreference/> => "AttributeTypeEnum.ASSET, ref(\"" + (n \ "@type") + "\"," + (n \ "@value") + "L)"
            case n @ <decimal/> => "AttributeTypeEnum.FLOAT, " + (n \ "@value")
            case n @ <integer/> => "AttributeTypeEnum.INT, " + (n \ "@value")
            case x => x.label
          }) +
          ")"
    }).mkString("\n\t\t\tmap(", ",", ")")

  def listValue(m: String, n: Node): String = "\t\t" +
    "data.getAttributeData(\"" +
    escapeJava(m) +
    "\").setDataAsList(" + (
    ((n \ "row").map { row =>
      ((row \ "column") map { col =>
        "\n\t\t\t\tkv(\"" + (col \ "@name") + "\", " + (
          (col \ "_").head match {
            case n @ <assetreference/> => "AttributeTypeEnum.ASSET, ref(\"" + (n \ "@type") + "\"," + (n \ "@value") + "L)"
            case n @ <string/> => "AttributeTypeEnum.STRING, \"" + (n \ "@value") + "\""
            case n @ <integer/> => "AttributeTypeEnum.INT, " + (n \ "@value") + "L"
            case x => x.label
          }) +
          ")"
      }).mkString("\n\t\t\tmap(", ",", ")")
    }).mkString("list(", ",", ")")) + ");"

}