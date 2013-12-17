<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/PickDimensionForm
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.*,
COM.FutureTense.Util.*,
java.util.*" %>
<cs:ftcs>
<%
	// There are a couple of things going on here...
	// There is the set of variables into which the data has been scattered
	// then there is the set of variables that are being created by the form
	// that we are building.
	if (ics.GetVar("dimFormPrefix") == null)
	{
		ics.SetVar("dimFormPrefix", ics.GetVar("AssetType"));
	}
	if (ics.GetVar("dimVarPrefix") == null)
	{
		ics.SetVar("dimVarPrefix", "ContentDetails");
	}
    // Get the total number of dimensions already defined.

	int iTotal = Integer.parseInt(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:Total"));
	String totalS = ics.GetVar(ics.GetVar("dimFormPrefix")+":Dimension:Total");
	if ( totalS != null && totalS.length() > 0)
	{
	  iTotal = Integer.parseInt( totalS );
	  for ( int j = 0; j < iTotal; j ++ )
	  {	
          ics.SetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+j+"_type", ics.GetVar( ics.GetVar("dimFormPrefix")+":Dimension:"+j+"_type" ));
		  ics.SetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+j, ics.GetVar( ics.GetVar("dimFormPrefix")+":Dimension:"+j ));
      }
  }
%>

<script language="JavaScript">
  function selectDimension( opt )
  {
    var selected = new Array();
    var index = 0;
    for (var intLoop=0; intLoop < opt.length; intLoop++)
    {
      if (opt[intLoop].selected && opt[intLoop].value.length > 0)
      {
        document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:"+index).value=opt[intLoop].value;
        document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:"+index+"_type").value="Dimension";
        index ++;
      }
    }
	  document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total").value=index;
  }
</script>
<div id="DimensionsForAsset">
<%
if(ics.GetVar("ContentDetails:Dimension:0") != null)
{
	iTotal = 1;
	ics.SetVar(ics.GetVar("dimVarPrefix")+":Dimension:0_type", "Dimension" );
}
%>
<input type="hidden"
       name="<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total"
       id="<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total"
       value="<%=iTotal%>" />
