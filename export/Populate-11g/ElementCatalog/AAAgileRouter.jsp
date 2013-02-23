<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><cs:ftcs><% try { 
%><%= wcs.core.WCS.route(ics, 
  ics.GetVar("c"), 
  ics.GetVar("cid"),
  request.getQueryString()) 
%><% } catch(Exception ex) { 
 ex.printStackTrace(); 
%><%= ex.getMessage() %><%
} %></cs:ftcs>
