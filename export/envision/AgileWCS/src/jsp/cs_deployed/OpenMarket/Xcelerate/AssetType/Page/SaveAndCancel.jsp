<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"
%><%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld"
%><%//
// OpenMarket/Gator/FlexibleAssets/Common/SaveAndCancel
//
// INPUT
//	ssvar:
//		locale		different images are used for different locale
//	var:
//		cs_imagedir the Xcelerate base url, e.g. "/Xcelerate"
//		updatetype	"setformdefaults" means creation, save.gif is used as save button
//                  otherwise use save_changes.gif for editting.
//		ThisPage
//                  This variable is used in the checkfields() javascript.
//      DMSupported whether this asset support switching between DM and WCM views
//      cs_formmode the current view is DM or WCM
// OUTPUT
//		a Cancel button and a Save button in a row.
//      plus a optional DM or WCM link, only if the asset type support both formmode.
%><cs:ftcs>
<div dojoType="dijit.layout.ContentPane" region="top">
<div class='toolbarContent'>
<table>
    <%
    String sImageDIR = ics.GetVar("cs_imagedir");
    String sLocale = ics.GetSSVar("locale");
    boolean create = "setformdefaults".equals(ics.GetVar("updatetype"));
    %>
    <xlat:lookup key="dvin/UI/Save" varname="SaveAlt"/>
    <xlat:lookup key='<%=create ? "dvin/UI/Save" : "dvin/AT/Common/SaveChanges"%>' varname="SaveDesc" escape="true"/>
    <xlat:lookup key="dvin/UI/Admin/InspectThisDescription" varname="InspectAlt"/>
    <xlat:lookup key="dvin/UI/Inspect" varname="InspectDesc" escape="true"/>
	<xlat:lookup key="dvin/UI/GoBack" varname="GoBackAlt"/>
    <xlat:lookup key="dvin/UI/GoBack" varname="GoBackDesc" escape="true"/>
    <xlat:lookup key="UI/Forms/Refresh" varname="RefreshAlt" escape="true"/>
	<xlat:lookup key="UI/Forms/RefreshThisDescription" varname="RefreshDesc" escape="true"/>
    <%
    String sSaveAlt = ics.GetVar("SaveAlt");
    String sSaveDesc = ics.GetVar("SaveDesc");
    String sSaveImage = create ? "btnSave.png" : "btnSave.png";
    String sInspectAlt = ics.GetVar("InspectAlt");
    String sInspectDesc = ics.GetVar("InspectDesc");
	String sGoBackAlt = ics.GetVar("GoBackAlt");
    String sGoBackDesc = ics.GetVar("GoBackDesc");
	String sRefreshAlt = ics.GetVar("RefreshAlt");
    String sRefreshDesc = ics.GetVar("RefreshDesc");
    //Cleanup temp variables
    ics.RemoveVar("SaveAlt");
    ics.RemoveVar("SaveDesc");
    ics.RemoveVar("InspectAlt");
    ics.RemoveVar("InspectDesc");
	ics.RemoveVar("GoBackAlt");
    ics.RemoveVar("GoBackDesc");
	ics.RemoveVar("RefreshAlt");
    ics.RemoveVar("RefreshDesc");
    %>
	<tr>
		<td align="left">
		<ics:if condition='<%=!"EditFront".equals(ics.GetVar("ThisPage"))%>'>
		<ics:then>
			<a href="javascript:void(0)"
				onclick="document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/goBackClick.png';return cancelFlexContentForm();"
				onmouseover="window.status='<%=sGoBackAlt%>';document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/goBackHover.png';return true;"
				onmouseout="window.status='';document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/goBack.png';return true;"
				><img id="btn_cancel" src="<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/goBack.png"
					title="<%=sGoBackAlt%>" align="absmiddle"	border="0"/></a>
		</ics:then>
		</ics:if>	
			<a href="javascript:void(0)"
                onclick="document.getElementById('btn_save').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnSaveClick.png';return submitFlexContentForm();"
                onmouseover="window.status='<%=sSaveDesc%>';document.getElementById('btn_save').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnSaveHover.png';return true;"
                onmouseout="window.status='';document.getElementById('btn_save').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnSave.png';return true;"
                ><img id="btn_save" src="<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/<%=sSaveImage%>"
                    title="<%=sSaveAlt%>" align="absmiddle"
                    border="0"/></a>
			<ics:if condition='<%="EditFront".equals(ics.GetVar("ThisPage"))%>'>
			<ics:then>
				<ics:callelement element="OpenMarket/Xcelerate/Util/GenerateLink">
					<ics:argument name="assettype" value='<%=ics.GetVar("AssetType")%>'/>
					<ics:argument name="assetid" value='<%=ics.GetVar("id")%>'/>
					<ics:argument name="varname" value="urlInspectItem"/>
					<ics:argument name="function" value="inspect"/>
				</ics:callelement>
				<a href="<%=ics.GetVar("urlInspectItem")%>"
					onclick="document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnInspectClick.png';return true;"
					onmouseover="window.status='<%=sInspectDesc%>';document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnInspectHover.png';return true;"
					onmouseout="window.status='';document.getElementById('btn_cancel').src='<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnInspect.png';return true;"
					><img id="btn_cancel" src="<%=sImageDIR%>/../js/fw/images/ui/ui/toolbar/btnInspect.png"
					title="<%=sInspectAlt%>" align="absmiddle"	border="0"/></a>						
				<xlat:lookup key="dvin/Common/Edit" escape="true" varname="_XLAT_"/>
				<xlat:lookup key="dvin/UI/Admin/EditThisDescription" escape="true" varname="_ALT_"/>
				<ics:callelement element="OpenMarket/Xcelerate/Util/GenerateLink">
					<ics:argument name="assettype" value='<%=ics.GetVar("AssetType")%>'/>
					<ics:argument name="assetid" value='<%=ics.GetVar("id")%>'/>
					<ics:argument name="varname" value="urlInspectItem"/>
					<ics:argument name="function" value="edit"/>
				</ics:callelement>
				<ics:setvar name="editCheck" value="true"/>
				<ics:if condition='<%="Template".equals(ics.GetVar("AssetType"))%>'>
				<ics:then>
					<!-- check whther user is allowed to edit SiteCatalog or ElementCatalog -->
					<% boolean member = ics.UserIsMember("PageEditor");%>
					<ics:if condition='<%=member%>'>
					<ics:then>					
						<asset:getassettype  name='<%=ics.GetVar("assetname")%>' outname="at" />
						<assettype:get name="at" field="description" output="at:description"/>
						<ics:setvar name="editCheck" value="WrongACLToEditAsset"/>
					</ics:then>
					<ics:else>
						<% boolean isMember = ics.UserIsMember("ElementEditor");%>
						<ics:if condition='<%=isMember%>'>
						<ics:then>
							<asset:getassettype  name='<%=ics.GetVar("assetname")%>' outname="at" />
							<assettype:get name="at" field="description" output="at:description"/>
							<ics:setvar name="editCheck" value="WrongACLToEditAsset"/>
						</ics:then>
						</ics:if>
					</ics:else>
					</ics:if>
				</ics:then>
				</ics:if>
				<ics:if condition='<%="true".equals(ics.GetVar("editCheck"))%>'>
				<ics:then>
					<a href="<%=ics.GetVar("urlInspectItem")%>" 
						onclick="document.getElementById('btn_edit').src='<%=ics.GetVar("cs_imagedir")%>/../js/fw/images/ui/ui/toolbar/btnRefreshClick.png';"
						onmouseover="window.status='<%=ics.GetVar("_XLAT_")%>';document.getElementById('btn_edit').src='<%=ics.GetVar("cs_imagedir")%>/../js/fw/images/ui/ui/toolbar/btnRefreshHover.png';return true;"
						onmouseout="window.status='';document.getElementById('btn_edit').src='<%=ics.GetVar("cs_imagedir")%>/../js/fw/images/ui/ui/toolbar/btnRefresh.png';return true;">
						<img class="actionBarBtn" id="btn_edit" src="<%=ics.GetVar("cs_imagedir")%>/../js/fw/images/ui/ui/toolbar/btnRefresh.png" border="0" align="absmiddle" title="<%=sRefreshDesc%>" alt="<%=sRefreshAlt%>"/>
					</a>				
				</ics:then>
				</ics:if>
			</ics:then>
			</ics:if>	
            <%
            if ( "WCM".equals(ics.GetVar("cs_formmode")) ) {
            	if (String.valueOf(true).equals(ics.GetVar("DMSupported"))) {
                String formmode = "DM";
                %><xlat:lookup key='<%="dvin/FlexibleAssets/FormMode/"+formmode%>' varname="FormModeDesc" escape="true"/><%
                String sFormModeDesc = ics.GetVar("FormModeDesc");
                ics.RemoveVar("FormModeDesc");
                %>
                <img height="1" width="10" src="<%=sImageDIR%>/graphics/common/screen/dotclear.gif"/>
                <script>
                    function ChangeFormMode(formmode)
                    {
                        var form = document.forms[0];
                        if (form.elements['doSubmit'].value=="yes")
                        {
                            form.TemplateChosen.value = form.flextemplateid.value;
                            form.MultiAttrVals.value="addanother";
                            form.cs_formmode.value=formmode;

                            repostFlexContentForm();
                        }
                        return false;
                    }
                </script>
                <a href="javascript:void(0)"
                    onclick="return ChangeFormMode('<%=formmode%>');"
                    onmouseover="window.status='<%=sFormModeDesc%>';return true;"
                    onmouseout="window.status='';return true"><xlat:stream key='<%="dvin/FlexibleAssets/FormMode/"+formmode%>'/></a>
                <%
            	}
            }
            %>
		</td>
	</tr></table>
	</div>
	<div id="msgArea"></div>
	<div class="toolbarBorder"></div>
</div>
</cs:ftcs>

