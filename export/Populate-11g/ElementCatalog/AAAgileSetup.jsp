<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><cs:ftcs><%String result =""; 
try { 
 result = wcs.scala.WCS.deploy(ics, 
  ics.GetVar("sites"), 
  ics.GetVar("username"),
  ics.GetVar("password")); 
} catch(Exception ex) { 
 ex.printStackTrace(); 
}%><%= result %></cs:ftcs>
