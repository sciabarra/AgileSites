<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"%>
<%//
// OpenMarket/Xcelerate/AssetType/Template/IContentSiteEntries
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
<%@ page import="com.openmarket.xcelerate.commands.PubTargetManagerDispatcher"%> 
<%@ page import="COM.FutureTense.Interfaces.FTValList"%>
<cs:ftcs>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/BasicEnvironment"/>
<html>
<head>
<satellite:link assembler="query" pagename="OpenMarket/Xcelerate/AssetType/Template/ContentSiteEntries" outstring="ContentSiteEntriesURL">
	<satellite:argument name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
	<satellite:argument name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
	<satellite:argument name="cs_imagedir" value='<%=ics.GetVar("cs_imagedir")%>'/>
	<satellite:argument name="id" value='<%=ics.GetVar("id")%>'/>
	<satellite:argument name="assetname" value='<%=ics.GetVar("assetname")%>'/>
	<satellite:argument name="AssetType" value='<%=ics.GetVar("AssetType")%>'/>
</satellite:link>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/prototype.js"></script>
<script type="text/javascript">


function init(){
	Ajax.Responders.register({
		onCreate:showLoading,
		onComplete:hideLoading
	});
	updatePanel('_t1','<%=ics.GetVar("ContentSiteEntriesURL")%>&divContainerId=_t1');
}

function updatePanel(divId,url){
	new Ajax.Request(url,{
		method : 'get',
		onComplete: function(transport){
			$(divId).update(transport.responseText);
		}
	});
}

function showLoading(){
	$('ajaxLoading').style.display='block';
}

function hideLoading(){
	$('ajaxLoading').style.display='none';
}
</script>
<link href='<%=ics.GetVar("cs_imagedir")%>/data/css/<%=ics.GetVar("locale")%>/publish.css' rel="styleSheet" type="text/css"/>
</head>
<body onLoad="init()">
<div id="_t1"></div>
<div id="ajaxLoading" style="width:200px;height:100px;position:absolute; top: 400px; display:none; left: 512px;" bgcolor="white">
<table width="100%" height="100%" cellspacing="0" cellpadding="0" bgcolor="#ffffff" align="center" style="border: 1px solid rgb(204,204,204);">
<tbody>
<tr>
<td valign="middle" align="center">
<img src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/icon/wait_ax.gif'/>
<br/>
<br/>
<b>
<span id="loadingMsg"><xlat:stream key="dvin/UI/Loading"/></span>
<img src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/icon/short_Progress.gif'/>
</b>
<a id="hider2" style="cursor: pointer; text-decoration: underline;"/>
</td>
</tr>
</tbody>
</table>
</div>
</body>
</html>	
</cs:ftcs>