<% 
//
// OpenMarket/Xcelerate/AssetType/Page/GetChildrenReferences
//
// Now that we're allowing Page attributes, we also need to get the 
// children that are related through attributes of data type defined as 
// asset references.
//
// INPUT
//
// OUTPUT
//
%>
<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"%>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"%>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"%>
<%@ taglib prefix="render" uri="futuretense_cs/render.tld"%>
<%@ taglib prefix="siteplan" uri="futuretense_cs/siteplan.tld"%>

<%@ taglib prefix="assettype" uri="futuretense_cs/assettype.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="hash" uri="futuretense_cs/hash.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="property" uri="futuretense_cs/property.tld" %>
<%@ taglib prefix="dateformat" uri="futuretense_cs/dateformat.tld" %>
<%@ taglib prefix="listobject" uri="futuretense_cs/listobject.tld" %>

<%@ page import="COM.FutureTense.Interfaces.*"%>
<%@ page import="com.fatwire.system.*"%>
<%@ page import="COM.FutureTense.Interfaces.IList,
com.openmarket.assetframework.interfaces.AssetTypeManagerFactory,
com.openmarket.assetframework.interfaces.IAssetTypeManager,
com.openmarket.gator.interfaces.IAttributableAssetInstance" %>

<%@ page import="java.util.*"%>
<%@page import="com.fatwire.system.Session"%>
<%@page import="com.fatwire.system.SessionFactory"%>
<%@page import="com.fatwire.services.ServicesManager"%>
<%@page import="com.fatwire.services.ApprovalService"%>
<%@page import="com.fatwire.services.AssetService"%>		 		
<%@ page import="com.openmarket.gator.page.*"%>
<%@ page import="com.fatwire.assetapi.data.*"%>
<%@ page import="com.fatwire.assetapi.query.Query"%>
<%@ page import="com.fatwire.assetapi.query.SimpleQuery"%>
<%@ page import="com.openmarket.assetframework.complexasset.ComplexAsset"%>
<%@ page import="com.openmarket.xcelerate.asset.*"%> 

<%@page import="com.fatwire.services.util.AssetUtil"%>
<%@page import="com.fatwire.assetapi.data.AssetId"%>
<%@page import="com.openmarket.xcelerate.interfaces.IAsset"%>
<%@page import="com.openmarket.xcelerate.asset.AssetList"%>
<%@page import="com.fatwire.services.beans.asset.AssetBean"%>

<cs:ftcs>

<%-- 
      load the page, to get the extended Page Definition 
      asset children  
--%>

<asset:load name="currentPage" type="Page"  option="editable" objectid='<%=ics.GetVar("cid")%>'/>

<asset:get name="currentPage"  field="name" output="assetname"/>
<asset:get name="currentPage"  field="description" output="description"/>
<asset:get name="currentPage"  field="flextemplateid" output="flextemplateid"/>

<!--  Create the list which will be populated with attribute asset references 
      of children  -->
<listobject:create name='RefChildrenList' columns='type,id,name'/>

<%

// Get Page id 
String id = ics.GetVar("cid") ; 

String name = ics.GetVar("assetname") ; 
String desc = ics.GetVar("description") ; 
String templateid = ics.GetVar("flextemplateid") ; 



// Create a Page Manager Instance  
PageManager pageMgr = new PageManager() ; 
IAssetTypeManager atm = AssetTypeManagerFactory.getATM(ics);

// Set up page just loaded object references... 
pageMgr.setupReferences(ics,atm,"currentPage" ); 

// Get the Page Object Reference 
com.openmarket.gator.page.Page node = (com.openmarket.gator.page.Page) ics.GetObj("currentPage");

// Get All Children Asset References 
List<AssetId> children = pageMgr.getChildrenReferences(node) ; 

// Get the childrens asset names 
Session ses = SessionFactory.getSession();
ServicesManager servicesManager = (ServicesManager) ses.getManager(ServicesManager.class.getName());

AssetService assetService = servicesManager.getAssetService();

List<String> fieldNames = Arrays.asList(IAsset.NAME);

List<String> list =  new ArrayList<String>();  
Map<String, AssetId> m = new HashMap<String, AssetId>();


for(AssetId child : children ) {
	AssetData assetData = assetService.read(child, fieldNames);
	Object nameData = AssetUtil.getAttribute(assetData, IAsset.NAME);
	name = nameData == null ? "" : String.valueOf(nameData);  
	list.add(name) ; 
	m.put(name,child) ; 	
}

// sort related children by name in alphabetical order 
Collections.sort(list);
for ( String s : list ) {
	AssetId  ch = m.get(s) ; 	
	%> 
	 <listobject:addrow name='RefChildrenList' >                              
       <listobject:argument name='type' value='<%=ch.getType()%>'/>
		<listobject:argument name='id' value='<%=String.valueOf(ch.getId())%>'/>
		<listobject:argument name='name' value='<%=s%>'/>								
    </listobject:addrow>
	<% 
}
%>
	
<%  
if ( children.size()  > 0 ) {
%>
<listobject:tolist name='RefChildrenList' listvarname='ChildrenReferences'/>
<% 
}
%>
</cs:ftcs>