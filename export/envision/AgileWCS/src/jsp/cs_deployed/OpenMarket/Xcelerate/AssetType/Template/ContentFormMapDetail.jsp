<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="render" uri="futuretense_cs/render.tld" %>

<%//
// OpenMarket/Xcelerate/AssetType/Template/ContentFormMapDetail
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

<!-- user code here -->
<%
	int curCount = 0;
%>

<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer">
	<ics:argument name="SpaceAbove" value="No"/>
</ics:callelement>
<tr>
<td colspan="8">
	<xlat:stream key="dvin/Common/AT/MapDetail"/>
</td>
</tr>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
<tr>
	<td colspan="8" class="light-line-color"><img height="1" width="1" src="Variables.cs_imagedir/graphics/common/screen/dotclear.gif" REPLACEALL="Variables.cs_imagedir"/></td>
</tr>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>

<%
String stotal = ics.GetVar( "total" );
int total = 0;
try
{
	total = Integer.parseInt( stotal );
}
catch( Exception e )
{
   out.println( "parsing total error." );
}
if ( 1 == total && ics.GetVar( "ContentDetails:Mapping:0:key" ) != null )
{
   for ( int i = 0; true; i ++ )
   {
      if ( ics.GetVar( "ContentDetails:Mapping:" + i + ":key" ) != null )
      {
         total = i + 2;
      }
      else
      {
         break;
      }   
   }
}
%>
	<tr>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text"><xlat:stream key="dvin/Common/AT/Map/key"/></td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text"><xlat:stream key="dvin/Common/AT/Map/type"/></td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text"><xlat:stream key="dvin/Common/AT/Map/value"/></td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td class="form-label-text"><xlat:stream key="dvin/Common/Site"/></td>	</tr>	
		<ics:sql sql="select id, name from Publication" listname="publist" table="Publication" />

<%	
for ( curCount = 0; curCount < total; curCount ++ )
{
%>
	<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
<%
	String siteid = "Template:Mapping:" + curCount + ":siteid";
%>
	<tr>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td>
		<ics:if condition='<%=curCount < total-1 && ics.GetVar( "ContentDetails:Mapping:" + curCount + ":key" ) != null%>' >
		<ics:then>			
			<ics:setvar name="CKey" value='<%=ics.GetVar( "ContentDetails:Mapping:" + curCount + ":key" ) %>' />			
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":key"%>' VALUE='<string:stream variable="CKey"/>' SIZE="20" MAXLENGTH="32"/>
		</ics:then>
		<ics:else>
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":key"%>' VALUE="" SIZE="20" MAXLENGTH="32"/>
		</ics:else>
		</ics:if>
		</td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td>
<%
		String typename = "Template:Mapping:" + curCount + ":type";
%>		
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:" + curCount + ":type") != null%>' >
		<ics:then>			
			<ics:setvar name="CType" value='<%=ics.GetVar("ContentDetails:Mapping:" + curCount + ":type")%>' />
			<SELECT name="<%= typename %>" >	
<%
			      String type = ics.GetVar( "CType" );
			      %>
			      <OPTION VALUE="tname" <%="tname".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEtname"/>
			      </OPTION>
			      
			      <OPTION VALUE="assettype" <%="assettype".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEassettype"/>
			      </OPTION>
			      		      
			      <OPTION VALUE="assetname" <%="assetname".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEassetname"/>
			      </OPTION>
			      
			      <OPTION VALUE="asset" <%="asset".equals(type)? "selected":""%>>
			      	<xlat:stream key="dvin/Common/AT/Map/TYPEasset"/>
			      </OPTION>
			</SELECT>
		</ics:then>
		<ics:else>
			<SELECT name="<%= typename %>" >	
				<OPTION VALUE="tname" > <xlat:stream key="dvin/Common/AT/Map/TYPEtname"/> </OPTION>
				<OPTION VALUE="assettype" > <xlat:stream key="dvin/Common/AT/Map/TYPEassettype"/> </OPTION>
				<OPTION VALUE="assetname" > <xlat:stream key="dvin/Common/AT/Map/TYPEassetname"/> </OPTION>
				<OPTION VALUE="asset" > <xlat:stream key="dvin/Common/AT/Map/TYPEasset"/> </OPTION>
			</SELECT>
		</ics:else>
		</ics:if>
		</td>
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td>
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value") != null%>' >
		<ics:then>			
			<ics:setvar name="CVALUE" value='<%=ics.GetVar("ContentDetails:Mapping:"+ curCount + ":value")%>' />
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE='<string:stream variable="CVALUE"/>' SIZE="32" MAXLENGTH="256"/>
		</ics:then>
		<ics:else>
			<INPUT TYPE="TEXT" NAME='<%= "Template:Mapping:" + curCount + ":value"%>' VALUE="" SIZE="32" MAXLENGTH="256"/>
		</ics:else>
		</ics:if>
		</td>
		
		<td><img height="1" width="6" src='<%=ics.GetVar("cs_imagedir")+"/graphics/common/screen/dotclear.gif"%>'/></td>
		<td>
<%
		String CSiteid = "Template:Mapping:" + curCount + ":siteid";
%>		
		
		<ics:if condition='<%=curCount < total-1 && ics.GetVar("ContentDetails:Mapping:" + curCount + ":siteid") != null%>' >
		<ics:then>			
			<ics:setvar name="CSiteid" value='<%=ics.GetVar("ContentDetails:Mapping:" + curCount + ":siteid")%>' />
			<SELECT name="<%= CSiteid %>" >	
				<ics:listloop listname="publist" >
					<ics:listget listname="publist" fieldname="id" output="ssiteid" />
					<ics:listget listname="publist" fieldname="name" output="ssitename" />
<%
			      String cursiteid = ics.GetVar( "CSiteid" );
			      String selectsiteid = ics.GetVar( "ssiteid" );
%>
			      <OPTION VALUE='<%=selectsiteid%>' <%=cursiteid.equals(selectsiteid)? "selected":""%>>
			      	<ics:getvar name="ssitename" />
			      </OPTION>
				</ics:listloop>
			</SELECT>
		</ics:then>
		<ics:else>
			<SELECT name="<%= siteid %>" >	
				<ics:listloop listname="publist" >
				<ics:listget listname="publist" fieldname="id" output="ssiteid" />
				<ics:listget listname="publist" fieldname="name" output="ssitename" />
				<OPTION VALUE='<%=ics.GetVar( "ssiteid" )%>' >
					<ics:getvar name="ssitename" />
				</OPTION>
				</ics:listloop>
			</SELECT>
		</ics:else>
		</ics:if>
		</td>
	</tr>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/>
<tr>
	<td colspan="8" class="light-line-color"><img height="1" width="1" src="Variables.cs_imagedir/graphics/common/screen/dotclear.gif" REPLACEALL="Variables.cs_imagedir"/></td>
</tr>
<ics:callelement element="OpenMarket/Xcelerate/UIFramework/Util/RowSpacer"/><%
}
%>		
<INPUT TYPE="hidden" NAME="Template:Mapping:Total" Value='<%=total%>' />
</cs:ftcs>