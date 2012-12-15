package test.cselement

import wcs.scala.Element
import wcs.scala.Env
import wcs.scala.Presentation
import wcs.scala.util.Common
import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.FTValList

class TestAreaEditor extends Element with Common {

  def userIsMemberOf(group: String)(implicit ics: ICS) {
    val args = new FTValList()
    args.setValString("GROUP", group)
    ics.runTag("USERISMEMBER", args)
    ics.GetErrno() == 0
  }

  def apply(e: Env): String = {
    implicit val env = e;
    val inst = e("PresInst")

    if (e("MultiValueEntry") equalsIgnoreCase "no") {

      val doDefaultDisplay = false
      val wrapStyle = Presentation(inst, "WRAPSTYLE").getOrElse("OFF")
      val fontSize = Presentation(inst, "FONTSIZE").getOrElse("2")
      val xsize = Presentation(inst, "XSIZE").getOrElse("24")
      val ysize = Presentation(inst, "YSIZE").getOrElse("20")

      val disableTextArea = Presentation(inst, "PERMISSION") match {
        case Some(group) => userIsMemberOf(group)
        case None => false
      }
      <tr>{
        call("OpenMarket/Gator/FlexibleAssets/Common/DisplayAttributeName")
      }
      <td></td>
      <td>{
        if(e("EditingStyle")=="single") {
          if(e("AttrType")=="url") {
            
          }
        }
      }</td>

      </tr>
    } else {
      ""
    }
  }
}