<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%//
// OpenMarket/Xcelerate/AssetType/Template/ContentDetailsHelper
//
// INPUT
//
// OUTPUT
//%>
<%@ page import="COM.FutureTense.Interfaces.FTValList" %>
<%@ page import="COM.FutureTense.Interfaces.ICS" %>
<%@ page import="COM.FutureTense.Interfaces.IList" %>
<%@ page import="COM.FutureTense.Interfaces.Utilities" %>
<%@ page import="COM.FutureTense.Util.ftErrors" %>
<%@ page import="COM.FutureTense.Util.ftMessage"%>
<cs:ftcs>
<%
if(ics.GetVar("cdAction").equals("ceil")){
String paramName=ics.GetVar("paramName");
String paramValue=ics.GetVar("paramValue");
ics.SetVar(paramName,new Double(Math.ceil(Double.parseDouble(paramValue))).intValue());
}else if(ics.GetVar("cdAction").equals("calculateFromToIndex")){
int displayPage = Integer.parseInt(ics.GetVar("displayPage"));
int totalRows = Integer.parseInt(ics.GetVar("totalRows"));
int totalPages = Integer.parseInt(ics.GetVar("totalPages"));
int fetchSize = Integer.parseInt(ics.GetVar("rowsPerPage"));
int start = (displayPage - 1)*fetchSize;
int last =  displayPage*fetchSize - 1;
int startIndex = start + 1;
int endIndex = 0;
if(displayPage != totalPages){
	endIndex = last + 1;
} else {
	endIndex = totalRows;
}
ics.SetVar("startIndex",startIndex);
ics.SetVar("endIndex",endIndex);
}else if(ics.GetVar("cdAction").equals("getElementIndex")){
	String pageNumStr = ics.GetVar("param");
	String pageNumStrParam[] = pageNumStr.split(":");
	ics.SetVar("currentElementNum",pageNumStrParam[2]);
}
%>
</cs:ftcs>