package scalasites.specs

import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.FTValList
import wcs._
import wcs.scala.util.Specification

class RunTagSpec(ics: ICS) extends Specification {

  "runTag" in {

    "create empty" in {
      val a0: FTValList = new FTValList()
      a0.setValString("NAME", "l0")
      a0.setValString("COLUMNS", "")
      ics.runTag("LISTOBJECT.CREATE", a0)
      val a1: FTValList = new FTValList()
      a1.setValString("NAME", "l0")
      a1.setValString("LISTVARNAME", "l0")
      ics.runTag("LISTOBJECT.TOLIST", a1)
      
      "col / row " in {
        //println("l0.ncol=" + ics.GetList("l0").numColumns)
        ics.GetList("l0").numColumns() must_== 1 /* not zero! */
                
        //println("l0.colname(0)='" + ics.GetList("l0").getColumnName(1)+"'")
        ics.GetList("l0").getColumnName(1) must_== null /* not zero! */
        
        //println("l0.colname(0)='" + ics.GetList("l0").getValue(null))
        
              
        //println("l0.nrow=" + ics.GetList("l0").numRows)
        ics.GetList("l0").numRows() must_== 0
      }
    }

    "create" in {
      val a0: FTValList = new FTValList()
      a0.setValString("NAME", "ol")
      a0.setValString("COLUMNS", "a,b")
      ics.runTag("LISTOBJECT.CREATE", a0)
      ics.GetList("ol") must_== null
    }

    val a1: FTValList = new FTValList()
    a1.setValString("NAME", "ol")
    a1.setValString("a", "10")
    a1.setValString("b", "20")

    ics.runTag("LISTOBJECT.ADDROW", a1);
    ics.runTag("LISTOBJECT.ADDROW", a1);
    ics.runTag("LISTOBJECT.ADDROW", a1);

    val a2: FTValList = new FTValList()
    a2.setValString("NAME", "ol")
    a2.setValString("LISTVARNAME", "l")
    ics.runTag("LISTOBJECT.TOLIST", a2)

    "get list" in {
      val l = ics.GetList("l")
      l must_!= null
    }

    "count columns" in {
      ics.GetList("l").numColumns() must_== 2
      ics.GetList("l").numRows() must_== 3
    }
  }

}