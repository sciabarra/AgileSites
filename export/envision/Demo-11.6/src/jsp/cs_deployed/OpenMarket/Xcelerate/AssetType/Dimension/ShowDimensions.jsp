<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/ShowDimensions
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.*" %>
<%@ page import="COM.FutureTense.Util.*" %>
<cs:ftcs>
<%
int iTotal = Integer.parseInt(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:Total"));
boolean bRevisionInspect = "true".equals(ics.GetVar("revisionInspect"));
%>
<ics:if condition='<%=iTotal > 0%>'>
<ics:then>
	<%
	for (int i = 0; i < iTotal; i++)
	{
		// load the dimension so we can spit out its name.
		// todo: replace this with a tag?
		String type = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+i+"_type");
		String id = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+i);
		boolean bComma = false;
	%>
		<asset:load name="currDim" type="<%=type%>" objectid="<%=id%>" />
		<asset:get name="currDim" field="description"/>
		<%=bComma? ", " : ""%>
		<%
		if (bRevisionInspect)
		{
			// In Revision Inspect popup, just show the description.
			%><string:stream variable="description"/><%
		}
		else
		{
			%>
			<ics:callelement element="OpenMarket/Xcelerate/Util/GenerateLink">
				<ics:argument name="assettype" value="<%=type%>"/>
				<ics:argument name="assetid" value="<%=id%>"/>
				<ics:argument name="varname" value="urlInspectItem"/>
				<ics:argument name="function" value="inspect"/>
			</ics:callelement>
			<a href="<%=ics.GetVar("urlInspectItem")%>"><string:stream variable="description"/></a>
			<%
		}

		bComma = true;
	}
	%>
</ics:then>
<ics:else>
	<span class="disabledText"><xlat:stream key='UI/Forms/NotAvailable' encode="false" escape="true"/></span>
</ics:else>
</ics:if>

</cs:ftcs>