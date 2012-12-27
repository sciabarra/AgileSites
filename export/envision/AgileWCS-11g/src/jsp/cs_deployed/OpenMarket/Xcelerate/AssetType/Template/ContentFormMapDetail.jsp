<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="render" uri="futuretense_cs/render.tld" %>
<%@ taglib prefix="property" uri="futuretense_cs/property.tld" %>

<%//
// OpenMarket/Xcelerate/AssetType/Template/ContentFormMapDetail
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

String sTmpString = "SELECT Template.id FROM Template,AssetPublication" + " WHERE status!='VO' " + "AND Template.id = AssetPublication.assetid" + " AND Template.subtype IS NULL " + "AND (AssetPublication.pubid = " + ics.GetSSVar("pubid") + " OR AssetPublication.pubid=0)";

%>
<%-- Create list of all typeless template --%>
<ics:sql table="Template" listname="typelesstemplates"
     sql='<%=sTmpString%>'/>
<ics:setvar name="totaltltid" value=""/>
<ics:listloop listname="typelesstemplates">
    <ics:listget listname="typelesstemplates" fieldname="id" output="tltid"/>
    <ics:setvar name="totaltltid" value='<%=ics.GetVar("tltid")+","+ics.GetVar("totaltltid")%>'/>
</ics:listloop>
<%-- trim off trailing comma (,) in totaltltid --%>
<% if (Utilities.goodString(ics.GetVar("totaltltid"))){ %>
	<ics:setvar name="totaltltid" value='<%=ics.GetVar("totaltltid").substring(0, ics.GetVar("totaltltid").length()-1)%>'/>
<% } %>
<%-- Get list of publications to be used in this element--%>
<ics:sql sql="select id, name from Publication" listname="publist" table="Publication"/>

<%--if showSiteTree is true, retrieve list of enabled assettypes for currrent pubid and include javascript function
to select from tree --%>
<ics:if condition='<%="true".equals(ics.GetVar("showSiteTree")) || "ucform".equals(ics.GetVar("cs_environment"))%>'><ics:then>
    <ics:callelement element="OpenMarket/Xcelerate/Actions/AssetMgt/EnableAssetTypePub">
        <ics:argument name="upcommand" value="ListEnabledAssetTypes"/>
        <ics:argument name="list" value="EnabledAssetTypes"/>
        <ics:argument name="pubid" value='<%=ics.GetSSVar("pubid")%>'/>
    </ics:callelement>
    <%String allAssetTypesAsString="";%>
    <ics:listloop listname="EnabledAssetTypes">
        <ics:listget listname="EnabledAssetTypes" fieldname="assettype" output="internalName"/>
        <%allAssetTypesAsString+=ics.GetVar("internalName")+",";%>
    </ics:listloop>
    <script type="text/javascript">
        function selectFromTreeForMap(tempIDField, tempNameField, tempTypeField, counter)
        {
            var typeDropDown = document.forms[0].elements['Template:Mapping:'+ counter +':type'];
            var mappingType = typeDropDown.options[typeDropDown.selectedIndex].value;
            if (mappingType=="tname")
            {
                legalTypes ="Template";
            }
            else {
                <%--using substring to ignore trailing comma(,)--%>
                legalTypes='<%=allAssetTypesAsString.substring(0, allAssetTypesAsString.length()-1)%>';
            }
            //reset temp fields to empty
            document.forms[0].elements['Template:Mapping:'+ counter +':selectedid'].value='';
            document.forms[0].elements['Template:Mapping:'+ counter +':selectedtype'].value='';
            document.forms[0].elements['Template:Mapping:'+ counter +':selectedname'].value='';

            //select asset and place in hidden fields			
			<%if("ucform".equals(ics.GetVar("cs_environment"))) { %>
				 SelectFromTreeTypedTextField('Template:Mapping:'+ counter +':selectedname','Template:Mapping:'+ counter +':selectedid','Template:Mapping:'+ counter +':selectedtype',legalTypes,'_typeAheadElementMapping_'+counter );
			<%}else{%>
				 SelectFromTreeTypedTextField('Template:Mapping:'+ counter +':selectedname','Template:Mapping:'+ counter +':selectedid','Template:Mapping:'+ counter +':selectedtype',legalTypes,'single');
			<%}%>

            //Verify that selectfromtreetypetextfield returned a valid asset.
            if (document.forms[0].elements['Template:Mapping:'+ counter +':selectedid'].value.length==0)
                return false;

            //depending on mappingType, need to determine content for 'Value' field.
            if (mappingType=="tname")
            {
                var totaltltids = [<%=ics.GetVar("totaltltid")%>];
                var selectedtid = document.forms[0].elements['Template:Mapping:'+ counter +':selectedid'].value;
                if (arrayContains(totaltltids,selectedtid))
                {
                    document.forms[0].elements['Template:Mapping:'+ counter +':value'].value =
                    '/'+document.forms[0].elements['Template:Mapping:'+ counter +':selectedname'].value;
                }
                else
                {
                    document.forms[0].elements['Template:Mapping:'+ counter +':value'].value =
                    document.forms[0].elements['Template:Mapping:'+ counter +':selectedname'].value;
                }
            }
            if (mappingType=="assettype")
            {
                document.forms[0].elements['Template:Mapping:'+ counter +':value'].value =
                document.forms[0].elements['Template:Mapping:'+ counter +':selectedtype'].value;
            }
            if (mappingType=="assetname")
            {
                tempValue = document.forms[0].elements['Template:Mapping:'+ counter +':selectedtype'].value+":"+document.forms[0].elements['Template:Mapping:'+ counter +':selectedname'].value;
                document.forms[0].elements['Template:Mapping:'+ counter +':value'].value = tempValue;
            }
            if (mappingType=="asset")
            {
                tempValue = document.forms[0].elements['Template:Mapping:'+ counter +':selectedtype'].value+":"+document.forms[0].elements['Template:Mapping:'+ counter +':selectedid'].value;
                document.forms[0].elements['Template:Mapping:'+ counter +':value'].value = tempValue;
            }
        }
    </script>
