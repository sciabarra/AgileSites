<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/DimensionSet/ShowEnabledDimensionDetails
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.*,COM.FutureTense.Util.*" %>
<cs:ftcs>
<ics:if condition='<%=!"true".equals(System.getProperty("cs.disable.dimensions.in.ui"))%>'>
<ics:then>
<%
    String pubid = (String)ics.GetSSVar("pubid");
    String sql = "select nid from PublicationTree where oid=(select id from AssetType where assettype='DimensionSet') and nparentid=(select nid from PublicationTree where oid=" + pubid +")";
%>
<ics:sql
      sql='<%=sql%>'
      listname="queryResults"
      table="PublicationTree"
      />
<ics:if condition='<%=ics.GetList("queryResults") != null && ics.GetList("queryResults").numRows()>0%>'>
<ics:then>
	<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer" /> 
	<tr>
		<td class="form-label-text"><xlat:stream key="dvin/AT/DimensionSet/EnabledDimensions"/>:</td>
		<td />
		<td class="form-inset">
			<ics:callelement element="OpenMarket/Xcelerate/AssetType/DimensionSet/ShowEnabledDimensions"/>
		</td>
	</tr>
</ics:then>
</ics:if>
</ics:then>
</ics:if>

</cs:ftcs>