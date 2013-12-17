<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ page import="COM.FutureTense.Interfaces.*,
                COM.FutureTense.Util.*,
                java.util.*,
                com.fatwire.assetapi.data.*,
                com.openmarket.xcelerate.asset.*"%>
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/ShowCopyToNewDimensionForm
//
// INPUT
//
// OUTPUT
//%>
<cs:ftcs>
<string:encode variable="contentPane_ID" varname="contentPane_ID"/>

<asset:load name="theCurrentAsset" objectid='<%=ics.GetVar("id")%>' type='<%=ics.GetVar("AssetType")%>'/>
<asset:scatter name="theCurrentAsset" prefix="ContentDetails"/>

<% 
	if (ics.GetVar("dimVarPrefix") == null)
	{
		ics.SetVar("dimVarPrefix", "ContentDetails");
	}	
%>

<script type="text/javascript">

	var translateAssetTab = function (dimId, dimType, assetId, assetType) {
		var docId = parent.fw.util.buildDocId({type: assetType, id: assetId});
		var thisView = SitesApp.getActiveView();
		SitesApp.event(docId, "translate", {id: dimId, type: dimType});
		var visibilityHandler = dojo.connect(SitesApp.tabController, 'onViewVisible', function(presentView){
			if (thisView === presentView)
				reloadContentPane();
			// May be later we should disconnect	
		});		
	};
	
	var deleteAssetT = function(aId, aType, aName){
		var docId = parent.fw.util.buildDocId({type: aType, id: aId});
		var thisView = SitesApp.getActiveView();
		SitesApp.event(docId, "delete", {id: aId, type: aType});
		var visibilityHandler = dojo.connect(SitesApp.tabController, 'onViewVisible', function(presentView){
			if (thisView === presentView)
				reloadContentPane();
			// May be later we should disconnect			
		});	
	};
	
	var reloadContentPane = function(){
		var contentPane = dijit.byId('<%= ics.GetVar("contentPane_ID")%>');
		if (contentPane && contentPane.declaredClass === "dojox.layout.ContentPane")
			contentPane.refresh();
	};
	
</script>

<%-- Now put the dim parents onto the page --%>
<div id="dimParents">
<%
boolean bRevisionInspect = "true".equals(ics.GetVar("revisionInspect"));

String sDimParentID = "";
int iDPTotal = Integer.parseInt(ics.GetVar("ContentDetails:Dimension-parent:Total"));
for (int i = 0; i < iDPTotal; i++)
{
	String type = ics.GetVar("ContentDetails:Dimension-parent:"+i+":asset_type");
    String id = ics.GetVar("ContentDetails:Dimension-parent:"+i+":asset");
    String group = ics.GetVar("ContentDetails:Dimension-parent:"+i+"group");
	sDimParentID = id;
    %>
	<input type="hidden" name="Dimension-parent:<%=i%>:asset_type" value="<%=type%>"/>
    <input type="hidden" name="Dimension-parent:<%=i%>:asset" value="<%=id%>"/>
    <input type="hidden" name="Dimension-parent:<%=i%>:group" value="<%=group%>"/>
	<%
}
%>
<input type="hidden" name="Dimension-parent:Total" value="<%=iDPTotal%>"/>
</div>

<%-- Look up words from LocaleStrings --%>
<xlat:lookup key="dvin/Common/Remove" escape="true" varname="_XLAT_"/>
<xlat:lookup key="dvin/Common/Remove" varname="_ALT_"/>
<xlat:lookup key="dvin/AT/Dimension/TranslateThisAsset" escape="true" varname='_TRANSLATE_'/>

<%-- Look up all of the translations of this asset and put them into the translated locales list--%>
<asset:gettranslations name="theCurrentAsset" list="translations"/>
<string:encode varname="ContentDetails:name" value='<%=ics.GetVar("ContentDetails:name")%>'/>
<string:encode variable="cs_imagedir" varname="cs_imagedir"/>
<%
HashMap<String, HashMap<String,String>> hmTranslatedLocales = new HashMap<String, HashMap<String,String>>();
HashMap<String, String> hmAsset;
for (IList row : new IterableIListWrapper( ics.GetList("translations") ))
{
	String type = row.getValue("TYPE");
	String id = row.getValue("OBJECTID");
	%>
    <asset:load name="translation" type='<%=row.getValue("TYPE")%>' objectid='<%=row.getValue("OBJECTID")%>'/>
    <asset:get name="translation" field="name" output='assetName'/>
    <asset:getlocales name="translation" list="translatedLocales" />
    <%
    hmAsset = new HashMap<String, String>();
    hmAsset.put("assettype", type);
    hmAsset.put("assetid", id);
    hmAsset.put("name", ics.GetVar("assetName"));
    for ( IList ilistLocales : new IterableIListWrapper( ics.GetList("translatedLocales") ) )
    {
        hmTranslatedLocales.put(ilistLocales.getValue("OBJECTID"), hmAsset);
    }
}