</ics:then></ics:if>
<ics:if condition='<%="ucform".equals(ics.GetVar("cs_environment"))%>'><ics:then>
<script type="text/javascript">
var SelectFromTreeTypedTextField = function(where, idwhere, typewhere, validTypes, taWidgetName){
	var obj=document.forms[0].elements[0];
	var nodes= [];
	nodes = dojo.query('div[name='+taWidgetName+']');
	if(nodes.length === 0)
		nodes = dojo.query('input[name='+taWidgetName+']');
	var	typeWidgetIns=dijit.getEnclosingWidget(nodes[0]),
		valueArray=typeWidgetIns.getAllDnDValues(),
		obj = document.forms[0].elements[0];
	var assettype=validTypes;
	var validTypesAndComma = validTypes+",";	
		
	if(valueArray.length > 0){
		var assetDataArray =  valueArray[0];
		if (validTypesAndComma.indexOf(assetDataArray[1]+",") == -1)
		{
			var xlatstr='<xlat:stream key="dvin/UI/Error/Selectionstypeselectionoftypeinvalid" encode="false" escape="true"/>';
			var replacestr=/Variables.assettype/;
			xlatstr = xlatstr.replace(replacestr,assettype);
			var replacestr=/Variables.splitpair/;
			alert(xlatstr.replace(replacestr,assetDataArray[1]));			
			return removeIllegalAssets(typeWidgetIns);
		}
		obj.form.elements[typewhere].value = assetDataArray[1];
		obj.form.elements[idwhere].value = assetDataArray[0];
		if(-1 !== assetDataArray[2].indexOf('('))
			obj.form.elements[where].value = assetDataArray[2].substring(0, assetDataArray[2].indexOf('('));
		else
			obj.form.elements[where].value = assetDataArray[2];
	}
	else{
		obj.form.elements[typewhere].value = '';
		obj.form.elements[idwhere].value = '';
		obj.form.elements[where].value = '';
	}	

};

