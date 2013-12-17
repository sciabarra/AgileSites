<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="user" uri="futuretense_cs/user.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="publication" uri="futuretense_cs/publication.tld" %>
<cs:ftcs>
<% if ("ucform".equals(ics.GetVar("cs_environment"))) {%>
	<p>
        <a href="javascript:window.parent.dojo.publish('fw/ui/app/gotoview', [{type: 'localehierarchy',  mode: 'view' }]);" >
            <xlat:stream key="dvin/AT/DimensionSet/ConfigureLocaleHierarchy"/>
        </a>
    </p>
   </script>
<%} else { %>
	 <satellite:link pagename="OpenMarket/Xcelerate/Admin/LocaleHierarchy/LocaleHierarchyView">
        <satellite:parameter name="DimensionSet:id" value='<%=ics.GetVar("ContentDetails:id")%>' />
    </satellite:link>
    <p>
        <a href='<%=ics.GetVar("referURL")%>'>
            <xlat:stream key="dvin/AT/DimensionSet/ConfigureLocaleHierarchy"/>
        </a>
    </p>
<% } %>	
</cs:ftcs>