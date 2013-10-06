
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%
//
// OpenMarket/Xcelerate/AssetType/Page/ContentForm.jsp
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="com.openmarket.gator.interfaces.*"%>
<cs:ftcs>
<div dojoType="dijit.layout.BorderContainer" class="bordercontainer">
<input type="hidden" name="FlexTmplsNextStep" value=""/>
<input type="hidden" name="MultiAttrVals" value=""/>

<ics:setvar name="templatetype" value="PageDefinition" />
<ics:if condition='<%=!(ics.GetVar("ContentDetails:createddate").equals(ics.GetVar("empty")))%>'>
<ics:then>
	<ics:setvar name="TemplateChosen" value='<%=ics.GetVar("ContentDetails:flextemplateid")  %>' />
	<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentForm1"/>
</ics:then>
<ics:else>
	<asset:getsubtype name='theCurrentAsset' output='subtypeName'/>
	<ics:if condition='<%=ics.GetVar("subtypeName") != null%>'>
	<ics:then>
		<ics:clearerrno />
		<asset:list type='<%=ics.GetVar("templatetype")%>'
			field1='name'
			value1='<%=ics.GetVar("subtypeName")%>'
			list='lCurrentSubtype'/>
		<ics:setvar name='TemplateChosen' value='<%=ics.GetList("lCurrentSubtype").getValue("id")%>'/>
	</ics:then>
	</ics:if>
	<ics:if condition='<%=ics.GetVar("TemplateChosen") == null%>'>
	<ics:then>
		<asset:list type='<%=ics.GetVar("templatetype")%>' excludevoided='true' pubid='<%=ics.GetSSVar("pubid")%>' list='lAllDefs'/>
		<ics:if condition='<%=ics.GetList("lAllDefs")!=null && ics.GetList("lAllDefs").numRows()==1%>'>
		<ics:then>
			<ics:setvar name='TemplateChosen' value='<%=ics.GetList("lAllDefs").getValue("id")%>'/>
			<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentForm1"/>
		</ics:then>
		<ics:else>
			<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/AssocTmpls"/>
		</ics:else>
		</ics:if>
	</ics:then>
	<ics:else>
		<ics:callelement element="OpenMarket/Xcelerate/AssetType/Page/ContentForm1"/>
	</ics:else>
	</ics:if>
</ics:else>
</ics:if>
</div>
</cs:ftcs>