var removeIllegalAssets =function(typeAheadWidget){
	var nodes = typeAheadWidget._source.getAllNodes();
	for (var i =0; i < nodes.length; i++) {
		parent.fw.ui.dnd.util.destroyItem(typeAheadWidget._source, nodes[i]);
	}
	typeAheadWidget.onChange();
	return;
};
</script>
</ics:then></ics:if>
<script type="text/javascript">
    function arrayContains(arr, value) {
      for (var i = 0; i < arr.length; i++) {
        if (arr[i] == value) return true;
      }
      return false;
    }
    //Only display map values corresponding to selected site of the site drop down.
    function showMappingForSelectedSite()
    {
        var siteArray = new Array();
        <%
        IList ilist = ics.GetList("publist");
        for (int i =1;ilist.moveTo(i);i++)
        {   %>
            siteArray[<%=i-1%>] = <%=ilist.getValue("id")%>;
        <%}%>
        selectbox = document.forms[0].elements['selectedsiteDropDown'];
        selectedsiteid = selectbox.options[selectbox.selectedIndex].value;

        var allTbdy = document.getElementsByTagName('tbody');
        for (var e=0;e<allTbdy.length;e++)
        {
            selectedclass = 'mapentry'+selectedsiteid;
            if (allTbdy[e].className == selectedclass || allTbdy[e].className == "newmapentry")
                allTbdy[e].style.display="";
            else if (allTbdy[e].className.indexOf('mapentry')>=0)
                allTbdy[e].style.display="none";
        }
        //do not show selectorLink if selectedid is not current pubid  (using visibility so it retains spacing)
        var allSpans = document.getElementsByTagName('span');
        for (var e=0;e<allSpans.length;e++)
        {
            if (allSpans[e].className == "selectorLink" && selectedsiteid != <%=ics.GetSSVar("pubid")%>)
            {
                allSpans[e].style.visibility="hidden";
            }
            else if (allSpans[e].className == "selectorLink" && selectedsiteid == <%=ics.GetSSVar("pubid")%>)
            {
                allSpans[e].style.visibility="visible";
            }
        }
        //set selected site for the new entry
        document.forms[0].elements['<%="Template:Mapping:"+(Integer.parseInt(ics.GetVar("total"))-1)+":siteid"%>'].value=selectedsiteid;
    }   
</script>
<ics:if condition='<%="ucform".equals(ics.GetVar("cs_environment"))%>'>
<ics:then>
	<script type="text/javascript">
		function resetValueField(counter)
		{
			var nodes= [];
			nodes = dojo.query('div[name=_typeAheadElementMapping_'+counter+']');
			if(nodes.length === 0)
				nodes = dojo.query('input[name=_typeAheadElementMapping_'+counter+']');
			var	typeWidgetIns = dijit.getEnclosingWidget(nodes[0]),
				dndSource = typeWidgetIns._source,
				dndNodes = dndSource.getAllNodes();
			dojo.forEach(dndNodes, function(dndNode){
				fw.ui.dnd.util.destroyItem(dndSource, dndNode);	
			});
			typeWidgetIns.showDropZone();
			document.forms[0].elements['Template:Mapping:'+counter+':value'].value='';
		}
	</script>
</ics:then>
<ics:else>
<script type="text/javascript">
	 function resetValueField(counter)
    {
        document.forms[0].elements['Template:Mapping:'+counter+':value'].value='';
    }
</script>
</ics:else>
</ics:if>
<!-- user code here -->
<%
	int curCount = 0;
%>

<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer">
	<ics:argument name="SpaceAbove" value="No"/>
</ics:callelement>
<tr>
<td colspan="8">
	<xlat:stream key="dvin/Common/AT/MapDetail"/>
</td>
</tr>

<tr>
    <td colspan="8">
        <xlat:stream key="dvin/Common/AT/MapDetailPicker"/>
    </td>
</tr>

