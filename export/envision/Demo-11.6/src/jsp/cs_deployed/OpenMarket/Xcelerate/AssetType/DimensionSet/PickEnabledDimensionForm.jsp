<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/DimensionSet/PickEnabledDimensionForm
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.FTValList" %>
<%@ page import="COM.FutureTense.Interfaces.ICS" %>
<%@ page import="COM.FutureTense.Interfaces.IList" %>
<%@ page import="COM.FutureTense.Interfaces.Utilities" %>
<%@ page import="COM.FutureTense.Util.ftErrors" %>
<%@ page import="COM.FutureTense.Util.ftMessage"%>
<cs:ftcs>
<ics:setvar name="currentUniqueID" value='<%=Utilities.genID()%>'/>
<%
	// Get the total number of dimensions already defined.
	int iTotal = Integer.parseInt(ics.GetVar("ContentDetails:EnabledDimension:Total"));

%>
<satellite:link assembler="query" pagename="OpenMarket/Xcelerate/Actions/PickAssetPopup" outstring="url_currentUniqueID">
	<satellite:argument name='cs_environment' value='<%=ics.GetVar("cs_environment")%>'/>
	<satellite:argument name='cs_formmode' value='<%=ics.GetVar("cs_formmode")%>'/>
	<satellite:argument name='cs_PickAssetType' value='Dimension'/>
	<satellite:argument name='cs_SelectionStyle' value='single'/>
	<satellite:argument name='cs_CallbackSuffix' value='<%=ics.GetVar("currentUniqueID")%>'/>
	<satellite:argument name='cs_FieldName' value='Dimensions'/>
</satellite:link>
<script type="text/javascript" language="JavaScript">
var alDimensions = new Array();

<%-- Initialize Dimension array --%>
<%
	StringBuilder dimensionSetVals = new StringBuilder();
	for (int i = 0; i < iTotal; i++)
	{
		String type = ics.GetVar("ContentDetails:EnabledDimension:"+i+"_type");
		String id = ics.GetVar("ContentDetails:EnabledDimension:"+i);		
		%>
		<asset:load name="currDim" type='<%=type%>' objectid='<%=id%>'/>
		<asset:get name="currDim" field="name" output="currDimName"/>
        alDimensions[alDimensions.length] = '<%=type%>,<%=id%>,<%=ics.GetVar("currDimName")%>';
		<%
		if(i>0){
			dimensionSetVals.append(";");
		}
		dimensionSetVals.append(type)
			.append(":")
			.append(id)
			.append(":")
			.append(ics.GetVar("currDimName"))
			.append(":")
			.append("");
	}
%>
function PickAssetCallback_<%=ics.GetVar("currentUniqueID")%>(SelectedAssets)
{
	var AssetInfo = SelectedAssets.split(":");
	var assetid = AssetInfo[1];
	var assettype = AssetInfo[0];
	if (!containsItem(assetid))
	{
		var name = AssetInfo[2];
		alDimensions[alDimensions.length] = assettype+','+assetid+','+name;
		document.getElementById("<%=ics.GetVar("AssetType")%>:EnabledDimension:Total").value = alDimensions.length;
		showDimensions();
	}
}

function PickAssetPopup_<%=ics.GetVar("currentUniqueID")%>() {
	OpenPickAssetPopup('<%=ics.GetVar("url_currentUniqueID")%>', GetBannerHistory());
}

function pickDimensionFromTree()
{
    var EncodedString = window.parent.frames["XcelTree"].document.TreeApplet.exportSelections()+'';
	var idArray2 = EncodedString.split(':');
	for (var i = 0, l = idArray2.length-1; i < l; i++)
	{ 
		idArray2[i] =  idArray2[i] + ":";
		var idArray = "";
		idArray = idArray2[i].split(',');
		var assetcheck = unescape(idArray2[i]);
		if (assetcheck.indexOf('assettype=')!=-1 && assetcheck.indexOf('id=')!=-1)
		{
			var DecodedString = unescape(idArray2[i]);
			var dataArray = DecodedString.split(',');
			var tmpArray = dataArray[0].split('=');
			var assetid = tmpArray[1];
			tmpArray = dataArray[1].split('=');
			var assettype = tmpArray[1];
			
			if (assettype == "Dimension")
			{
				if (!containsItem(assetid))
				{
					var name = idArray[1].replace(/\+/g,' ');
					name = DecodeUTF8(name.substr(0, name.length-1));
					alDimensions[alDimensions.length] = assettype+','+assetid+','+name;
					document.getElementById("<%=ics.GetVar("AssetType")%>:EnabledDimension:Total").value = alDimensions.length;
					showDimensions();
				}
			}
			else
			{
				alert("<xlat:stream key="dvin/AT/Dimension/SelectDimensionFromTree" escape="true" encode="false"/>");
			}
		}
	}
}

function containsItem(id)
{
    for (var i = 0, l = alDimensions.length; i < l; i++)
    {
        var str = alDimensions[i];
        var vals = str.split(',');
        if (id == vals[1])
        {
            return true;
        }
    }

    return false;
}