<%
    // "preselected_dimensionid" could be passed from other page. It's mainly used for pre-selecting the locale in the list.
    String sPreSelectedDimensionID = ics.GetVar("preselected_dimensionid");
    if (sPreSelectedDimensionID != null)
    {   // A pre-selected dimension asset is passed, show its name as well as the hidden inputs that contain the information
        %>
        <asset:load name="selDimension" type='<%=ics.GetVar("preselected_dimensiontype")%>' objectid="<%=sPreSelectedDimensionID%>" />
        <asset:get name="selDimension" field="description" output="DimName" />
        <%=ics.GetVar("DimName")%>
        <input type="hidden"
               name="<%=ics.GetVar("dimFormPrefix")%>:Dimension:0_type"
               id="<%=ics.GetVar("dimFormPrefix")%>:Dimension:0_type"
               value="<%=ics.GetVar("preselected_dimensiontype")%>" />
        <input type="hidden"
               name="<%=ics.GetVar("dimFormPrefix")%>:Dimension:0"
               id="<%=ics.GetVar("dimFormPrefix")%>:Dimension:0"
               value="<%=sPreSelectedDimensionID%>" />
        <script type="text/javascript" language="JavaScript">
            document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total").value='1';
        </script>
        <%
    }
    else
    {
        int len = 0;
        %>
        <ics:if condition='<%= ics.GetList("listLocales") != null && ics.GetList("listLocales").hasData() %>' >
        <ics:then>
            <select dojoType="fw.dijit.UISimpleSelect" name='dimensions' showErrors="false" clearButton="true" size='1'>
            <ics:if condition='<%="true".equals(ics.GetVar("defaultFormStyle"))%>'>
			<ics:then>
            <script event='startup' type='dojo/connect'>
				dojo.addClass(this.domNode, 'defaultFormStyle');
			</script>
			</ics:then>
			</ics:if>
			<%-- Check to make sure that the current asset is not the master asset for the locale.If it's the master asset then dont give the option to select blank Locale --%>
            <%int translationsCount = 0;%>  
			
			<asset:isempty name='<%=ics.GetVar("assetname")%>' field="createddate" output="hasnodata"/>
			<ics:if condition='<%=ics.GetVar("hasnodata").equals("true")%>' >
			<ics:then>
				<asset:get name='<%=ics.GetVar("assetname")%>' field="createddate" output="assetcreateddate"/>
				<ics:if condition='<%=!(ics.GetVar("empty").equals(ics.GetVar("assetcreateddate")))%>'>
				<ics:then>
					<asset:gettranslations name='<%=ics.GetVar("assetname")%>' list="translatedLocales" />
				</ics:then>
				</ics:if>
			</ics:then>	
			</ics:if>

            <ics:if condition='<%= ics.GetList("translatedLocales") != null && ics.GetList("translatedLocales").hasData() %>' >
              <ics:then>
                <%translationsCount = ics.GetList("translatedLocales").numRows();%>
              </ics:then>
            </ics:if>
            <%
            String sDimParentID = "";
			int iDPTotal = Integer.parseInt(ics.GetVar("ContentDetails:Dimension-parent:Total"));
			for (int i = 0; i < iDPTotal; i++)
			{
				String type = ics.GetVar("ContentDetails:Dimension-parent:"+i+":asset_type");
				String id = ics.GetVar("ContentDetails:Dimension-parent:"+i+":asset");
				String group = ics.GetVar("ContentDetails:Dimension-parent:"+i+"group");
				sDimParentID = id;
			}
			if(sDimParentID.equals("") || !sDimParentID.equals(ics.GetVar("id")) || translationsCount == 0){
			%>
			<option value=""></option>
			<%}%>
            <ics:listloop listname='listLocales' >
                <ics:listget listname='listLocales' fieldname='id' output='did'/>
                <ics:listget listname='listLocales' fieldname='description' output='ddescription'/>
                <%
                len++;
                String did = ics.GetVar( "did" );
                String selected = "";
				
				/*
					This is a grave hack and I hate to do this. But this has to be done so that we can make Dimensions work with default start menu items.
					For some reason asset gather of a Dimension default value (obtained from start menu item) does not seem to work.
					So we are forced to set the default Dimension value (if available from Start menu item) as a prefixed ICS variable and 
					read it here. Because asset gather/scatter does not work as expected with Dimensions(atleast from the NewContentForm.xml), 
					the expression ContentDetails:Dimension:Total always returns 0 or null. If we have a default value for dimension we need to set the 
					'iTotal' variable accordingly so that we can properly set the default value for Dimension in the asset creation screen.
				*/
				
                for (int i = 0; i < iTotal; i++)
                {
                    String id = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+i);
                    if ( null != id && id.equals( did ) )
                    {
                        selected = "selected";
					    ics.SetVar("selectedDimId", id);
					    ics.SetVar("selectedIndex", i);	
                        break;
                    }
                }
                %>
                <option value='<%=ics.GetVar("did")%>' <%=selected%> ><string:stream variable="ddescription"/></option>
            </ics:listloop>
			<script type='dojo/connect' event='onChange'>
				document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:0").value=this._getValueAttr();
				document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:0_type").value="Dimension";
				document.getElementById("<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total").value= 1;
			</script>
            </select>
        </ics:then>
        <ics:else>
         	<select dojoType="fw.dijit.UISimpleSelect" name='dimensions' showErrors="false" clearButton="true" size='1'>
	         	<ics:if condition='<%="true".equals(ics.GetVar("defaultFormStyle"))%>'>
				<ics:then>
		            <script event='startup' type='dojo/connect'>
						dojo.addClass(this.domNode, 'defaultFormStyle');
					</script>
				</ics:then>
				</ics:if>
				<option value=""></option>
         	</select>
        </ics:else>
        </ics:if>
        <%
        for( int l = 0; l < len; l ++ )
        {
            %>
            <input type="hidden"
                   name="<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=l%>_type"
                   id="<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=l%>_type"
                   value="<%=ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+l+"_type")%>" />
            <input type="hidden"
                   name="<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=l%>"
                   id="<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=l%>"
                   value="<%=StringEscapeUtils.escapeHtml(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension:"+l))%>" />
            <%
        }
    }

if (ics.GetVar("selectedDimId") != null)
{%>
	<script language="JavaScript">
		document.getElementById('<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=ics.GetVar("selectedIndex")%>').value = '<%=ics.GetVar("selectedDimId")%>';
		document.getElementById('<%=ics.GetVar("dimFormPrefix")%>:Dimension:<%=ics.GetVar("selectedIndex")%>_type').value = "Dimension";
		document.getElementById('<%=ics.GetVar("dimFormPrefix")%>:Dimension:Total').value='1';  
	</script>
<%}%>


<%-- Now make sure the dimparent info is maintained for the next save.  In case we have to add
     one more dimension parent, leave room. --%>
<div id="dimParents">
<%
final int iDPTotal = Integer.parseInt(ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension-parent:Total"));
for (int i = 0; i < (iDPTotal+1); i++)
{
	String type = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension-parent:"+i+":asset_type");
    String id = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension-parent:"+i+":asset");
    String group = ics.GetVar(ics.GetVar("dimVarPrefix")+":Dimension-parent:"+i+":group");
	%>
	<input type="hidden"
           name="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:asset_type"
           id="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:asset_type"
           value="<%=type%>"/>
    <input type="hidden"
           name="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:asset"
           id="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:asset"
           value="<%=id%>"/>
    <input type="hidden"
           name="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:group"
           id="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:<%=i%>:group"
           value="<%=group%>"/>
	<%
}
%>
<input type="hidden"
       name="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:Total"
       id="<%=ics.GetVar("dimFormPrefix")%>:Dimension-parent:Total"
       value="<%=iDPTotal%>"/>

</div>
</div>
</cs:ftcs>