<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
<tr>
	<%--<td colspan="8" class="light-line-color"><img height="1" width="1" src="Variables.cs_imagedir/graphics/common/screen/dotclear.gif" REPLACEALL="Variables.cs_imagedir"/></td>--%>
</tr>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
<%
String stotal = ics.GetVar( "total" );
int total = 0;
try
{
	total = Integer.parseInt( stotal );
}
catch( Exception e )
{
   out.println( "parsing total error." );
}
if ( 1 == total && ics.GetVar( "ContentDetails:Mapping:0:key" ) != null )
{
   for ( int i = 0; true; i ++ )
   {
      if ( ics.GetVar( "ContentDetails:Mapping:" + i + ":key" ) != null )
      {
         total = i + 2;
      }
      else
      {
         break;
      }   
   }
}
%>
	<tr>
	<td colspan="7" style="text-align:left;padding:0;" class="form-label-text"><xlat:stream key="dvin/Common/Site"/>:
	  <SELECT name="selectedsiteDropDown" onchange="return showMappingForSelectedSite()">
		  <ics:listloop listname="publist">
			  <ics:listget listname="publist" fieldname="id" output="ssiteid"/>
			  <ics:listget listname="publist" fieldname="name" output="ssitename"/>
			  <%
				  String selectThisSiteId = ics.GetSSVar("pubid");
				  if (ics.GetVar("selectedsiteDropDown")!=null)
					selectThisSiteId = ics.GetVar("selectedsiteDropDown");
			  %>
			  <OPTION value='<%=ics.GetVar("ssiteid")%>' <%=selectThisSiteId.equals(ics.GetVar("ssiteid"))?"selected":""%>>
				<ics:getvar name="ssitename"/>
			  </OPTION>
		  </ics:listloop>
	  </SELECT></td>
  </tr>
	<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
	<tr>
		<td class="form-label-text" style="text-align:left;padding:0;height:25px;"><xlat:stream key="dvin/Common/AT/Map/key"/></td>
		<td><img style="width:10px;height:1px;" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text" style="text-align:left;padding:0;height:25px;"><xlat:stream key="dvin/Common/AT/Map/type"/></td>
		<td><img style="width:10px;height:1px;" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text" style="text-align:left;padding:0;height:25px;"><xlat:stream key="dvin/Common/AT/Map/value"/></td>
		<td><img style="width:10px;height:1px;" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td><img style="width:10px;height:1px;" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		</tr>
		<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer">
			<ics:argument name="colspan" value="7"/>
		</ics:callelement>