function showDimensions()
{
    var newHTML = "<ul>";
    for (var i = 0, l = alDimensions.length; i < l; i++)
    {
        var str = alDimensions[i];
        var vals = str.split(',');
        newHTML += "<li>"+vals[2];
        newHTML += " <a href=\"javascript:removeThisDimension("+i+")\"><img src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/icon/iconDeleteContent.gif' alt='<xlat:stream key="dvin/AT/Dimension/DeleteThisDimension"/>' width='14' height='14' border='0' /></a>";
        newHTML +="</li>";
        newHTML += "<input type='hidden' name='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"_type' id='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"_type' value='"+vals[0]+"' />";
        newHTML += "<input type='hidden' name='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"' id='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"' value='"+vals[1]+"' />";
    }
    newHTML += "</ul>";
    document.getElementById('DimenstionDiv').innerHTML = newHTML;
}

function removeThisDimension(index)
{
    var tmpArray = new Array();
    for (var i = 0, l = alDimensions.length; i < l; i++)
    {
        if (index != i)
        {
            tmpArray[tmpArray.length] = alDimensions[i];
        }
    }

    alDimensions = tmpArray;
    showDimensions();
}
</script>
<div id="DimenstionDiv"></div>
<%-- Now the code to support adds --%>
<ics:if condition='<%="ucform".equals(ics.GetVar("cs_environment"))%>'>
	<ics:then>
		<div>
			<div name="typeAheadDimensionSet"> </div>	
			<div>Drag And Drop Dimension Assets</div>		
		</div>
		<script type="text/javascript" language="JavaScript">
			var selectDnDDimensionSet = function(){
				var obj=document.forms[0].elements[0], widgetName="typeAheadDimensionSet";
				var nodes= [];
				nodes = dojo.query('div[name='+widgetName+']');
				if(nodes.length === 0)
					nodes = dojo.query('input[name='+widgetName+']');
				var	typeWidgetIns=dijit.getEnclosingWidget(nodes[0]),
					valueArray=typeWidgetIns.getAllDnDValues();
				dojo.query('#DimenstionDiv > input').forEach(function(node){
					dojo.destroy(node);
				});
				if(valueArray.length > 0){
					for(var i = 0; i < valueArray.length ; i++){
						var assetDataArray =  valueArray[i];
						dojo.place("<input type='hidden' name='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"_type' value='"+assetDataArray[1]+"'/>", dojo.byId('DimenstionDiv'), 'first');
						dojo.place("<input type='hidden' name='<%=ics.GetVar("AssetType")%>:EnabledDimension:"+i+"' value='"+assetDataArray[0]+"'/>", dojo.byId('DimenstionDiv'), 'first');	
					}
				}
				document.getElementById("<%=ics.GetVar("AssetType")%>:EnabledDimension:Total").value = valueArray.length;
			}
		</script>
		<%
			StringBuilder acceptedType = new StringBuilder();
			acceptedType.append("[")
			.append("\""+ "Dimension" + "\"")
			.append("]");
		%>		
		<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/TypeAheadWidget'>			
			<ics:argument name="parentType" value='<%=acceptedType.toString()%>'/>
			<ics:argument name="subTypesForWidget" value='*'/>
			<ics:argument name="subTypesForSearch" value=''/>
			<ics:argument name="multipleVal" value="true"/>
			<ics:argument name="widgetValue" value='<%=dimensionSetVals.toString()%>'/>	
			<ics:argument name="funcToRun" value='selectDnDDimensionSet'/>
			<ics:argument name="widgetNode" value='typeAheadDimensionSet'/>
			<ics:argument name="typesForSearch" value='Dimension'/>	
			<ics:argument name="displaySearchbox" value='false'/>
			<ics:argument name="multiOrderedAttr" value='true'/>				
		</ics:callelement>
	</ics:then>	
	<ics:else>
		<ics:if condition='<%="true".equals(ics.GetVar("showSiteTree"))%>'>
			<ics:then>
				<xlat:lookup key="dvin/AT/Common/SelectFromTree" evalall="true" varname="_XLAT_alt"/>
				<a href="javascript:void(0)" onclick="pickDimensionFromTree()">
				  <ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TextButton"><ics:argument name="buttonkey" value="UI/Forms/AddSelectedItems"/></ics:callelement>
				</a>
			</ics:then>
			<ics:else>
				<A HREF="javascript:void(0);" onMouseOut="window.status='';return true;" onClick="PickAssetPopup_<%=ics.GetVar("currentUniqueID")%>();return false;"><ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TextButton"><ics:argument name="buttonkey" value="UI/Forms/Browse"/></ics:callelement></A>
			</ics:else>
		</ics:if>
	</ics:else>	
</ics:if>
</div>
<div id="DimensionsForAsset">
<input type="hidden"
       name="<%=ics.GetVar("AssetType")%>:EnabledDimension:Total"
       id="<%=ics.GetVar("AssetType")%>:EnabledDimension:Total"
       value="<%=iTotal%>" />
<%if(!"ucform".equals(ics.GetVar("cs_environment"))){%>
	<script type="text/javascript" language="JavaScript">showDimensions();</script>
<% } %>
</cs:ftcs>
