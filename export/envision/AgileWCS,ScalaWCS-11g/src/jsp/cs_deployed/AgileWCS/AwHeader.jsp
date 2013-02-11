<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="asset" uri="futuretense_cs/asset.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="render" uri="futuretense_cs/render.tld"
%><%@ page import="wcs.core.WCS"
%><cs:ftcs><ics:if condition='<%=ics.GetVar("seid")!=null%>'><ics:then><render:logdep
cid='<%=ics.GetVar("seid")%>' c="SiteEntry"/></ics:then></ics:if><ics:if condition='<%=ics.GetVar("eid")!=null%>'><ics:then><render:logdep
cid='<%=ics.GetVar("eid")%>' c="CSElement"/></ics:then></ics:if><%
String r = WCS.dispatch(ics, "agilewcs.typeless.Header");
if(r!=null) ics.StreamText(r); %></cs:ftcs>