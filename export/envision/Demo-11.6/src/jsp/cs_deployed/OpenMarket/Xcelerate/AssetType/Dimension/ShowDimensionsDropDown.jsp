<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>   
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/ShowDimensionsDropDown
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
<script language="JavaScript">
  function selectedDimension(opt)
  {
    document.getElementsByName("selectedDim")[0].value=opt.options[opt.selectedIndex].text;
  }
</script>
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

    %>
	<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer" /> 
	
	<tr>
		<td class="form-label-text"><xlat:stream key="dvin/AT/Dimension/Locale"/>:</td>
		<td />
		<td class="form-inset">
             <asset:list type="Dimension" list="listLocales" order="description" pubid='<%=ics.GetSSVar("pubid")%>' excludevoided="true">
              <asset:argument name="subtype" value="Locale"/>
        </asset:list>
        <ics:if condition='<%= ics.GetList("listLocales") != null && ics.GetList("listLocales").hasData() %>' >
        <ics:then>
            <select name='dimensionssearch' size='1' onchange='selectedDimension(this)'>
                <option value=""></option>
            <ics:listloop listname='listLocales' >
                <ics:listget listname='listLocales' fieldname='id' output='did'/>
                <ics:listget listname='listLocales' fieldname='description' output='ddescription'/>
                <%
                    String selected = "";
                    if (ics.GetVar("selectedDim")!= null){
                        if (ics.GetVar("selectedDim").equals(ics.GetVar("ddescription")))
                            selected = "selected";
                    }
                %>
                <option value='<%=ics.GetVar("did")%>' name='<%=ics.GetVar("ddescription")%>' <%=selected%> ><%=ics.GetVar("ddescription")%> </option>
            </ics:listloop>
            </select>
        </ics:then>
        </ics:if>
        </td>
	</tr>
</ics:then>
</ics:if>
    <!-- hidden field for building filter string and for editing search-->
    <input type="hidden" name="selectedDim" value='<%=ics.GetVar("selectedDim")%>'/>

</cs:ftcs>