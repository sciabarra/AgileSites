<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="property" uri="futuretense_cs/property.tld" %>
<%//
// OpenMarket/Gator/FlexibleAssets/Common/GetInputName
//
// INPUT
//  cs_AttrName
//  cs_IsMultiple
//  cs_ValueNum ( if is multiple but not multi select)
//  cs_IsMultiSelect ( if is multiple )
//  cs_Varname
//
// OUTPUT
//  Variables.cs_Varname
//%>
<%@ page import="COM.FutureTense.Interfaces.FTValList" %>
<%@ page import="COM.FutureTense.Interfaces.ICS" %>
<%@ page import="COM.FutureTense.Interfaces.IList" %>
<%@ page import="COM.FutureTense.Interfaces.Utilities" %>
<%@ page import="COM.FutureTense.Util.ftErrors" %>
<%@ page import="COM.FutureTense.Util.ftMessage"%>
<cs:ftcs>

<%
if (ics.GetVar("cs_PropUseLegacyInputNames")==null) { %>
  <property:get inifile="gator.ini" param="cc.useLegacyInputNames" varname="cs_PropUseLegacyInputNames"/> <%
}

if (ics.GetVar("cs_AttrName")==null || ics.GetVar("cs_IsMultiple")==null || ics.GetVar("cs_Varname")==null) {
    ics.SetErrno(ftErrors.badparams);
    ics.SetVar("errdetail1", "OpenMarket/Gator/FlexibleAssets/Common/GetInputName: Missing required param(s)");
} else {
    
    if (ics.GetVar("cs_IsMultiple").equals("true") && !(ics.GetVar("cs_IsMultiSelect") != null && ics.GetVar("cs_IsMultiSelect").equals("true"))) {
        if (ics.GetVar("cs_ValueNum")==null) {
            ics.SetErrno(ftErrors.badparams);
            ics.SetVar("errdetail1", "OpenMarket/Gator/FlexibleAssets/Common/GetInputName: Missing required param(s)");
        } else {
            if (ics.GetVar("cs_PropUseLegacyInputNames").equals("false")) {
                ics.SetVar(ics.GetVar("cs_Varname"), "Attribute_"+ics.GetVar("cs_AttrName")+"_"+ics.GetVar("cs_ValueNum"));
            } else {
                ics.SetVar(ics.GetVar("cs_Varname"), ics.GetVar("cs_ValueNum")+ics.GetVar("cs_AttrName"));
            }
        }
    } else { // single value
        if (ics.GetVar("cs_PropUseLegacyInputNames").equals("false")) {
            ics.SetVar(ics.GetVar("cs_Varname"), "Attribute_"+ics.GetVar("cs_AttrName"));
        } else {
            ics.SetVar(ics.GetVar("cs_Varname"), ics.GetVar("cs_AttrName"));
        }
    }
    
} %>
</cs:ftcs>

