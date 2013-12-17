<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="assettype" uri="futuretense_cs/assettype.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/AdvCols/ListManualShowItems
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

<%
	StringBuilder sbMultList = new StringBuilder();
	int counter = 0;
%>
<ics:listloop listname="recList">
	<ics:listget listname="recList" fieldname="assettype" output="aType" />
	<ics:listget listname="recList" fieldname="assetid" output="aId" />
	<asset:list list="thisElement" field1="id" value1='<%=ics.GetVar("aId")%>'  type='<%=ics.GetVar("aType")%>'/> 
	<assettype:load name="AssetType" field="assettype" value='<%=ics.GetVar("aType")%>'/>
	<assettype:get name="AssetType" field="description" output="aDesc"/>
	<%
		if (counter != 0)
		{
			sbMultList.append(";");
		}
		sbMultList.append(ics.GetVar("aType"))
			.append(":")
			.append(ics.GetVar("aId"))
			.append(":")
			.append(ics.GetList("thisElement").getValue("name") + " (" + ics.GetVar("aDesc") + ")")
			.append(":")
			.append("");										
		counter++;
	%>	
</ics:listloop>	
<ics:setvar name="widValueList" value="<%=sbMultList.toString()%>" />			
</cs:ftcs>