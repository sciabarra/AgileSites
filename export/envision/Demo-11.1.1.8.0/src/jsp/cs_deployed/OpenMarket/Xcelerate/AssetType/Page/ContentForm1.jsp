<%@ page contentType="text/html; charset=UTF-8"
		import="com.openmarket.gator.interfaces.*,
				com.openmarket.assetframework.interfaces.*,
				com.openmarket.xcelerate.interfaces.*,
                COM.FutureTense.Interfaces.*,
                com.openmarket.xcelerate.util.ConverterUtils"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="assettype" uri="futuretense_cs/assettype.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="hash" uri="futuretense_cs/hash.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="property" uri="futuretense_cs/property.tld" %>
<%@ taglib prefix="dateformat" uri="futuretense_cs/dateformat.tld" %>

<%
//
//
// OpenMarket/Xcelerate/AssetType/Page/ContentForm1
//
// INPUT
//
// OUTPUT
%>
<cs:ftcs>

<%
try
{
	String assetType = ics.GetVar("AssetType") ;
	String updateType = ics.GetVar("updatetype") ;
	String templateType = ics.GetVar("templatetype") ;
	String templateChosen =   ics.GetVar("TemplateChosen");%>
	<assettype:load name='type' type='<%=ics.GetVar("AssetType")%>'/>
	<assettype:scatter name='type' prefix='AssetTypeObj'/>
	<ics:setvar name='RateName' value='<%=ics.GetVar("empty")%>'/>
	<ics:setvar name='ConfidenceName' value='<%=ics.GetVar("empty")%>'/>
	<input type="hidden" name="TreeSelect" value=""/>
	<input type="hidden" name="upcommand" value="submit"/><%
	if (Utilities.goodString(ics.GetVar("TemplateChosen"))) {%>
		<input type="hidden" name="TemplateChosen" value='<%=ics.GetVar("TemplateChosen")%>'/><%
	} else {%>
		<input type="hidden" name="TemplateChosen" value=""/><%
	}%>
	
	<input type="hidden" name="doSubmit" value="yes"/>
	<input type="hidden" name="FieldsOnForm" value="name,description,flextemplateid,externalid,ruleset,Dimension,Dimension-parent,category"/>
	<input type="hidden" name="isReposted" value="true"/>
	<ics:setvar name="attributetype" value="PageAttribute" />
	<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ParseTreeSelect"/>
	<!-- We must always gather and rescatter, if this is a repost --><%
	if (ics.GetVar("isReposted") != null && ics.GetVar("isReposted").equals("true"))
	{%>
		<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/AssetGather">
			<ics:argument name="GetOrSet" value="set"/>
			<ics:argument name="flextype" value="asset"/>
		</ics:callelement>
		<asset:scatter name="theCurrentAsset" prefix="ContentDetails"/><%
	}%>
	<ics:if condition='<%=ics.GetVar("TreeSelect")!=null%>'>
	<ics:then>
		<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ShowTreePickError"/>
	</ics:then>
	</ics:if>

	<ics:if condition='<%=!(ics.GetVar("ContentDetails:templateid")==null || ics.GetVar("ContentDetails:templateid").equals(ics.GetVar("empty")))%>'>
	<ics:then>
		<ics:setvar name='TemplateChosen' value='<%=ics.GetVar("ContentDetails:templateid")%>'/>
	</ics:then>
	<ics:else>
		<asset:set name='theCurrentAsset' field='templateid' value='<%=ics.GetVar("TemplateChosen")%>'/>
		<ics:if condition='<%=ics.GetVar("flexassets:name")!=null%>'>
		<ics:then>
			<ics:setvar name='ContentDetails:name' value='<%=ics.GetVar("flexassets:name")%>'/>
		</ics:then>
		</ics:if>
	</ics:else>
	</ics:if>
	<%-- Fix for PR#21131 Moved from above--%>
	<ics:callelement element='OpenMarket/Xcelerate/AssetType/Page/ContentForm1JavaScript'>
	  <ics:argument name='flextype' value='asset'/>
	</ics:callelement>

	<assettype:load name='TemplateTypeMgr' type='<%=ics.GetVar("templatetype")%>'/>
	<assettype:scatter name="TemplateTypeMgr" prefix="DefTypeObj"/>
	<asset:load type='<%=ics.GetVar("templatetype")%>' name='ProdTmplinst' objectid='<%=ics.GetVar("TemplateChosen")%>'/>
	<asset:get name='ProdTmplinst' field='name' output='TypeName'/>
	<asset:get name="ProdTmplinst" field="description" output="TypeDesc" />
	<ics:ifempty variable="TypeDesc">
	<ics:then>
		<ics:setvar name="TypeDesc" value='<%=ics.GetVar("TypeName") %>' />
	</ics:then>
	</ics:ifempty>
	<ics:ifempty variable="cs_title">
	<ics:then>
		<ics:setvar name="cs_title" value='<%=ics.GetVar("TypeDesc") %>' />
	</ics:then>
	</ics:ifempty>
	<% if(!"ucform".equals(ics.GetVar("cs_environment"))){%>
		<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/SaveAndCancel"/>
	<%}%>
	<xlat:lookup key="UI/Forms/Content" varname="tabContent"/>
	<xlat:lookup key="UI/Forms/Metadata" varname="tabMetadata"/>
	<div dojoType="dijit.layout.ContentPane" region="center">
	<div dojoType="dijit.layout.TabContainer" class="formstabs formsTabContainer" style="width:100%;height:100%">
	<ics:callelement element="OpenMarket/Xcelerate/Util/RetainSelectedTab">
		<ics:argument name="tabContent" value='<%= ics.GetVar("tabContent") %>'/>
		<ics:argument name="elementType" value='JSP'/>
	</ics:callelement>	
	<div dojoType="dijit.layout.ContentPane" title='<%=ics.GetVar("tabContent")%>' style="height: 100%;"  selected="true">
        <table class="width70BottomExMargin" border="0" cellpadding="0" cellspacing="0">
			
		<!-- page title w/ asset name, if any -->
		<!--assettype:list list="ThisAsset" arg1="<%=ics.GetVar("AssetType")%>"/-->
	  <tr>
		  <ics:if condition='<%=!(ics.GetVar("updatetype")!=null && ics.GetVar("updatetype").equals("setformdefaults"))%>'>
		  <ics:then>
			  <td><span class="title-text"><string:stream variable="cs_title"/>:</span>&nbsp;<span class="title-value-text"><string:stream variable="ContentDetails:name"/></span></td>
		  </ics:then>
		  <ics:else>
			  <ics:ifempty variable="ContentDetails:name">
			  <ics:then>
				  <td><span class="title-text"><string:stream variable="cs_title"/>:</span></td>
			  </ics:then>
			  <ics:else>
				  <td><span class="title-text"><string:stream variable="cs_title"/>:</span>&nbsp;<span class="title-value-text"><string:stream variable="ContentDetails:name"/></span></td>
			  </ics:else>
			  </ics:ifempty>
		  </ics:else>
		  </ics:if>
	  </tr>

	  <ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TitleBar">
		  <ics:argument name="SpaceBelow" value="No"/>
	  </ics:callelement><tr><td>
	  <table>
	  <% 
		Hashtable enabledFields = (Hashtable)ics.GetObj("enabledFields");
		if (enabledFields == null || enabledFields.containsKey("name")) {%>
			<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
			<tr>
				<td class="form-label-text"><span class="alert-color">*</span><xlat:stream key="dvin/AT/Common/Name"/>:</td>
				<td><img height="1" width="5" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
				<property:get param="xcelerate.asset.sizeofnamefield" inifile="futuretense_xcel.ini" varname="sizeofnamefield"/>
				<td class="form-inset">
					<ics:callelement element="OpenMarket/Gator/AttributeTypes/TextBox">
						<ics:argument name="inputName" value="flexassets:name"/>
						<ics:argument name="inputValue" value='<%=ics.GetVar("ContentDetails:name")%>'/>
						<ics:argument name="inputSize" value='32'/>
						<ics:argument name="inputMaxlength" value='<%=ics.GetVar("sizeofnamefield")%>'/>
						<ics:argument name="applyDefaultFormStyle" value='<%=ics.GetVar("defaultFormStyle")%>' />
					</ics:callelement>
				</td>
			</tr><% }
			
			if (enabledFields == null || enabledFields.containsKey("template")) {%>
			<xlat:lookup key="dvin/UI/Template" varname="label"/>
			<ics:callelement element="OpenMarket/Xcelerate/Actions/Util/ShowTemplatesIfAny">
				<ics:argument name="Prefix" value="flexassets"/>
				<ics:argument name="showDummy" value="true"/>
				<ics:argument name="label" value='<%=ics.GetVar("label")+":"%>'/>
				<ics:argument name="subtype" value='<%=ics.GetVar("TypeName") %>' />
			</ics:callelement><%
			}%>
			<ics:if condition='<%=ics.GetVar("ContentDetails:flextemplateid").equals("")%>'>
	        <ics:then>
			<ics:setvar name='flextemplateid' value='<%=ics.GetVar("TemplateChosen")%>'/>
	        </ics:then>
	        <ics:else>
			<ics:setvar name='flextemplateid' value='<%=ics.GetVar("ContentDetails:flextemplateid")%>'/>
	       </ics:else>
	       </ics:if>
	       <ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/AssocAttr">
				<ics:argument name="tagtype"  value="flextemplates"/>
			</ics:callelement>
			<%
			// Handle asset associations
			IAssocNamedManager we = AssocNamedManagerFactory.make(ics);
			ics.RegisterList("associations", we.getList(ics.GetVar("AssetType"),null,null,null,null,null,null));%>
			
			<ics:if condition='<%=ics.GetList("associations").hasData()%>'>
			<ics:then>
				<ics:callelement element="OpenMarket/Xcelerate/Actions/AssetMgt/AssetChildrenFormTypeAhead"/>
			</ics:then>
			</ics:if>
			
		</table> </td></tr></table> 
	</div>
	<div dojoType="dijit.layout.ContentPane" title='<%=ics.GetVar("tabMetadata")%>' style="height: 100%;">
	<table border="0" cellpadding="0" cellspacing="0" class="width70BottomExMargin">
	<!-- page title w/ asset name, if any -->
	<tr>
		<ics:if condition='<%=!(ics.GetVar("updatetype")!=null && ics.GetVar("updatetype").equals("setformdefaults"))%>'>
		<ics:then>
			<td><span class="title-text"><string:stream variable="cs_title"/>: <string:stream variable="ContentDetails:name"/></span></td>
		</ics:then>
		<ics:else>
			<ics:ifempty variable="ContentDetails:name">
			<ics:then>
				<td><span class="title-text"><string:stream variable="cs_title"/>:</span></td>
			</ics:then>
			<ics:else>
				<td><span class="title-text"><string:stream variable="cs_title"/>:</span>&nbsp;<span class="title-value-text"><string:stream variable="ContentDetails:name"/></span></td>
			</ics:else>
			</ics:ifempty>
		</ics:else>
		</ics:if>
	</tr>

	<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/TitleBar">
		<ics:argument name="SpaceBelow" value="No"/>
	</ics:callelement>

	<tr>
		<td>
		<table>
			<!-- attribute inspection form -->
			<ics:setvar name='storeID'  value='<%=ics.GetVar("id")%>'/>
			<ics:if condition='<%=!(ics.GetVar("updatetype")!=null && ics.GetVar("updatetype").equals("setformdefaults"))%>'>
			<ics:then>
				<ics:setvar name="NewSection" value="true"/>
			</ics:then>
			</ics:if><%			

			if (enabledFields == null || enabledFields.containsKey("description")){%>
			<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
			<tr>
				<td valign="top" class="form-label-text"><span class="alert-color"></span><xlat:stream key="dvin/AT/Common/Description"/>:</td>
				<td></td>
				<td class="form-inset">
					<ics:callelement element="OpenMarket/Gator/AttributeTypes/TextBox">
						<ics:argument name="inputName" value="flexassets:description"/>
						<ics:argument name="inputValue" value='<%=ics.GetVar("ContentDetails:description")%>'/>
						<ics:argument name="inputSize" value='32'/>
						<ics:argument name="inputMaxlength" value='128'/>
						<ics:argument name="applyDefaultFormStyle" value='<%=ics.GetVar("defaultFormStyle")%>' />
					</ics:callelement>
				</td>
			</tr><%
			}
			
			if (enabledFields == null || enabledFields.containsKey("id")) {%>
				<ics:if condition='<%=!(ics.GetVar("updatetype").equals("setformdefaults"))%>'>
				<ics:then>
					<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
					<tr>
						<td class="form-label-text"><xlat:stream key="dvin/AT/Common/ID"/>:</td>
						<td></td>
						<td class="form-inset"><span class="disabledEditText"><string:stream variable="id"/></span></td>
					</tr>
				</ics:then>
				</ics:if><%
			}
		
			if (enabledFields == null || enabledFields.containsKey("filename")) {%>
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
				<tr>
					<td class="form-label-text"><xlat:stream key="dvin/Common/Filename"/>:</td>
					<td></td>
					<td class="form-inset">
						<ics:callelement element="OpenMarket/Gator/AttributeTypes/TextBox">
							<ics:argument name="inputName" value="flexassets:filename"/>
							<ics:argument name="inputValue" value='<%=ics.GetVar("ContentDetails:filename")%>'/>
							<ics:argument name="inputSize" value='32'/>
							<ics:argument name="inputMaxlength" value='64'/>
							<ics:argument name="applyDefaultFormStyle" value='<%=ics.GetVar("defaultFormStyle")%>' />
						</ics:callelement>
					</td>
				</tr><%
			}
			
			if (enabledFields == null || enabledFields.containsKey("path")) {%>
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
				<tr>
					<td class="form-label-text"><xlat:stream key="dvin/Common/Path"/>:</td>
					<td></td>
					<td class="form-inset">
						<ics:callelement element="OpenMarket/Gator/AttributeTypes/TextBox">
							<ics:argument name="inputName" value="flexassets:path"/>
							<ics:argument name="inputValue" value='<%=ics.GetVar("ContentDetails:path")%>'/>
							<ics:argument name="inputSize" value='32'/>
							<ics:argument name="inputMaxlength" value='255'/>
							<ics:argument name="applyDefaultFormStyle" value='<%=ics.GetVar("defaultFormStyle")%>' />
						</ics:callelement>
					</td>
				</tr><%
			}%>

			<!--Call start date and end date for site preview-->
			<ics:callelement element="OpenMarket/Xcelerate/UIFramework/form/AssetTypeStartEndDate">
				<ics:argument name="startDateFieldName" value="flexassets:startdate"/>
				<ics:argument name="endDateFieldName"  value="flexassets:enddate"/>
			</ics:callelement>
			<!--End site preview dates -->
			<%
			ics.SetVar("dimFormPrefix", "flexassets");
			ics.SetVar("dimVarPrefix", "ContentDetails");
			ics.CallElement("OpenMarket/Xcelerate/AssetType/Dimension/ShowDimensionForm", null);			
			ics.SetVar("NewSection", "true");
			%>
			<ics:setvar name='assettype' value='Page'/>
			<ics:selectto table="Category" listname="Category" where="assettype"/>
			<ics:if condition='<%=ics.GetList("Category").hasData()%>'>
			<ics:then>
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
				<tr>
					<td class="form-label-text"><xlat:stream key="dvin/UI/Admin/Category"/>:</td>
					<td></td>
					<td class="form-inset">
						<ics:callelement element="OpenMarket/Xcelerate/Actions/Util/MakeCategoryList">
							<ics:argument name="Prefix" value="flexassets"/>
						</ics:callelement>
					</td>
				</tr>
			</ics:then>
			</ics:if>
			
			<%if (enabledFields == null || enabledFields.containsKey("templatetype")) {%>
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
				<tr>
					<td class="form-label-text"><string:stream variable="DefTypeObj:description"/>:</td>
					<td></td>
					<td class="form-inset"><xlat:lookup key="dvin/Common/InspectThisItem" varname="InspectThisItem" escape="true"/>
						<ics:callelement element="OpenMarket/Xcelerate/Util/GenerateLink">
							<ics:argument name="assettype" value='<%=ics.GetVar("templatetype")%>'/>
							<ics:argument name="assetid" value='<%=ics.GetVar("TemplateChosen")%>'/>
							<ics:argument name="varname" value="urlInspectItem"/>
							<ics:argument name="function" value="inspect"/>
						</ics:callelement>
						<a href="<%=ics.GetVar("urlInspectItem")%>" onmouseover="window.status='<%=ics.GetVar("InspectThisItem")%>';return true;" onmouseout="window.status='';return true" ><span class="disabledEditText"><string:stream variable="TypeName"/></span></a>
						<input type="hidden" name="flexassets:subtype" value='<string:stream variable="TypeName"/>' />
					</td>
				</tr><%
			}%>
			

			
			<!--  Associating   Defintion -->
	       <ics:if condition='<%=ics.GetVar("ContentDetails:flextemplateid").equals("")%>'>
	        <ics:then>
		    <INPUT type="hidden" name="flexassets:flextemplateid" value="<%=ics.GetVar("TemplateChosen")%>"/>
	        </ics:then>
	        <ics:else>
		   <INPUT type="hidden" name="flexassets:flextemplateid" value="<%=ics.GetVar("ContentDetails:flextemplateid")%>"/>
	       </ics:else>
	       </ics:if>
		
			<ics:setvar name='flextemplateid' value='<%=ics.GetVar("TemplateChosen")%>'/>
			<ics:if condition='<%=!(ics.GetVar("updatetype").equals("setformdefaults"))%>'>
			<ics:then>
				<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer"/>
				<tr>
					<td class="form-label-text"><xlat:stream key="dvin/AT/Common/Created"/>:</td>
					<td></td>
					<dateformat:getdatetime name='_FormatDate_' value='<%=ics.GetVar("ContentDetails:createddate")%>' valuetype='jdbcdate'  varname='ContentDetails:createddate'/>
					<td class="form-inset"><span class="disabledEditText"><xlat:stream key="dvin/FlexibleAssets/FlexAssets/Bycreatedby"/></span></td>
				</tr>
				
				<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
				<tr>
 					<td class="form-label-text"><xlat:stream key="dvin/AT/Common/Modified"/>:</td>
					<td></td>
					<dateformat:getdatetime name='_FormatDate_' value='<%=ics.GetVar("ContentDetails:updateddate")%>' valuetype='jdbcdate'  varname='ContentDetails:updateddate'/>
					<td class="form-inset"><span class="disabledEditText"><xlat:stream key="dvin/FlexibleAssets/FlexAssets/byUpdatedby"/> </span></td>
				</tr>
			</ics:then>
			</ics:if>
			</table>
			</td>
			</tr>
			</table>
			</div>
</div>
</div>
<%
}
catch (Exception e)
{
	e.printStackTrace();
}
%>
</cs:ftcs>
