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
%><cs:ftcs>
<% 
   boolean verified = false; 
   File inputDir = null;
   File curDir = null;
   File curFile = null;
   if(ics.GetVar("user")!=null && ics.GetVar("pass")!=null) { %>
<user:login 
  username='<%= ics.GetVar("user") %>' 
  password='<%= ics.GetVar("pass") %>'/>
<% verified = ics.GetErrno() == 0; %> 
<% } %>
<% if(ics.GetVar("site")!=null) { %>
<publication:load name="pub" 
  field="name" 
  value='<%= ics.GetVar("site") %>'/>
<% } %>
<% if(verified && ics.GetObj("pub")!=null ) { %>
<%--= ics.GetObj("pub") --%>
<publication:get name="pub"  
  field="id" 
  output="pubid"/><% 
  inputDir = new File(ics.GetProperty("cs.csdtfolder"), "stargaze");
  inputDir = new File(inputDir, WCS.normalizeSiteName(ics.GetVar("site")));
  String c = ics.GetVar("c");
  String cid = ics.GetVar("cid");
  File file = new File(new File(inputDir, c), cid+".xml");
  String subtype = ics.GetVar("subtype");
  //System.out.println(c+":"+cid+(subtype==null? "" : ":"+subtype));
  System.out.println("Import " +c+":"+cid+ (subtype==null? "" : ":"+subtype));
%><%= c+":"+cid+ (subtype==null? "" : ":"+subtype) %>
<%// Create a new asset for importing the exported file //%>
<asset:create name="newasset" 
  type='<%= ics.GetVar("c") %>'/>
<%// Scatter all fields of the XML into Sites variables //%>
<% if(subtype==null) { %>
Without Subtype:
<asset:import name="newasset" 
  prefix="pfx" 
  file='<%= file.getAbsolutePath() %>'
  pubid='<%= ics.GetVar("pubid") %>' />
<% } else { %>
With Subtype:
<asset:import name="newasset" 
  prefix="pfx" 
  file='<%= file.getAbsolutePath() %>'
  pubid='<%= ics.GetVar("pubid") %>'
  subtype='<%= ics.GetVar("subtype") %>'/>
<% } %>
<ics:setvar name="pfx:Publist:Total" value='1'/>
<ics:setvar name="pfx:Publist:0" value='<%= ics.GetVar("pubid") %>'/>
<%// Gather the variables into the newly created asset //%>
<asset:gather name="newasset"
  prefix="pfx"
  fieldlist="*"/>
<%// Save the asset //%>
<asset:save name="newasset"/><% 
} else { %>Bad username, password or site
<form method="post">
<input type="hidden" name="pagename" value="AAAgileImport">
User/Pass:<br>
<input type="text" name="user" value=""><br>
Pass:<br>
<input type="text" name="pass" value=""><br>
Site:<br>
<input type="text" name="site" value=""><br>
C/Cid:<br>
<input type="text" name="c" value=""size="5">:<input type="text" name="cid" value="" size="5"><br>
<input type="submit">
</form>
<% } %>
</cs:ftcs>