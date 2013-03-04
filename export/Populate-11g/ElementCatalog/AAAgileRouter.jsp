<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="render" uri="futuretense_cs/render.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ page import="wcs.core.*"
%><cs:ftcs><%
 System.out.println("%%%");
 Call c = WCS.route(ics, 
   ics.GetVar("c"), 
   ics.GetVar("cid"),
   request.getQueryString());
%><ics:callelement  element='<%= c.getName() %>'><%
 for(String k: c.keysLeft()) {
%><ics:argument name='<%= k %>' value='<%= c.getOnce(k) %>'/><%	 
 } 
%></ics:callelement></cs:ftcs>