<%	
for ( curCount = 0; curCount < total; curCount ++ )
{
%>
    <tbody class='<%=(curCount < total-1)?"mapentry"+ics.GetVar("ContentDetails:Mapping:" + curCount + ":siteid"):"newmapentry"%>'>
    <tr>
        <td>
		<ics:if condition='<%=curCount < total-1 && ics.GetVar( "ContentDetails:Mapping:" + curCount + ":key" ) != null%>' >
		<ics:then>			
			<ics:setvar name="CKey" value='<%=ics.GetVar( "ContentDetails:Mapping:" + curCount + ":key" ) %>' />			
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":key"%>' VALUE='<string:stream variable="CKey"/>' SIZE="32" MAXLENGTH="32"/>
		</ics:then>
		<ics:else>
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":key"%>' VALUE="" SIZE="32" MAXLENGTH="32"/>
		</ics:else>
		</ics:if>
		</td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td>
<%
		String typename = "Template:Mapping:" + curCount + ":type";
%>		
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:" + curCount + ":type") != null%>' >
		<ics:then>			
			<ics:setvar name="CType" value='<%=ics.GetVar("ContentDetails:Mapping:" + curCount + ":type")%>' />
			<SELECT name="<%= typename %>" onchange="resetValueField(<%=curCount%>)">
<%
			      String type = ics.GetVar( "CType" );
			      %>
			      <OPTION VALUE="tname" <%="tname".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEtname"/>
			      </OPTION>
			      
			      <OPTION VALUE="assettype" <%="assettype".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEassettype"/>
			      </OPTION>
			      		      
			      <OPTION VALUE="assetname" <%="assetname".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEassetname"/>
			      </OPTION>
			      
			      <OPTION VALUE="asset" <%="asset".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEasset"/>
			      </OPTION>
			</SELECT>
		</ics:then>
		<ics:else>
			<SELECT name="<%= typename %>" onchange="resetValueField(<%=curCount%>)">
				<OPTION VALUE="tname" > <xlat:stream key="dvin/Common/AT/Map/TYPEtname"/> </OPTION>
				<OPTION VALUE="assettype" > <xlat:stream key="dvin/Common/AT/Map/TYPEassettype"/> </OPTION>
				<OPTION VALUE="assetname" > <xlat:stream key="dvin/Common/AT/Map/TYPEassetname"/> </OPTION>
				<OPTION VALUE="asset" > <xlat:stream key="dvin/Common/AT/Map/TYPEasset"/> </OPTION>
			</SELECT>
		</ics:else>
		</ics:if>
		</td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<%if(!"ucform".equals(ics.GetVar("cs_environment"))){%>
		<td>
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value") != null%>' >
		<ics:then>			
			<ics:setvar name="CVALUE" value='<%=ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value")%>' />
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE='<string:stream variable="CVALUE"/>' SIZE="32" MAXLENGTH="256"/>
		</ics:then>
		<ics:else>
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE="" SIZE="32" MAXLENGTH="256"/>
		</ics:else>
		</ics:if>
		</td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<%}%>
		<td>
			<%if("ucform".equals(ics.GetVar("cs_environment"))){%>
				<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value") != null%>' >
				<ics:then>			
					<ics:setvar name="CVALUE" value='<%=ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value")%>' />
					<INPUT TYPE="HIDDEN" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE='<string:stream variable="CVALUE"/>' SIZE="32" MAXLENGTH="256"/>
				</ics:then>
				<ics:else>
					<ics:removevar name="CVALUE"/>
					<INPUT TYPE="HIDDEN" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE="" SIZE="32" MAXLENGTH="256"/>
				</ics:else>
				</ics:if>
				<input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedid"%>'/>
                    <input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedtype"%>'/>
                    <input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedname"%>'/>
				<div>
					<div name="_typeAheadElementMapping_<%=curCount%>"> </div>
				</div>
				<script type="text/javascript" language="JavaScript">
					var _selectDnDElementMapping_<%=curCount%> = function(){
                        selectFromTreeForMap('<%= "Template:Mapping:" + curCount + ":selectedname"%>','<%= "Template:Mapping:" + curCount + ":selectedid"%>','<%= "Template:Mapping:" + curCount + ":selectedtype"%>','<%=curCount%>')							
					};
				</script>
				<%
				if(null != ics.GetVar("CVALUE")){
					if("assettype".equals(ics.GetVar("CType"))){
						ics.SetVar("mappedAsset", ics.GetVar("CVALUE")+": "+":"+ics.GetVar("CVALUE")+": ");
					}
					else if("tname".equals(ics.GetVar("CType"))){
						ics.SetVar("mappedAsset", "Template"+": "+":"+ics.GetVar("CVALUE")+": ");
					}
					else if("asset".equals(ics.GetVar("CType"))){
						ics.SetVar("mappedAsset", ics.GetVar("CVALUE").substring(0, ics.GetVar("CVALUE").indexOf(":"))+":" + ics.GetVar("CVALUE").substring(ics.GetVar("CVALUE").indexOf(":")+1, ics.GetVar("CVALUE").length()) + ":"+ics.GetVar("CVALUE").substring(ics.GetVar("CVALUE").indexOf(":")+1, ics.GetVar("CVALUE").length()) + "(" + ics.GetVar("CVALUE").substring(0, ics.GetVar("CVALUE").indexOf(":"))  +"): ");
					}
					else if("assetname".equals(ics.GetVar("CType"))){
						ics.SetVar("mappedAsset", ics.GetVar("CVALUE").substring(0, ics.GetVar("CVALUE").indexOf(":"))+": "+":"+ics.GetVar("CVALUE").substring(ics.GetVar("CVALUE").indexOf(":")+1, ics.GetVar("CVALUE").length()) + "(" + ics.GetVar("CVALUE").substring(0, ics.GetVar("CVALUE").indexOf(":"))  +"): ");
					}
				}
				else{
					ics.SetVar("mappedAsset", "");
				}
				%>
				<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/TypeAheadWidget'>			
					<ics:argument name="parentType" value='["*"]'/>
					<ics:argument name="subTypesForWidget" value='*'/>
					<ics:argument name="subTypesForSearch" value=''/>
					<ics:argument name="multipleVal" value="false"/>
					<ics:argument name="widgetValue" value='<%=ics.GetVar("mappedAsset")%>'/>	
					<ics:argument name="funcToRun" value='<%="_selectDnDElementMapping_" + curCount%>'/>
					<ics:argument name="widgetNode" value='<%="_typeAheadElementMapping_" + curCount%>'/>
					<ics:argument name="typesForSearch" value=''/>	
					<ics:argument name="displaySearchbox" value='false'/>
					<ics:argument name="multiOrderedAttr" value='true'/>				
				</ics:callelement>
			<% } else {%>
                <property:get param="advancedUI.enableAssetForms" inifile="futuretense_xcel.ini" varname="_enableForms"/>
				<%if(null == ics.GetVar("showSiteTree") || "false".equals(ics.GetVar("showSiteTree")) || !"true".equalsIgnoreCase(ics.GetVar("_enableForms"))){
						sTmpString = "Mapping" + curCount;
					%>
					<ics:setvar name="currentUniqueID" value="<%=sTmpString%>"/>
                    <!-- There are two pick asset popup URL, one for Template, one for enabled AssetTypes. URL depends on type dropdown-->
                    <xlat:lookup key="dvin/Common/AT/Map/value" varname="fieldname"/>
                    <satellite:link pagename="OpenMarket/Xcelerate/Actions/PickAssetPopup" assembler="query" outstring='<%="url_"+ics.GetVar("currentUniqueID")+"Template"%>'>
                        <satellite:argument name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
                        <satellite:argument name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
                        <satellite:argument name="cs_PickAssetType" value="Template"/>
                        <satellite:argument name="cs_SelectionStyle" value="single"/>
                        <satellite:argument name="cs_CallbackSuffix" value='<%=ics.GetVar("currentUniqueID")%>'/>
                        <satellite:argument name="cs_FieldName" value='<%=ics.GetVar("fieldname")%>'/>
                    </satellite:link>

                    <satellite:link pagename="OpenMarket/Xcelerate/Actions/PickAssetPopup" assembler="query" outstring='<%="url_"+ics.GetVar("currentUniqueID")%>'>
                        <satellite:argument name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
                        <satellite:argument name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
                        <satellite:argument name="cs_SelectionStyle" value="single"/>
                        <satellite:argument name="cs_CallbackSuffix" value='<%=ics.GetVar("currentUniqueID")%>'/>
                        <satellite:argument name="cs_FieldName" value='<%=ics.GetVar("fieldname")%>'/>
                    </satellite:link>

                    <script type="text/javascript">
                        function <%="PickAssetCallback_"+ics.GetVar("currentUniqueID")+"(SelectedAssets)"%>
                        {
                            var AssetInfo = SelectedAssets.split(":");
                            var typeDropDown = document.forms[0].elements['<%="Template:Mapping:" + curCount + ":type"%>'];
                            var mappingType = typeDropDown.options[typeDropDown.selectedIndex].value;
                            if (mappingType=="tname")
                            {
                                var totaltltids = [<%=ics.GetVar("totaltltid")%>];
                                if (arrayContains(totaltltids,AssetInfo[1]))
                                {
                                    document.forms[0].elements['<%= "Template:Mapping:" + curCount + ":value"%>'].value = '/'+AssetInfo[2];
                                }
                                else
                                {
                                    document.forms[0].elements['<%= "Template:Mapping:" + curCount + ":value"%>'].value = AssetInfo[2];
                                }
                            }
                            if (mappingType=="assettype")
                            {
                                document.forms[0].elements['<%= "Template:Mapping:" + curCount + ":value"%>'].value = AssetInfo[0];
                            }
                            if (mappingType=="assetname")
                            {
                                document.forms[0].elements['<%= "Template:Mapping:" + curCount + ":value"%>'].value = AssetInfo[0]+":"+AssetInfo[2];
                            }
                            if (mappingType=="asset")
                            {
                                document.forms[0].elements['<%= "Template:Mapping:" + curCount + ":value"%>'].value = AssetInfo[0]+":"+AssetInfo[1];
                            }
                        }
                        function <%="PickAssetPopup_"+ics.GetVar("currentUniqueID")+"()"%>
                        {
                            var typeDropDown = document.forms[0].elements['<%="Template:Mapping:" + curCount + ":type"%>'];
                            var mappingType = typeDropDown.options[typeDropDown.selectedIndex].value;
                            if (mappingType=="tname")
                            {
                                OpenPickAssetPopup('<%=ics.GetVar("url_"+ics.GetVar("currentUniqueID")+"Template")%>', GetBannerHistory());
                            }
                            else
                            {
                                OpenPickAssetPopup('<%=ics.GetVar("url_"+ics.GetVar("currentUniqueID"))%>', GetBannerHistory());
                            }
                        }
                    </script>
                    <xlat:lookup key="dvin/AT/Common/SelectFromPopup" varname="_XLAT_"/>
                    <span class="selectorLink"><a href="javascript:void(0)" onclick='<%="PickAssetPopup_"+ics.GetVar("currentUniqueID")+"();return false;"%>' target="_blank"><ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TextButton"><ics:argument name="buttonkey" value="UI/Forms/Browse"/></ics:callelement></a></span>
                 <%} else {%>
                    <input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedid"%>'/>
                    <input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedtype"%>'/>
                    <input type="hidden" name='<%= "Template:Mapping:" + curCount + ":selectedname"%>'/>

                    <!-- Create a "pick-from-tree" link that fills in the text field we need to be filled in -->
                    <xlat:lookup key="dvin/AT/Common/SelectFromTree" varname="_XLAT_"/>
                    <span class="selectorLink"><A HREF="javascript:void(0)" onclick="selectFromTreeForMap('<%= "Template:Mapping:" + curCount + ":selectedname"%>','<%= "Template:Mapping:" + curCount + ":selectedid"%>','<%= "Template:Mapping:" + curCount + ":selectedtype"%>','<%=curCount%>')">
                    <ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TextButton"><ics:argument name="buttonkey" value="UI/Forms/AddSelectedItems"/></ics:callelement>
                    </A></span>
                 <%}%>
			<%}%> 
        </td>
		
<%
		String CSiteid = "Template:Mapping:" + curCount + ":siteid";
%>		
		
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:" + curCount + ":siteid") != null%>' >
		<ics:then>			
			<ics:setvar name="CSiteid" value='<%=ics.GetVar("ContentDetails:Mapping:" + curCount + ":siteid")%>' />
			<input type="hidden" name="<%=CSiteid%>" value='<%=ics.GetVar("CSiteid")%>'/>
        </ics:then>
		<ics:else>
            <input type="hidden" name="<%=CSiteid%>" value=""/>
		</ics:else>
		</ics:if>

    
    </tr>
    <ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer">
			<ics:argument name="colspan" value="7"/>
		</ics:callelement>
    </tbody>

<%
}
%>
<script type="text/javascript">showMappingForSelectedSite();</script>
<INPUT TYPE="hidden" NAME="Template:Mapping:Total" Value='<%=total%>' />
</cs:ftcs>