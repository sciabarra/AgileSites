<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/ShowDimensionDetails
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
		String sql = "select nid from PublicationTree where oid=(select id from AssetType where assettype='Dimension') and nparentid=(select nid from PublicationTree where oid=" + pubid +")";
	
	%>
	<ics:sql
	      sql='<%=sql%>'
	      listname="queryResults"
	      table="PublicationTree"
	      />
	<ics:if condition='<%=ics.GetList("queryResults") != null && ics.GetList("queryResults").numRows()>0%>'>
	<ics:then>
		<%
			if (ics.GetVar("dimFormPrefix") == null)
			{
				ics.SetVar("dimFormPrefix", ics.GetVar("AssetType"));
			}
			if (ics.GetVar("dimVarPrefix") == null)
			{
				ics.SetVar("dimVarPrefix", "ContentDetails");
			}
			if(null != ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:Total")){
		%>
		<asset:scatter name='theCurrentAsset' fieldlist='Dimension,Dimension-parent' prefix='<%= ics.GetVar("dimVarPrefix") %>'/>
		<asset:list type="Dimension" list="listLocales" order="description" pubid='<%=ics.GetSSVar("pubid")%>' excludevoided="true">
              <asset:argument name="subtype" value="Locale"/>
        </asset:list>
       	<ics:if condition='<%=ics.GetList("listLocales") != null && ics.GetList("listLocales").hasData()%>'>
		<ics:then>
			<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
			<tr>
				<td class="form-label-text"><xlat:stream key="dvin/AT/Dimension/Locale"/>:</td>
				<td />
				<td class="form-inset">
					<ics:callelement element="OpenMarket/Xcelerate/AssetType/Dimension/ShowDimensions"/>
				</td>
			</tr>
		</ics:then>
		</ics:if>
		<%
	    int iDPTotal = Integer.parseInt(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension-parent:Total"));
	    if (iDPTotal > 0)
	    {
	    %>
		<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
		<tr>
			<td class="form-label-text"><xlat:stream key="dvin/AT/Dimension/Translations"/>:</td>
			<td />
			<td class="form-inset">
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Dimension/LinkToCopyToNewDimensionForm"/>
			</td>
		</tr>
	    <%
	    }
			}
	%>
	</ics:then>
	</ics:if>
</ics:then>
</ics:if>
</cs:ftcs>