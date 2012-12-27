package test.cselement

import wcs.scala.Element
import wcs.scala.Env
import wcs.scala.Presentation
import wcs.scala.util.Common
import COM.FutureTense.Interfaces.ICS
import COM.FutureTense.Interfaces.FTValList

class TestAreaEditor extends Element with Common {

  def userIsMemberOf(group: String)(implicit ics: ICS) = {
    val args = new FTValList()
    args.setValString("GROUP", group)
    ics.runTag("USERISMEMBER", args)
    ics.GetErrno() == 0
  }

  def apply(e: Env): String = {

    implicit val env = e;

    val inst = e("PresInst")
    val wrapStyle = Presentation(inst, "WRAPSTYLE").getOrElse("OFF")
    val fontSize = Presentation(inst, "FONTSIZE").getOrElse("2")
    val xsize = Presentation(inst, "XSIZE").getOrElse("24")
    val ysize = Presentation(inst, "YSIZE").getOrElse("20")

    val disableTextArea = Presentation(inst, "PERMISSION") match {
      case Some(group) => if (userIsMemberOf(group)) null else "disabled"
      case None => null
    }

    if (e("MultiValueEntry") equalsIgnoreCase "no") {

      e("doDefaultDisplay") = "no"

      <tr>
        <td>
          <b>Attribute</b>
          <br/>
          AttrType={ e("AttrType") }
          <br/>
          AttrName={ e("AttrName") }
          <br/>
          EditingStyle={ e("EditingStyle") }
        </td>
        <td></td>
        <td>
          {
            if (e("EditingStyle") == "single") {
              if (e("AttrType") == "url") {
                <input type="hidden" name={ e("AttrName") + "_file" } value={
                  e.get('AttrValueList, "urlvalue").getOrElse(System.currentTimeMillis() + ".txt")
                }/>
              }

              <textarea cols={ xsize } rows={ ysize } name={ e("AttrName") } wrap={ wrapStyle } disabled={ disableTextArea }>
                {
                  e.get('AttrValueList, "@urlvalue").getOrElse("")
                }
              </textarea>
            }
          }
        </td>
      </tr>

    } else {

      /*

      if (e("RequiredAttr") equalsIgnoreCase "true") {
        e("RequireInfo") = e("RequireInfo") + "*" + e.getCounter("TCounter") + e("AttrName") + "*ReqTrue*" + e("AttrType") + "!"
      } else {
        e("RequireInfo") = e("RequireInfo") + "*" + e.getCounter("TCounter") + e("AttrName") + "*ReqFalse*" + e("AttrType") + "!"
      }

      <TEXTAREA COLS={ xsize } ROWS={ ysize } NAME={ e("AttrName") } WRAP={ wrapStyle }>
        {
          if (e.exist('AttrValueList, "@urlvalue")) {
            e.exist('AttrValueList, "@urlvalue")
          } else {
            ""
          }
        }
      </TEXTAREA>
*/
      ""
    }
  }
}