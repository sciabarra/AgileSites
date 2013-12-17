<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/DimensionSet/ShowEnabledDimensions
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.*" %>
<%@ page import="COM.FutureTense.Util.*" %>
<cs:ftcs>
<%
int iTotal = Integer.parseInt(ics.GetVar("ContentDetails:EnabledDimension:Total"));
%>
<ics:if condition='<%=iTotal > 0%>'>
<ics:then>
	<ul>
		<%
		for (int i = 0; i < iTotal; i++)
		{
			// load the dimension so we can spit out its name.
			// todo: replace this with a tag?
			String type = ics.GetVar("ContentDetails:EnabledDimension:"+i+"_type");
			String id = ics.GetVar("ContentDetails:EnabledDimension:"+i);
		%>
			<asset:load name="currDim" type="<%=type%>" objectid="<%=id%>" />
			<asset:get name="currDim" field="name"/>
			<li><string:stream variable="name"/></li>
		<%
		}
		%>
	</ul>
</ics:then>
</ics:if>

</cs:ftcs>