// Get all locales that the current asset belongs to and add current asset to the translated locales list
hmAsset = new HashMap<String, String>();
hmAsset.put("assettype", ics.GetVar("AssetType"));
hmAsset.put("assetid", ics.GetVar("id"));
hmAsset.put("name", ics.GetVar("ContentDetails:name"));

int iTotal = Integer.parseInt(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:Total"));
ArrayList<String> alistCurDimensions = new ArrayList<String>();
for (int i = 0; i < iTotal; i++)
{
    String id = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+i);
    hmTranslatedLocales.put(id, hmAsset);
}
%>
<%-- Get all locales for the site --%>
<asset:list type="Dimension" list="listLocales" order="name" pubid='<%=ics.GetSSVar("pubid")%>' excludevoided="true">
      <asset:argument name="subtype" value="Locale"/>
</asset:list>

    <table BORDER="0" CELLSPACING="0" CELLPADDING="0" style="margin:0px" class="width-outer-50">
		<tr>
			<td></td><td class="tile-dark" HEIGHT="1"><IMG WIDTH="1" HEIGHT="1" src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/dotclear.gif'/></td><td></td>
		</tr>
	<tr>
		<td class="tile-dark" WIDTH="1" NOWRAP="nowrap"><BR /></td>
		<td >
			<table class="width-inner-100" cellpadding="0" cellspacing="0" border="0" bgcolor="#ffffff"><tr><td colspan="10" class="tile-highlight"><IMG WIDTH="1" HEIGHT="1" src="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/dotclear.gif"/></td></tr>
			<tr>
				<td class="tile-a" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">
					<DIV class="new-table-title"><BR/></DIV>
				</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">
					<DIV class="new-table-title"><xlat:stream key="dvin/Common/Name"/></DIV>
				</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">
					<DIV class="new-table-title"><xlat:stream key="dvin/UI/Admin/Locale"/></DIV>
				</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;&nbsp;&nbsp;&nbsp;</td>
				<td class="tile-b" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">
					<DIV class="new-table-title"><xlat:stream key="dvin/UI/Admin/Action"/></DIV>
				</td>
				<td class="tile-c" background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/grad.gif">&nbsp;</td>
			</tr>
			<tr><td colspan="10" class="tile-dark"><IMG WIDTH="1" HEIGHT="1" src="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/dotclear.gif"/></td></tr>
			
						


