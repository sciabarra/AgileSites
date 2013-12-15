<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%
/**
 * OpenMarket/Xcelerate/AssetType/Page/ContentFormRowSpacer
 * Draw a field seperator based on the NewSection ics variable.
 *
 * @param NewSection	a ics variable.<ul>
 * 						<li>if ics.GetVar("NewSection") is true, draw a section line.
 * 						And the NewSection variable is removed after that.
 *						<li>otherwise, draw a blank row as field separator.</ul>
 */
%><cs:ftcs><%
	if (Boolean.valueOf(ics.GetVar("NewSection")).booleanValue()) {
		%><ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/><%
		ics.RemoveVar("NewSection");
	} else {
		%><ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/><%
	}
%></cs:ftcs>

