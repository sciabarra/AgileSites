package test.cselement

import wcs.scala.Element
import wcs.scala.Env

class TestAreaEditorOld extends Element {

  def apply(e: Env): String = {

    /*   
        <FTCS Version="1.1">
      <!-- OpenMarket/Gator/AttributeTypes/IMAGEPICKER -->
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ASSETTYPENAME" VARNAME="ASSETTYPENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ATTRIBUTETYPENAME" VARNAME="ATTRIBUTETYPENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ATTRIBUTENAME" VARNAME="ATTRIBUTENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="CATEGORYATTRIBUTENAME" VARNAME="CATEGORYATTRIBUTENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="RESTRICTEDCATEGORYLIST" VARNAME="RESTRICTEDCATEGORYLISTOUT"/><!-- added by Greg -->
      <IF COND="IsVariable.CATEGORYATTRIBUTENAMEOUT=false">
        <THEN>
          <SETVAR NAME="CATEGORYATTRIBUTENAMEOUT" VALUE="CS.Empty"/>
        </THEN>
      </IF>
      <!-- added by Greg -->
      <IF COND="IsVariable.RESTRICTEDCATEGORYLISTOUT=false">
        <THEN>
          <SETVAR NAME="RESTRICTEDCATEGORYLISTOUT" VALUE="CS.Empty"/>
        </THEN>
      </IF>
      <IF COND="Variables.MultiValueEntry=no">
        <THEN>
 */
    <tr>
      <td>MultiValueEntry={ e("MultiValueEntry") }</td>
      { /*
      <callelement NAME="OpenMarket/Gator/FlexibleAssets/Common/DisplayAttributeName"/>
      <if COND="Variables.EditingStyle=single">
        <then>
          <SETVAR NAME="my_attr_name" VALUE="Variables.cs_SingleInputName"/>
          <if COND="Variables.RequiredAttr=true">
            <then>
              <setvar NAME="RequireInfo" VALUE="*Variables.cs_CurrentInputName*Variables.currentAttrName*ReqTrue*Variables.AttrType!"/>
            </then>
            <else>
              <setvar NAME="RequireInfo" VALUE="*Variables.cs_CurrentInputName*Variables.currentAttrName*ReqFalse*Variables.AttrType!"/>
            </else>
          </if>
          <IF COND="AttrValueList.#numRows=0">
            <THEN>
              <SETVAR NAME="thevalue" VALUE="Variables.empty"/>
            </THEN>
            <ELSE>
              <SETVAR NAME="thevalue" VALUE="AttrValueList.value"/>
            </ELSE>
          </IF>
        </then>
        <else>
          <SETVAR NAME="my_attr_name" VALUE="Variables.cs_MultipleInputName"/>
          <if COND="Variables.RequiredAttr=true">
            <then>
              <setvar NAME="RequireInfo" VALUE="*Variables.cs_MultipleInputName*Variables.currentAttrName*ReqTrue*Variables.AttrType!"/>
            </then>
            <else>
              <setvar NAME="RequireInfo" VALUE="*Variables.cs_MultipleInputName*Variables.currentAttrName*ReqFalse*Variables.AttrType!"/>
            </else>
          </if>
          <IF COND="Variables.tempval=Variables.empty">
            <THEN>
              <SETVAR NAME="thevalue" VALUE="Variables.empty"/>
            </THEN>
            <ELSE>
              <SETVAR NAME="thevalue" VALUE="Variables.tempval"/>
            </ELSE>
          </IF>
        </else>
      </if>
      */ }
      <td></td>
      <td>
        Qualcosa qui
        { /*
        <setvar NAME="doDefaultDisplay" VALUE="no"/>
        <!-- Actual Field -->
        <IF COND="Variables.thevalue=Variables.empty">
          <THEN>
            <IMG NAME="Variables.my_attr_name" SRC="Variables.cs_imagedir/graphics/SessionVariables.locale/logo/NoImage.gif" BORDER="0" REPLACEALL="Variables.cs_imagedir,Variables.my_attr_name,SessionVariables.locale"/>
          </THEN>
          <ELSE>
            <ASSETSET.SETASSET NAME="theImage" TYPE="Variables.ASSETTYPENAMEOUT" ID="Variables.thevalue"/>
            <ASSETSET.GETATTRIBUTEVALUES NAME="theImage" ATTRIBUTE="Variables.ATTRIBUTENAMEOUT" LISTVARNAME="imageList" TYPENAME="Variables.ATTRIBUTETYPENAMEOUT"/>
            <satellite.blob service="img src" width="100" border="0" name="Variables.my_attr_name" csblobid="SessionVariables.csblobid" blobtable="MungoBlobs" blobkey="id" blobwhere="imageList.value" blobcol="urldata"/>
          </ELSE>
        </IF>
        <INPUT TYPE="HIDDEN" NAME="Variables.my_attr_name" VALUE="Variables.thevalue" REPLACEALL="Variables.my_attr_name,Variables.thevalue"/>
        <![CDATA[
        <SCRIPT>
            var windowProperties = 'scrollbars=yes,resizable=yes,width=950,height=650,top=0,left=0';
        </SCRIPT>
        ]]>
        <a HREF="" onclick="javascript:window.open('ContentServer?pagename=OpenMarket/Gator/AttributeTypes/IMAGEPICKERShowImages&#38;fieldname=Variables.my_attr_name&#38;id=Variables.thevalue&#38;ATTRIBUTETYPENAMEOUT=Variables.ATTRIBUTETYPENAMEOUT&#38;ATTRIBUTENAMEOUT=Variables.ATTRIBUTENAMEOUT&#38;ASSETTYPENAMEOUT=Variables.ASSETTYPENAMEOUT&#38;CATEGORYATTRIBUTENAMEOUT=Variables.CATEGORYATTRIBUTENAMEOUT&#38;RESTRICTEDCATEGORYLISTOUT=Variables.RESTRICTEDCATEGORYLISTOUT','templateSelect', windowProperties);return false;" REPLACEALL="Variables.ATTRIBUTETYPENAMEOUT,Variables.ATTRIBUTENAMEOUT,Variables.ASSETTYPENAMEOUT,Variables.CATEGORYATTRIBUTENAMEOUT,Variables.RESTRICTEDCATEGORYLISTOUT,Variables.my_attr_name,Variables.thevalue,Variables.AttrName">
          <IMG HSPACE="10" ALIGN="ABSMIDDLE" src="Variables.cs_imagedir/graphics/SessionVariables.locale/button/content/images/popup.gif" BORDER="0" REPLACEALL="Variables.cs_imagedir,SessionVariables.locale"/>
        </a>
        <XLAT.LOOKUP KEY="dvin/Util/FlexAssets/RemoveSelect" VARNAME="deleteALT" ENCODE="false"/>
        <a href="javascript:void(0)" onclick="javascript:document.forms[0].Variables.my_attr_name.value='';document.images.Variables.my_attr_name.src= 'Variables.cs_imagedir/graphics/SessionVariables.locale/logo/NoImage.gif'; return false;" onmouseover="window.status='Variables.deleteALT'; return true;" onmouseout="window.status=' ';return true;" REPLACEALL="Variables.cs_imagedir,Variables.my_attr_name,Variables.deleteALT,SessionVariables.locale"> <img src="Variables.cs_imagedir/graphics/common/icon/iconDeleteContent.gif" ALIGN="absmiddle" HSPACE="4" border="0" alt="Variables.deleteALT" title="Variables.deleteALT" REPLACEALL="Variables.cs_imagedir,Variables.deleteALT"/></a>
        */ }
      </td>
    </tr>
    /*
    </THEN>
      </IF>
    </FTCS>
    */
  }

