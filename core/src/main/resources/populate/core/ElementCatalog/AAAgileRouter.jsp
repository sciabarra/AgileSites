<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="render" uri="futuretense_cs/render.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ page import="wcs.core.*,wcs.api.*"
%><cs:ftcs><%! final static Log log = wcs.api.Log.getLog("jsp.wcs.api.Router");
%><%
  Call c = WCS.route(ics, 
   ics.GetVar("site"), 
   ics.GetVar("url"),
   request.getQueryString());
   log.trace(c.toString());
%><ics:callelement  element='<%= c.getOnce("element") %>'><%
 for(String k: c.keysLeft()) {
%><ics:argument name='<%= k %>' value='<%= c.getOnce(k) %>'/><%	 
 } 
%></ics:callelement></cs:ftcs>