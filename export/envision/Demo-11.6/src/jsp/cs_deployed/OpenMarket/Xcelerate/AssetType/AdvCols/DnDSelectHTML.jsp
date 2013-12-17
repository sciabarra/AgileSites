<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld"%>
<%//
// OpenMarket/Xcelerate/AssetType/AdvCols/DnDSelectHTML
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
<string:encode varname="appendStr" variable="appendStr" />
<string:encode varname="cs_imagedir" variable="cs_imagedir" />

<xlat:lookup key='dvin/AT/Common/SelectFromTree' varname="SelectFromTree"/>
<div style="display:inline-block">
	<% if(!"ucform".equals(ics.GetVar("cs_environment")) && "true".equals(ics.GetVar("showSiteTree"))){%>
	<div style="display:inline-block; margin-bottom: 10px;">
		<a href="javascript:void(0)" onclick="return SelectFromTreeRecoDndMult('typeAheadReco_<%=ics.GetVar("appendStr")%>')"
			onmouseover="window.status=' '; return true;" onmouseout="window.status=' ';return true;">
		<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TextButton"><ics:argument name="buttonkey" value="UI/Forms/AddSelectedItems"/></ics:callelement></a>
	</div>
	<%}%>
	<div name="typeAheadReco_<%=ics.GetVar("appendStr")%>"> </div>
	<%if(!"ucform".equals(ics.GetVar("cs_environment"))){%>
	<div style="display:inline-block; vertical-align:middle;">
		<img border="0" id="_fwTypeAheadHelpImgReco_<%=ics.GetVar("appendStr")%>" src="<%=ics.GetVar("cs_imagedir")+"/graphics/common/icon/help_new.png"%>" 
		<% if("true".equals(ics.GetVar("showSiteTree"))){%>
			alt="Select From Tree" title="Select From Tree"/>
		<%}else{%>
			alt="Type To Search And Select" title="Type To Search And Select"/>			
		<%}%>
		<script type="text/javascript">
			dojo.addOnLoad(function(){
				var displayInfo = {
					'<xlat:stream key="UI/UC1/JS/AcceptedAssetTypes" locale='<%=ics.GetVar("locale")%>'></xlat:stream>': 'Any',
					'<xlat:stream key="UI/UC1/JS/AcceptedSubTypes" locale='<%=ics.GetVar("locale")%>'></xlat:stream>': 'Any',
					'<xlat:stream key="UI/UC1/JS/AcceptsMultiple" locale='<%=ics.GetVar("locale")%>'></xlat:stream>': true	
				};
				var dijitTooltip = new fw.ui.dijit.HoverableTooltip({
					connectedNodes: ["_fwTypeAheadHelpImgReco_<%=ics.GetVar("appendStr")%>"], 
					content: fw.util.createHoverableTooltip(displayInfo),
					position:'below' /*Only below supported */
				});
			});								
		</script>
	</div>
	<%}%>
	</div>
</cs:ftcs>