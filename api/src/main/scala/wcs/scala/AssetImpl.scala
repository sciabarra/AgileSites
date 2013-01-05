package wcs.scala
import java.util.Date
import com.fatwire.assetapi.data.MutableAssetData

class AssetImpl(val a: wcs.java.Asset) extends wcs.java.Asset {

  def c = a.getType()
  def cid = a.getId()
  def name = a.getName()
  def description = a.getDescription()
  def filename = a.getFilename()
  def path = a.getPath()

  def apply(list: String): String = a.getString(list)
  def apply(list: String, pos: Int): String = a.getString(list, pos)

  def asInt(list: String): Int = a.getInt(list)
  def asInt(list: String, pos: Int): Int = a.getInt(list, pos)

  def asId(list: String): Long = a.getId(list)
  def asId(list: String, pos: Int): Long = a.getId(list, pos)

  def asDate(list: String): Date = a.getDate(list)
  def asDate(list: String, pos: Int): Date = a.getDate(list, pos)

  def range(list: String) = 1 to a.getSize(list)

 
  def getAttributes(): java.util.List[String] = {
    throw new RuntimeException(
      "should not be called here - reserved for setup");
  }

  def setData(data: MutableAssetData) {
    throw new RuntimeException(
      "should not be called here - reserved for setup");
  }
}