  /*  
    <FTCS Version="1.1">
      <!-- OpenMarket/Gator/AttributeTypes/IMAGEPICKER -->
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ASSETTYPENAME" VARNAME="ASSETTYPENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ATTRIBUTETYPENAME" VARNAME="ATTRIBUTETYPENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="ATTRIBUTENAME" VARNAME="ATTRIBUTENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="CATEGORYATTRIBUTENAME" VARNAME="CATEGORYATTRIBUTENAMEOUT"/>
      <presentation.getprimaryattributevalue NAME="Variables.PresInst" ATTRIBUTE="RESTRICTEDCATEGORYLIST" VARNAME="RESTRICTEDCATEGORYLISTOUT"/><!-- added by Greg -->
      <IF COND="IsVariable.CATEGORYATTRIBUTENAMEOUT=false">
        <THEN>
          <SETVAR NAME="CATEGORYATTRIBUTENAMEOUT" VALUE="CS.Empty"/>
        </THEN>
      </IF>
      <!-- added by Greg -->
      <IF COND="IsVariable.RESTRICTEDCATEGORYLISTOUT=false">
        <THEN>
          <SETVAR NAME="RESTRICTEDCATEGORYLISTOUT" VALUE="CS.Empty"/>
        </THEN>
      </IF>
      <IF COND="Variables.MultiValueEntry=no">
        <THEN>
          <tr>
            <callelement NAME="OpenMarket/Gator/FlexibleAssets/Common/DisplayAttributeName"/>
            <if COND="Variables.EditingStyle=single">
              <then>
                <SETVAR NAME="my_attr_name" VALUE="Variables.cs_SingleInputName"/>
                <if COND="Variables.RequiredAttr=true">
                  <then>
                    <setvar NAME="RequireInfo" VALUE="*Variables.cs_CurrentInputName*Variables.currentAttrName*ReqTrue*Variables.AttrType!"/>
                  </then>
                  <else>
                    <setvar NAME="RequireInfo" VALUE="*Variables.cs_CurrentInputName*Variables.currentAttrName*ReqFalse*Variables.AttrType!"/>
                  </else>
                </if>
                <IF COND="AttrValueList.#numRows=0">
                  <THEN>
                    <SETVAR NAME="thevalue" VALUE="Variables.empty"/>
                  </THEN>
                  <ELSE>
                    <SETVAR NAME="thevalue" VALUE="AttrValueList.value"/>
                  </ELSE>
                </IF>
              </then>
              <else>
                <SETVAR NAME="my_attr_name" VALUE="Variables.cs_MultipleInputName"/>
                <if COND="Variables.RequiredAttr=true">
                  <then>
                    <setvar NAME="RequireInfo" VALUE="*Variables.cs_MultipleInputName*Variables.currentAttrName*ReqTrue*Variables.AttrType!"/>
                  </then>
                  <else>
                    <setvar NAME="RequireInfo" VALUE="*Variables.cs_MultipleInputName*Variables.currentAttrName*ReqFalse*Variables.AttrType!"/>
                  </else>
                </if>
                <IF COND="Variables.tempval=Variables.empty">
                  <THEN>
                    <SETVAR NAME="thevalue" VALUE="Variables.empty"/>
                  </THEN>
                  <ELSE>
                    <SETVAR NAME="thevalue" VALUE="Variables.tempval"/>
                  </ELSE>
                </IF>
              </else>
            </if>
            <td></td>
            <td>
              <setvar NAME="doDefaultDisplay" VALUE="no"/>
              <!-- Actual Field -->
              <IF COND="Variables.thevalue=Variables.empty">
                <THEN>
                  <IMG NAME="Variables.my_attr_name" SRC="Variables.cs_imagedir/graphics/SessionVariables.locale/logo/NoImage.gif" BORDER="0" REPLACEALL="Variables.cs_imagedir,Variables.my_attr_name,SessionVariables.locale"/>
                </THEN>
                <ELSE>
                  <ASSETSET.SETASSET NAME="theImage" TYPE="Variables.ASSETTYPENAMEOUT" ID="Variables.thevalue"/>
                  <ASSETSET.GETATTRIBUTEVALUES NAME="theImage" ATTRIBUTE="Variables.ATTRIBUTENAMEOUT" LISTVARNAME="imageList" TYPENAME="Variables.ATTRIBUTETYPENAMEOUT"/>
                  <satellite.blob service="img src" width="100" border="0" name="Variables.my_attr_name" csblobid="SessionVariables.csblobid" blobtable="MungoBlobs" blobkey="id" blobwhere="imageList.value" blobcol="urldata"/>
                </ELSE>
              </IF>
              <INPUT TYPE="HIDDEN" NAME="Variables.my_attr_name" VALUE="Variables.thevalue" REPLACEALL="Variables.my_attr_name,Variables.thevalue"/>
              <![CDATA[
        <SCRIPT>
            var windowProperties = 'scrollbars=yes,resizable=yes,width=950,height=650,top=0,left=0';
        </SCRIPT>
        ]]>
              <a HREF="" onclick="javascript:window.open('ContentServer?pagename=OpenMarket/Gator/AttributeTypes/IMAGEPICKERShowImages&#38;fieldname=Variables.my_attr_name&#38;id=Variables.thevalue&#38;ATTRIBUTETYPENAMEOUT=Variables.ATTRIBUTETYPENAMEOUT&#38;ATTRIBUTENAMEOUT=Variables.ATTRIBUTENAMEOUT&#38;ASSETTYPENAMEOUT=Variables.ASSETTYPENAMEOUT&#38;CATEGORYATTRIBUTENAMEOUT=Variables.CATEGORYATTRIBUTENAMEOUT&#38;RESTRICTEDCATEGORYLISTOUT=Variables.RESTRICTEDCATEGORYLISTOUT','templateSelect', windowProperties);return false;" REPLACEALL="Variables.ATTRIBUTETYPENAMEOUT,Variables.ATTRIBUTENAMEOUT,Variables.ASSETTYPENAMEOUT,Variables.CATEGORYATTRIBUTENAMEOUT,Variables.RESTRICTEDCATEGORYLISTOUT,Variables.my_attr_name,Variables.thevalue,Variables.AttrName">
                <IMG HSPACE="10" ALIGN="ABSMIDDLE" src="Variables.cs_imagedir/graphics/SessionVariables.locale/button/content/images/popup.gif" BORDER="0" REPLACEALL="Variables.cs_imagedir,SessionVariables.locale"/>
              </a>
              <XLAT.LOOKUP KEY="dvin/Util/FlexAssets/RemoveSelect" VARNAME="deleteALT" ENCODE="false"/>
              <a href="javascript:void(0)" onclick="javascript:document.forms[0].Variables.my_attr_name.value='';document.images.Variables.my_attr_name.src= 'Variables.cs_imagedir/graphics/SessionVariables.locale/logo/NoImage.gif'; return false;" onmouseover="window.status='Variables.deleteALT'; return true;" onmouseout="window.status=' ';return true;" REPLACEALL="Variables.cs_imagedir,Variables.my_attr_name,Variables.deleteALT,SessionVariables.locale"> <img src="Variables.cs_imagedir/graphics/common/icon/iconDeleteContent.gif" ALIGN="absmiddle" HSPACE="4" border="0" alt="Variables.deleteALT" title="Variables.deleteALT" REPLACEALL="Variables.cs_imagedir,Variables.deleteALT"/></a>
            </td>
          </tr>
        </THEN>
      </IF>
    </FTCS>
*/
}