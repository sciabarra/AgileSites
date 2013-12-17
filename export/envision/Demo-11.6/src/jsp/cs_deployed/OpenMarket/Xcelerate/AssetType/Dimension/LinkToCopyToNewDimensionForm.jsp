<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="property" uri="futuretense_cs/property.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/Dimension/LinkToCopyToNewDimensionForm
//
// INPUT
//
// OUTPUT
//%>
<cs:ftcs>

<%-- Record dependencies for the Template --%>
<ics:if condition='<%=ics.GetVar("tid")!=null%>'><ics:then><render:logdep cid='<%=ics.GetVar("tid")%>' c="Template"/></ics:then></ics:if>

<property:get param="advancedUI.enableAssetForms" inifile="futuretense_xcel.ini" varname="_enableForms_"/>

<satellite:link assembler="query" pagename="OpenMarket/Xcelerate/AssetType/Dimension/ShowCopyToNewDimensionForm" outstring="getCopyURL">
    <satellite:argument name="cs_environment" value='<%=ics.GetVar("cs_environment")%>'/>
	<satellite:argument name="cs_formmode" value='<%=ics.GetVar("cs_formmode")%>'/>
	<satellite:argument name="AssetType" value='<%=ics.GetVar("AssetType")%>'/>
	<satellite:argument name="id" value='<%=ics.GetVar("id")%>'/>
	<satellite:argument name="cs_imagedir" value='<%=ics.GetVar("cs_imagedir")%>'/>
	<satellite:argument name="contentPane_ID" value='<%=ics.GetVar("id") + "_copyDimensionFormPane"%>'/>
	<satellite:argument name="_enableForms" value='<%=ics.GetVar("_enableForms_")%>'/>
	<satellite:parameter name="documentId" value='<%=ics.GetVar("docId")%>'/>
</satellite:link>

<script type="text/javascript">

	dojo.addOnLoad(function(){	
		var cPane = dijit.byId('<%= ics.GetVar("id") + "_copyDimensionFormPane" %>');
		cPane.set('href', '<%= ics.GetVar("getCopyURL") %>');
		dojo.connect(cPane, 'onLoad', function(){
			var loadingScreen = dojo.byId('<%= ics.GetVar("id") + "_loadingWrapper_dimension" %>');
			if (loadingScreen)
				dojo.style(loadingScreen, 'display', 'none');
		});
		dojo.connect(cPane, 'onUnload', function(){
			var loadingScreen = dojo.byId('<%= ics.GetVar("id") + "_loadingWrapper_dimension" %>');
			if (loadingScreen)
				dojo.style(loadingScreen, 'display', '');
		});
	});
		
</script>


<div>
	<div id='<%= ics.GetVar("id") + "_loadingWrapper_dimension" %>' valign="middle" align="center" colspan="3" style="border: 1px solid #CCC;padding:4px;">
		<img src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/icon/wait_ax.gif'/>
		<br/>
		<br/>
		<b>
		<span><xlat:stream key="dvin/UI/Loading"/></span>
		<img src='<%=ics.GetVar("cs_imagedir")%>/graphics/common/icon/short_Progress.gif'/>
		</b>
	</div>
	<div id='<%= ics.GetVar("id") + "_copyDimensionFormPane" %>' refreshOnShow="true" dojoType="dojox.layout.ContentPane">	
		<script type="dojo/method"></script>
	</div>	
</div>
</cs:ftcs>