<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="user" uri="futuretense_cs/user.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="publication" uri="futuretense_cs/publication.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><%@ page import="org.apache.log4j.*,
                   org.apache.log4j.net.*,
                   java.util.*,
                   java.io.*,
                   wcs.core.*"
%><cs:ftcs><% 
   boolean verified = false; 
   File outputDir = null;
   File curDir = null;
   File curFile = null;
   if(ics.GetVar("user")!=null && ics.GetVar("pass")!=null) { 
%><user:login 
  username='<%= ics.GetVar("user") %>' 
  password='<%= ics.GetVar("pass") %>'
/><% verified = ics.GetErrno() == 0;  
 } 
 if(ics.GetVar("site")!=null) { 
%><publication:load name="pub" 
  field="name" 
  value='<%= ics.GetVar("site") %>'
/><% } 
  if(verified && ics.GetObj("pub")!=null ) { 
%><publication:get name="pub"  
  field="id" 
  output="pubid"
/><ics:sql  listname="assets" 
 table="AssetPublication" 
 sql='<%= "select assettype, assetid from AssetPublication"
          +" where pubid="+ics.GetVar("pubid") %>'
/><% 
  outputDir = new File(ics.GetProperty("cs.csdtfolder"), "stargaze");
  outputDir = new File(outputDir, WCS.normalizeSiteName(ics.GetVar("site")));
  outputDir.mkdirs();   
%><ics:listloop listname="assets"
><ics:listget listname="assets" 
  fieldname="assettype" output="assettype"
/><ics:listget listname="assets" 
  fieldname="assetid" output="assetid"
/><asset:load name="asset"
  site='<%= ics.GetVar("assettype") %>' 
  type='<%= ics.GetVar("assettype") %>' 
  objectid='<%= ics.GetVar("assetid") %>'
/><asset:scatter name="asset" 
  prefix="pfx" 
  exclude="true" 
  fieldlist="*"
/><% String assettype = ics.GetVar("assettype");
   curDir = new File(outputDir, assettype);
   curFile = new File(curDir, ics.GetVar("assetid")+".xml");
   boolean exclude = assettype.equals("Template") || assettype.equals("CSElement") || assettype.equals("SiteEntry"); 
   if(!exclude && !ics.GetVar("pfx:status").equals("VO")) {
     curDir.mkdir(); 
%><asset:export name="asset"
  file='<%= curFile.getAbsolutePath() %>' 
  prefix="pfx"
/><%= ics.GetVar("assettype") + ":" + ics.GetVar("assetid") %>
<% } %></ics:listloop>
<% } else { %>Bad username, password or site<% } %>
</cs:ftcs>