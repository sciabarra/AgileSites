<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="render" uri="futuretense_cs/render.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ page import="wcs.core.*"
%><cs:ftcs><%
  Call c = WCS.route(ics, 
   ics.GetVar("c"), 
   ics.GetVar("cid"),
   request.getQueryString());
%><ics:callelement  element='<%= c.getOnce("element") %>'><%
 for(String k: c.keysLeft()) {
%><ics:argument name='<%= k %>' value='<%= c.getOnce(k) %>'/><%	 
 } 
%></ics:callelement></cs:ftcs>