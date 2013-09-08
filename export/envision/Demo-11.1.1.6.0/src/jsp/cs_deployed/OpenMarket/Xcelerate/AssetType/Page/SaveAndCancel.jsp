<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"
%><%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"
%><%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"
%><%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld"
%><%//
// OpenMarket/Xcelerate/AssetType/Page/SaveAndCancel
//
// INPUT
//	ssvar:
//		locale		different images are used for different locale
//	var:
//		cs_imagedir the Xcelerate base url, e.g. "/Xcelerate"
//		updatetype	"setformdefaults" means creation, save.gif is used as save button
//                  otherwise use save_changes.gif for editting.
//		ThisPage
//                  This variable is used in the checkfields() javascript.
//      DMSupported whether this asset support switching between DM and WCM views
//      cs_formmode the current view is DM or WCM
// OUTPUT
//		a Cancel button and a Save button in a row.
//      plus a optional DM or WCM link, only if the asset type support both formmode.
%><cs:ftcs>
<ics:callelement element="OpenMarket/Gator/FlexibleAssets/Common/SaveAndCancel" />
</cs:ftcs>