<%
for ( IList row : new IterableIListWrapper( ics.GetList("listLocales") ) )
{
    %>
	<tr>
				<td><BR/></td><td><BR/></td><%
	String sLocale = row.getValue("name");
    String sLocaleDesc = row.getValue("description");
	ics.SetVar("localeDesc",sLocaleDesc);
    String sLocaleID = row.getValue("id");

    if ( hmTranslatedLocales.containsKey( sLocaleID ) )
    {
        // A translation asset has already been created for the current locale, show the translation asset info.
        hmAsset = hmTranslatedLocales.get( sLocaleID );
        String type = hmAsset.get("assettype");
        String id = hmAsset.get("assetid");

        String sURL = hmAsset.get("name");
        %>
        <xlat:lookup key="dvin/AT/Dimension/MasterAsset" varname="masterAssetXLAT"/>
        <%
        String sRoot = id.equals(sDimParentID) ? " ("+ics.GetVar("masterAssetXLAT")+")" : null;
        String sDeleteLink = "";

        if (sRoot == null)
        {
        	if (bRevisionInspect)
			{
				// In Revision Inspect popup.
				sRoot = "";
			}
			else
			{
				// Create make root link
				%>
				<satellite:link assembler="query" pagename="OpenMarket/Xcelerate/Actions/MakeRootFront" outstring="makeRootURL">
					<satellite:parameter name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
					<satellite:parameter name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
					<satellite:parameter name="AssetType" value='<%=ics.GetVar("AssetType")%>'/>
					<satellite:parameter name="id" value='<%=ics.GetVar("id")%>'/>
					<satellite:parameter name="MakeRootID" value='<%=id%>'/>
					<satellite:parameter name="MakeRootType" value='<%=type%>'/>
					<satellite:parameter name="docId" value='<%=ics.GetVar("documentId")%>'/>
					<satellite:parameter name="rootId" value='<%=ics.GetVar("id")%>'/>
				</satellite:link>
                <xlat:lookup key="fatwire/Alloy/UI/MakeMaster" varname="makemasterXLAT"/>
                <%
				sRoot = "<a href=\""+ics.GetVar("makeRootURL")+"\">"+ics.GetVar("makemasterXLAT")+"</a>";
			}
        }

        if (! id.equals( ics.GetVar("id") ))
        {
        	if (!bRevisionInspect)
			{
				// Not in Revision Inspect popup
				%>
				<%-- Get URL for the detail screen --%>
				
				<ics:callelement element="OpenMarket/Xcelerate/Util/GenerateLink">
					<ics:argument name="assettype" value="<%=type%>"/>
					<ics:argument name="assetid" value="<%=id%>"/>
					<ics:argument name="varname" value="urlInspectItem"/>
					<ics:argument name="function" value="inspect"/>
				</ics:callelement>
				<%
				sURL = "<a href=\""+ics.GetVar("urlInspectItem")+"\">"+sURL+"</a>";				
			}
        }

        if (!bRevisionInspect)
		{
			%>
			<%-- Get URL for the delete screen --%>
			<% if("ucform".equals(ics.GetVar("cs_environment"))){%>	
				<ics:setvar name="deleteURL" value='<%="javascript:deleteAssetT(\'" + id + "\',\'" + type + "\',\'" + hmAsset.get("name") + "\');"%>' />
			<% } else { %>
				 <satellite:link assembler="query" pagename="OpenMarket/Xcelerate/Actions/DeleteFront" outstring="deleteURL">
				<satellite:parameter name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
				<satellite:parameter name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
				<satellite:parameter name="AssetType" value='<%=type%>'/>
				<satellite:parameter name="ccsourceid" value='<%=ics.GetVar("id")%>'/>
				<satellite:parameter name="id" value='<%=id%>'/>
			</satellite:link>
			<% } %> 			
        	<%
			sDeleteLink = "<a href=\""+ics.GetVar("deleteURL")+"\" onmouseover=\"window.status='"+ics.GetVar("_XLAT_")+"';return true;\" onmouseout=\"window.status='';return true\">" +
						"<img height=\"14\" width=\"14\" src=\""+ics.GetVar("cs_imagedir")+"/graphics/common/icon/iconDeleteContent.gif\" hspace=\"2\" border=\"0\" alt=\""+ics.GetVar("_ALT_")+"\"/>" +
						"</a>";
		}
        %>
       <td><%=sDeleteLink%></td>
	   <td><BR/></td>
	   <td><%=sURL%></td>
	    <td><BR/></td>
            <td><em><string:stream variable="localeDesc"/></em></td>
			
            <td><BR/></td>
			<td><%=sRoot%></td>
 			<td><BR/></td>
         
        <%
    }
    else if (!bRevisionInspect)
    {
        // A translation asset hasn't been created for the current locale, show the create link.
        %>
        <%-- Get URL for the copy screen --%>
		<% if("ucform".equals(ics.GetVar("cs_environment"))){%>
			<ics:setvar name="referURL" value='<%="javascript:translateAssetTab(\'" + row.getValue("id") + "\',\'Dimension\',\'" + ics.GetVar("id") 
									+ "\',\'" + ics.GetVar("AssetType") + "\');"%>' />
		<% } else { %>
			 <satellite:link assembler="query" pagename="OpenMarket/Xcelerate/Actions/TranslateFront">
				<satellite:parameter name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
				<satellite:parameter name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
				<satellite:parameter name="ccsourceid" value='<%=ics.GetVar("id")%>'/>
				<satellite:parameter name="id" value='<%=ics.GetVar("id")%>'/>
				<satellite:parameter name="AssetType" value='<%=ics.GetVar("AssetType")%>'/>
				<satellite:parameter name="preselected_dimensiontype" value='Dimension'/>
				<satellite:parameter name="preselected_dimensionid" value='<%=row.getValue("id")%>'/>
			</satellite:link>
		<% } %>     
 			 
			<td>
				<br/>
			</td><td><br/></td><td><span class="disabledText"><xlat:stream key="UI/Forms/NotAvailable"/></span></td><td><br/></td><td><em><string:stream variable="localeDesc"/></em></td><td><br/></td>
			
			<% if("true".equals(ics.GetVar("_enableForms")) || "ucform".equals(ics.GetVar("cs_environment"))){ %>
	            <td>
					<a href="<%=ics.GetVar("referURL")%>" onmouseover="window.status='<%=ics.GetVar("_TRANSLATE_")%>';return true;" onmouseout="window.status='';return true"><span class="action-text"><%=ics.GetVar("_TRANSLATE_")%></span></a>
				</td>
			<% } else{
			 %><td><BR/></td><%
			}%>
        
        <%
   %><td><BR/></td><%
	}
}
%><td><BR/></td>
</tr>
</table>
		</td>
		<td class="tile-dark" WIDTH="1" NOWRAP="nowrap"><BR /></td>
		</tr>
		<tr>
		<td colspan="3" class="tile-dark" HEIGHT="1"><IMG WIDTH="1" HEIGHT="1" src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/dotclear.gif'/></td>
		</tr>
		<tr>
		<td></td><td background="<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/shadow.gif"><IMG WIDTH="1" HEIGHT="5" src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/screen/dotclear.gif'/></td><td></td>
		</tr>
	</table>
</cs:ftcs>
