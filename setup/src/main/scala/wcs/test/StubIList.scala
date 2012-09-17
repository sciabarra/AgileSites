package wcs.test

import COM.FutureTense.Interfaces.IList
import org.eintr.loglady.Logging

class StubIList(val name: String, val listMap: Map[String, List[String]]) extends IList with Logging {

  log.trace("listMap=%s", listMap)

  private var curRow = 0
  private val columns = listMap.keys.toArray
  private val (_numRows, _hasData, _numColumns) =
    if (listMap.isEmpty) (0, false, 0)
    else
      (listMap.values.map { _.size }.min, true, columns.size)

  log.trace("numRows=%s", _numRows)

  def getName(): String = { name }

  def hasData(): Boolean = { _hasData }

  def numColumns(): Int = { _numColumns }

  // note currentRow starts from 1
  def currentRow(): Int = { curRow + 1 }

  def getValue(arg0: String): String = {
    val v = listMap.get(arg0).getOrElse(null)
    if (v == null)
      null
    else
      v(curRow)
    //listMap(arg0)(curRow) 
  }

  def getObject(arg0: String): Object = { throw new Exception("getObject not implemented") }

  def getFileData(arg0: String): Array[Byte] = { throw new Exception("getFileData not implemented") }

  def getFileString(arg0: String): String = { throw new Exception("getFileString not implemented") }

  def flush(): Unit = {}

  def getColumnName(arg0: Int): String = {
    if (arg0 > 0 && arg0 < _numColumns)
      columns(arg0)
    else
      null
  }

  def numRows(): Int = { _numRows }

  // note  rows are numbered from 1 to lastRow
  def moveTo(row: Int): Boolean = {
    if (row <= _numRows) {
      curRow = row - 1
      true
    } else {
      false
    }
  }

  def atEnd(): Boolean = { curRow == _numRows }

  def moveToRow(arg0: Int, arg1: Int): Boolean = { throw new Exception("moveToRow not implemented") }

  def numIndirectColumns(): Int = { 0 }

  def getIndirectColumnName(arg0: Int): String = { null }

  def clone(arg0: String): IList = { throw new Exception("clone not implemented") }

  def rename(arg0: String): Unit = { throw new Exception("rename not implemented") }

  def stringInList(arg0: String): Boolean = { throw new Exception("stringInList not implemented") }

  override def toString = "StubIList(%s : %s".format(name, listMap.toString)

}