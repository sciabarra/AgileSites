<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld"%>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld"%>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld"%>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld"%>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld"%>
<%@page import="java.util.*"%>

<%--
- OpenMarket/Gator/AttributeTypes/CKEDITOR

                      C K E D I T O R   3.2


  Modified: April 06, 2010
  Reason:   Migrated, Ported to latest CKEditor  Element for Flex Assets
            Completely changed Interface... 
-
--%>

<%@ page import="COM.FutureTense.Interfaces.FTValList"%>
<%@ page import="COM.FutureTense.Interfaces.ICS"%>
<%@ page import="COM.FutureTense.Interfaces.IList"%>
<%@ page import="COM.FutureTense.Interfaces.Utilities"%>
<%@ page import="com.openmarket.gator.interfaces.IAttributeTypeManager"%>
<%@ page import="com.openmarket.gator.interfaces.IPresentationObject"%>
<%@ page import="com.openmarket.assetframework.interfaces.IAssetTypeManager"%>
<%@ page import="com.openmarket.assetframework.interfaces.AssetTypeManagerFactory"%>
<%@ page import="com.openmarket.xcelerate.publish.EmbeddedLink"%>
<%!
	final static String DEFAULT_HEIGHT = "200px";
	final static String DEFAULT_WIDTH = "";
	final static String DEFAULT_TOOLBAR = "CS";
	final static String DEFAULT_DEPTYPE = "false";
	//[KGF 2008-12-02] Adding lazyload.
	//I will be coding such that if it is not explicitly "false" it will be treated as "true".
	final static String DEFAULT_LAZYLOAD = "true";
%>
<cs:ftcs>
	<ics:getproperty name="xcelerate.ckeditor.basepath" file="futuretense_xcel.ini" output="basepath" />
	<ics:getproperty name="ft.cgipath" file="futuretense.ini" output="cgipath" />
<%
	final String cgiPath = ics.GetVar("cgipath");
%>
	<ics:if condition='<%=!Utilities.goodString(ics.GetVar("basepath"))%>'>
	<ics:then>
		<ics:setvar name="basepath" value='<%=ics.GetVar("cgipath") + "ckeditor/"%>' />
	</ics:then>
	</ics:if>	
	<ics:setvar name="doDefaultDisplay" value="no" />
	
	<script type="text/javascript" src="<ics:getvar name="basepath"/>ckeditor.js"></script>
<%
	boolean isSingleValued = "single".equals(ics.GetVar("EditingStyle"));
	IList attributeValueList = ics.GetList("AttrValueList", false);
	boolean hasValues = attributeValueList != null && attributeValueList.hasData();
	boolean isUrlField = "url".equals(ics.GetVar("AttrType"));
	
	boolean allowEmbeddedLinks = "true".equals(ics.GetVar("AllowEmbeddedLinks"));
	FTValList args = new FTValList();
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "XSIZE");
	args.setValString("VARNAME", "XSIZE");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "YSIZE");
	args.setValString("VARNAME", "YSIZE");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "TOOLBAR");
	args.setValString("VARNAME", "TOOLBAR");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "LAZYLOAD");
	args.setValString("VARNAME", "LAZYLOAD");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "ALLOWEDASSETTYPES");
	args.setValString("VARNAME", "ALLOWEDASSETTYPES");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "SCRIPT");
	args.setValString("VARNAME", "SCRIPT");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "IMAGEPICKERID");
	args.setValString("VARNAME", "IMAGEPICKERID");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "DEPTYPE");
	args.setValString("VARNAME", "DEPTYPE");
	ics.runTag("presentation.getprimaryattributevalue", args);

	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "WIDTH");
	args.setValString("VARNAME", "WIDTH");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "HEIGHT");
	args.setValString("VARNAME", "HEIGHT");
	ics.runTag("presentation.getprimaryattributevalue", args);	
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "CONFIG");
	args.setValString("VARNAME", "CONFIG");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "CONFIGOBJ");
	args.setValString("VARNAME", "CONFIGOBJ");
	ics.runTag("presentation.getprimaryattributevalue", args);
	args.setValString("ATTRIBUTE", "RESIZE");
	args.setValString("VARNAME", "RESIZE");
	ics.runTag("presentation.getprimaryattributevalue", args);	

	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("ATTRIBUTE", "IMAGEASSETTYPE");
	args.setValString("VARNAME", "IMAGEASSETTYPE");
	ics.runTag("presentation.getprimaryattributevalue", args);

	args.setValString("NAME", ics.GetVar("PresInst"));
	args.setValString("VARNAME", "MAXVALUES");
	ics.runTag("presentation.getmaxvalues", args);
	String maximumValues = ics.GetVar("MAXVALUES");
	maximumValues = Utilities.goodString(maximumValues) && !"0".equals(maximumValues) ? maximumValues : "-1";

	if (!Utilities.goodString(ics.GetVar("XSIZE"))) ics.SetVar("XSIZE", DEFAULT_WIDTH);
	if (!Utilities.goodString(ics.GetVar("YSIZE"))) ics.SetVar("YSIZE", DEFAULT_HEIGHT);
	if (!Utilities.goodString(ics.GetVar("LAZYLOAD"))) ics.SetVar("LAZYLOAD", DEFAULT_LAZYLOAD);
	if (!Utilities.goodString(ics.GetVar("TOOLBAR"))) ics.SetVar("TOOLBAR", DEFAULT_TOOLBAR);
	if (!Utilities.goodString(ics.GetVar("DEPTYPE"))) ics.SetVar("DEPTYPE", DEFAULT_DEPTYPE);

	if (!Utilities.goodString(ics.GetVar("WIDTH"))) {
		if (Utilities.goodString(ics.GetVar("XSIZE"))) {
			ics.SetVar("WIDTH", ics.GetVar("XSIZE"));
		} else {
			ics.SetVar("WIDTH", DEFAULT_WIDTH);
		}
	} 
	
	if (!Utilities.goodString(ics.GetVar("HEIGHT"))) {
		if (Utilities.goodString(ics.GetVar("YSIZE"))) {
			ics.SetVar("HEIGHT", ics.GetVar("YSIZE"));
		} else {
			ics.SetVar("HEIGHT", DEFAULT_HEIGHT);
		}
	}
	
	//Take the width and height if they come with px else add it.
	if (ics.GetVar("WIDTH") != null) {
		String _WIDTH = ics.GetVar("WIDTH").trim();
		ics.SetVar("WIDTH",_WIDTH);
		try {
            Integer.parseInt(_WIDTH);
            ics.SetVar("WIDTH",_WIDTH + "px");
        } catch (Exception ex) {
			// if width is empty, we should not add 'px' to width.
			// No exception to catch.
        }
	}
	
	if (ics.GetVar("HEIGHT") != null) {
		String _HEIGHT = ics.GetVar("HEIGHT").trim();
		String HEIGHT=_HEIGHT.endsWith("px")?_HEIGHT:_HEIGHT+"px";
		ics.SetVar("HEIGHT",HEIGHT);
	}

	if (Utilities.goodString(ics.GetVar("RESIZE"))) {
		ics.SetVar("RESIZE", ics.GetVar("RESIZE"));
	} else {
		ics.SetVar("RESIZE", "");
	}

	//Generate the ids for the fckeditor so that if there are muliple instances(In different fields and not as in multi value) 
	//on the same form they can be handled idependently
	String _genId = String.valueOf((int) (Math.random()*10000000));
%>		
			
<satellite:link pagename="OpenMarket/Xcelerate/Actions/FCKEditorRenderer" assembler="query" outstring="urlFCKEditorRenderer">
	<satellite:argument name="cs_environment" 	value='<%=ics.GetVar("cs_environment")%>' />
	<satellite:argument name="cs_formmode" 		value='<%=ics.GetVar("cs_formmode")%>' />
	<satellite:argument name="cs_imagedir" 		value='<%=ics.GetVar("cs_imagedir")%>' />
</satellite:link> 

<tr>
	<ics:callelement element="OpenMarket/Gator/FlexibleAssets/Common/DisplayAttributeName" />
	<td></td>
	<td>
	<ics:if condition='<%= "no".equals(ics.GetVar("MultiValueEntry")) %>'>
	<ics:then>
		<ics:if condition="<%= isUrlField %>">
		<ics:then>
			<ics:if condition="<%= hasValues %>">
			<ics:then>
				<ics:listget listname="AttrValueList" fieldname="urlvalue" output="filename" />
				<ics:listget listname="AttrValueList" fieldname="@urlvalue" output="MyAttrVal" />
			</ics:then>
			<ics:else>
				<ics:resolvevariables name="CS.UniqueID.txt" output="filename" />
			</ics:else>
			</ics:if>
			<string:encode variable="cs_SingleInputName" varname="cs_SingleInputName"/> 
			<input 	type="hidden"
					name="<ics:getvar name="cs_SingleInputName"/>_file"
					value="<ics:getvar name="filename"/>" />
		</ics:then>
		</ics:if>
<%
		String attributeValue = ics.GetVar("MyAttrVal");
		attributeValue = (attributeValue == null ? "" : attributeValue);
%>
		<string:encode variable="cs_SingleInputName" varname="cs_SingleInputName"/> 
		<ics:setvar name="currentValue" value='<%= attributeValue %>' />
		<ics:setvar name="currentInput" value='<%= ics.GetVar("cs_SingleInputName") %>' />
			
		<ics:if condition="<%= allowEmbeddedLinks %>">
		<ics:then>
			<ics:if condition='<%= !"true".equals(ics.GetVar("embeddedlink_inited")) %>'>
			<ics:then>
				<ics:setvar name="enableEmbeddedLinks" value='1' />
			</ics:then>
			</ics:if>
		</ics:then>
		<ics:else>
			<ics:setvar name="enableEmbeddedLinks" value='0' />
		</ics:else>
		</ics:if> 

<!--
		 Updated  : This is  the Script To interface create an instance of
					CKeditor.  Replace and Build

		 So I think that this covers all the current options related to creation of the instances.
		 Of course, you could use directly the ckeditor constructor,
		 but unless you are absolutely sure that you know what you are doing,
		 you should avoid it.
--> 
<% 
		String defaultFormStyle = "";
		String ckEditorWidth = ics.GetVar("WIDTH");
		if ("".equals(ckEditorWidth) || "px".equalsIgnoreCase(ckEditorWidth)) 
			defaultFormStyle = "defaultFormStyle";
%> 
		<div class='<%= defaultFormStyle %>'>
			<input 	type="hidden"
					id='hidden_<ics:getvar name="currentInput"/>'
					name='<ics:getvar name="currentInput"/>'
					value='<string:stream variable="currentValue"/>' />
					
			<div 	dojoType="dojox.layout.ContentPane"
					id='<ics:getvar name="currentInput"/>'
					style='padding:4px 4px 0 4px; width: <ics:getvar name="WIDTH"/>; height: <ics:getvar name="HEIGHT"/>; border:1px solid #E6E6E6'>
<%
		//Keep textarea empty if this is new fck instance from "Add Another" button.
		String name = ics.GetVar("cs_MultipleInputName");
		String count = name.substring(name.lastIndexOf("_") + 1, name.length());
		if (Integer.parseInt(count) <= attributeValueList.numRows())
		{
			EmbeddedLink link = new EmbeddedLink(ics,ics.GetVar("currentValue"), false, false, true);
			String ret = link.evaluate();
%>
			<%= ret %>
<%
		}
%>
			</div>
			<script type="text/javascript">
				var ckeditor,ckconfig,TOOLBAR;
				CKEDITOR.config.csRootContext = '<%=ics.GetVar("cgipath")%>' ;
				CKEDITOR.config.assetId = '<ics:getvar name="ContentDetails:id"/>';
				CKEDITOR.config.assetName = '<ics:getvar name="ContentDetails:name"/>';
				CKEDITOR.config.assetType = '<ics:getvar name="AssetType"/>';
				CKEDITOR.config.urlFCKEditorRenderer = '<ics:getvar name="urlFCKEditorRenderer"/>';
				CKEDITOR.config.cs_environment = '<ics:getvar name="cs_environment"/>';
				CKEDITOR.config.CSSitePath = '<ics:getvar name="cgipath"/>';
				CKEDITOR.config.AutoDetectLanguage = 'false';
				var locale = '<ics:getssvar name="locale"/>'.toLowerCase().replace('_','-');
				CKEDITOR.config.language = CKEDITOR.lang.detect('en',locale);	
				CKEDITOR.config.BasePath = '<ics:getvar name="basepath"/>';
				CKEDITOR.config.doHtml = "true"  ;
				CKEDITOR.config.doHtmlEncoding = true ;
			
<% 
			if (null != ics.GetVar("showSiteTree")) { 
%>
				CKEDITOR.config.showSiteTree = '<%=ics.GetVar("showSiteTree").trim()%>';
<% 
			} 
%>
				function loadCkeditor_<%=_genId%>()
				{
					var configobj,
					ckid = "<ics:getvar name="currentInput"/>",
					config_<%=_genId%>=CKEDITOR.config,TOOLBAR;
<% 	
			if (Utilities.goodString(ics.GetVar("CONFIGOBJ"))) {
%>
					configobj = <%=ics.GetVar("CONFIGOBJ")%>; 
<%
			} 
			if (Utilities.goodString(ics.GetVar("CONFIG"))) {
%>
					config_<%=_genId%>.customConfig='<%=ics.GetVar("CONFIG")%>';
<% 
			} 
%>
					config_<%=_genId%>.width = (configobj&&configobj.width)?configobj.width:'<ics:getvar name="WIDTH"/>';
					config_<%=_genId%>.height = (configobj&&configobj.height)?configobj.height:'<ics:getvar name="HEIGHT"/>';
					config_<%=_genId%>.resize_enabled=(configobj&&configobj.resize_enabled)?configobj.resize_enabled:(('<%=ics.GetVar("RESIZE")%>'.toUpperCase()=='TRUE')? true: false);
					config_<%=_genId%>.assetId = '<ics:getvar name="ContentDetails:id"/>';
					config_<%=_genId%>.fieldName= '<ics:getvar name="currentInput"/>';
					config_<%=_genId%>.fieldDesc = '<ics:getvar name="currentAttrName"/>';
					config_<%=_genId%>.editingstyle = '<%=ics.GetVar("EditingStyle")%>';
					// Retrieve one of the Attribute Property
					// To determine if we allow Embedded Asset Links
					config_<%=_genId%>.enableEmbeddedLinks = '<ics:getvar name="enableEmbeddedLinks"/>';
<%
					if (Utilities.goodString(ics.GetVar("ALLOWEDASSETTYPES"))) {
%>
					config_<%=_genId%>.allowedassettypes = '<ics:getvar name="ALLOWEDASSETTYPES"/>';
<%
					} if (Utilities.goodString(ics.GetVar("SCRIPT"))) {
%>
					config_<%=_genId%>.script = '<ics:getvar name="SCRIPT"/>';
<%						
					} if (Utilities.goodString(ics.GetVar("IMAGEASSETTYPE"))) { 
%>
					config_<%=_genId%>.imageassettype = '<ics:getvar name="IMAGEASSETTYPE"/>';
<%
					}
					
			if (Utilities.goodString(ics.GetVar("IMAGEPICKERID"))) {
				IAssetTypeManager atm = AssetTypeManagerFactory.getATM(ics);
				ics.SetObj("atmgr", atm.locateAssetManager("AttrTypes"));
				IAttributeTypeManager iam = (IAttributeTypeManager) ics.GetObj("atmgr");
				try {
					IPresentationObject presInst = iam.getPresentationObject(ics.GetVar("IMAGEPICKERID"));
					String ASSETTYPENAME = presInst.getPrimaryAttributeValue("ASSETTYPENAME");
					String ATTRIBUTETYPENAME = presInst.getPrimaryAttributeValue("ATTRIBUTETYPENAME");
					String ATTRIBUTENAME = presInst.getPrimaryAttributeValue("ATTRIBUTENAME");
%>
					config_<%=_genId%>.ip_assettypename = "<%=ASSETTYPENAME%>";
					config_<%=_genId%>.ip_attributetypename = "<%=ATTRIBUTETYPENAME%>";
					config_<%=_genId%>.ip_attributename = "<%=ATTRIBUTENAME%>";
<%	
				} catch(Exception e){
%>
					<ics:logmsg severity="warn"
								msg='<%="Cannot locate the imagepicker.Please check the id " + ics.GetVar("IMAGEPICKERID")%>'/>
<%
				}
			}
%>
					if (CKEDITOR.config.cs_environment == "ucform")
						config_<%=_genId%>.toolbar =(configobj&&configobj.toolbar)?configobj.toolbar:'SITES';	
					else
						config_<%=_genId%>.toolbar =(configobj&&configobj.toolbar)?configobj.toolbar:'<%=ics.GetVar("TOOLBAR")%>';  // toolBarSet;
					
			<ics:if condition='<%="exists".equals(ics.GetVar("DEPTYPE"))%>'>
			<ics:then>
					config_<%=_genId%>.DEPTYPE = "true"; 
			</ics:then>
			<ics:else>
					config_<%=_genId%>.DEPTYPE = "false";
			</ics:else>
			</ics:if>
					if (configobj) {
						for (var key in configobj) {
							if (key != "toolbar" || key != "width" || key != "height" || key != "resize_enabled")
								config_<%=_genId%>[key]=configobj[key];
						}
					}
					ckeditor = (config_<%=_genId%>.customConfig) ? new CKEDITOR.replace( ckid , {customConfig:config_<%=_genId%>.customConfig} ):new CKEDITOR.replace( ckid , config_<%=_genId%> );
					setCKEventListeners_<%=_genId%>(ckeditor);
				}
			
			<%-- [KGF] don't create yet if LAZYLOAD is false! Instead, show a textarea and add an onclick. --%>
			<ics:if condition='<%="false".equals(ics.GetVar("LAZYLOAD"))%>'>
			<ics:then>
				<%-- replace textarea immediately with the new ckeditor --%>
				loadCkeditor_<%=_genId%>()
			</ics:then>
			<ics:else>
				<%-- hook up onFocus for ContentPane to lazy loaded ckeditor --%>
				dojo.addOnLoad(function(){
					var contentPane_<%=_genId%> = dijit.byId('<ics:getvar name="currentInput"/>');
					dojo.connect(contentPane_<%=_genId%>,"onFocus", function() { loadCkeditor_<%=_genId%>(); })
				});
			</ics:else>
			</ics:if>
			
				// Attaches all CKEditor Event Listeners
				function setCKEventListeners_<%=_genId%>(editor) {
					// Fired when the attributes CKEDITOR instance is created, fully initialized,
					// ALL FW plugins loaded and ready for interaction.
					// Set Plugin Command Buttons DISABLED, etc depending on Configuration..
					editor.on( 'instanceReady', function( e ) {
						ck_loadCompleteCount++;
					}) ;

					editor.on( 'mode', function( e ) {
						// Editor is ready, loaded and intilized
						// Get the editor Handle Instance
						var ckeditor = e.editor ;
						// Get the editor Config
						var config = ckeditor.config ;
						// Set Configuration to HTML from Rendered Data  global flag

						// Get All FW Asset Link / Include plugin commands
						var cmdinclude 		= ckeditor.getCommand( 'fwincludeasset') ;
						var cmdlink    		= ckeditor.getCommand( 'fwlinkasset') ;
						var cmdincludenew	= ckeditor.getCommand( 'fwincludenewasset' ) ;
						var cmdlinknew    	= ckeditor.getCommand( 'fwlinknewasset') ;
						var cmdip 			= ckeditor.getCommand( 'fwimagepicker' ) ;

						// If the attribute property Allow Embedded Links: is No
						// Then Disable THE TRAY of Include / Link Assets plus Image Picker  
						if (ckeditor.config.enableEmbeddedLinks=='0')
						{
							// No Allowed Embedded Links 
							cmdinclude.setState(CKEDITOR.TRISTATE_DISABLED);
							cmdlink.setState(CKEDITOR.TRISTATE_DISABLED);
							cmdincludenew.setState(CKEDITOR.TRISTATE_DISABLED);
							cmdlinknew.setState(CKEDITOR.TRISTATE_DISABLED);                                   
							cmdip.setState(CKEDITOR.TRISTATE_DISABLED);
						} else {
							if(ckeditor.mode === 'wysiwyg'){
								if(!ckeditor.config.ip_assettypename)
									cmdip.setState(CKEDITOR.TRISTATE_DISABLED);
								else
									cmdip.setState(CKEDITOR.TRISTATE_OFF);
							}
						}
					}) ;
				}
			</script>
		</div>

		<ics:removevar name="currentValue" />
		
	</ics:then>
	<ics:else>
		<ics:if condition="<%= isUrlField %>">
		<ics:then>
			<ics:if condition="<%= !hasValues %>">
			<ics:then>
				<ics:resolvevariables name="CS.UniqueID.txt" output="filename" />
				<string:encode variable="cs_MultipleInputName" varname="cs_MultipleInputName"/> 
				<input 	type="hidden"
					name="<ics:getvar name="cs_MultipleInputName"/>_file"
					value="<ics:getvar name="filename"/>" />
			</ics:then>
			</ics:if>
		</ics:then>
		</ics:if>
		<ics:setvar name="currentInput" value='<%= ics.GetVar("cs_MultipleInputName") %>' />

<%		
		String showSiteTree = "";				
		if (null != ics.GetVar("showSiteTree")) {
			showSiteTree = ics.GetVar("showSiteTree").trim();
		} 
		
		String allowedassettypes = "";
		if (Utilities.goodString(ics.GetVar("ALLOWEDASSETTYPES"))) {
			allowedassettypes = ics.GetVar("ALLOWEDASSETTYPES");
		} 
		
		String script = "";
		if (Utilities.goodString(ics.GetVar("SCRIPT"))) {
			script = ics.GetVar("SCRIPT");
		}
		
		String imageassettype="";
		if (Utilities.goodString(ics.GetVar("IMAGEASSETTYPE"))) {
			imageassettype = ics.GetVar("IMAGEASSETTYPE");
		}
		
		String ASSETTYPENAME = "";
		String ATTRIBUTETYPENAME = "";
		String ATTRIBUTENAME = "";
		if (Utilities.goodString(ics.GetVar("IMAGEPICKERID"))) {
			IAssetTypeManager atm = AssetTypeManagerFactory.getATM(ics);
			ics.SetObj("atmgr", atm.locateAssetManager("AttrTypes"));
			IAttributeTypeManager iam = (IAttributeTypeManager) ics.GetObj("atmgr");
			try {
				IPresentationObject presInst = iam.getPresentationObject(ics.GetVar("IMAGEPICKERID"));
				ASSETTYPENAME = presInst.getPrimaryAttributeValue("ASSETTYPENAME");
				ATTRIBUTETYPENAME = presInst.getPrimaryAttributeValue("ATTRIBUTETYPENAME");
				ATTRIBUTENAME = presInst.getPrimaryAttributeValue("ATTRIBUTENAME");
			} catch (Exception e) {
%>
				<ics:logmsg severity="warn"
							msg='<%="Cannot locate the imagepicker.Please check the id " + ics.GetVar("IMAGEPICKERID")%>' />
<%
			}
		}
		
		String _toolbar=("ucform".equals(ics.GetVar("cs_environment"))) ? "SITES" : ics.GetVar("TOOLBAR");
		boolean _resize = (("TRUE".equals(ics.GetVar("RESIZE").toUpperCase()))? true: false);
		String width = ics.GetVar("WIDTH");
		String height= ics.GetVar("HEIGHT");
		String inline_params="";
		if (Utilities.goodString(ics.GetVar("CONFIGOBJ"))) {
			String configobj=ics.GetVar("CONFIGOBJ");
			String[] tokens = configobj.replaceAll("[{}'\"]","").split(",");
			for (String token : tokens) {
				int delim_index = token.indexOf(':');
				String KEY = token.substring(0,delim_index);
				String VALUE = token.substring(delim_index+1);
				if(KEY.equals("toolbar")) { _toolbar = VALUE; continue; }
				if(KEY.equals("resize_enabled")) { _resize = Boolean.valueOf(VALUE); continue; }
				if(KEY.equals("width")) { width = VALUE; continue; }
				if(KEY.equals("height")) { height = VALUE; continue; }
				inline_params = inline_params + ",'"+ KEY + "': '"+VALUE + "'";
			}
		}
		
		if (Utilities.goodString(ics.GetVar("CONFIG"))) {
			inline_params = inline_params+ ",'customConfig': '"+ics.GetVar("CONFIG")+"'";
		}
		
		String DEPTYPE = "false";
		if ("exists".equals(ics.GetVar("DEPTYPE"))) 
			DEPTYPE = "true";
		
		String editorParams = "{'config': {'csRootContext': '" + ics.GetVar("cgipath") + "'," +
									  //"'width': '" + width + "'," +
									  "'height': '" + height + "'," +
									  "'resize_enabled': " + _resize + ","+
									  "'assetId': '" + ics.GetVar("ContentDetails:id") + "'," +
									  "'assetName': '" + ics.GetVar("ContentDetails:name") + "'," +
									  "'assetType': '" + ics.GetVar("AssetType") + "'," +
									  "'editingstyle': '" + ics.GetVar("EditingStyle") + "'," +
									  "'urlFCKEditorRenderer': '" + ics.GetVar("urlFCKEditorRenderer") + "'," +
									  "'fieldName': '" + ics.GetVar("currentInput") + "'," +
									  "'fieldDesc': '" + ics.GetVar("currentAttrName") + "'," +
									  "'showSiteTree': '" + showSiteTree + "'," +
									  "'enableEmbeddedLinks': '" + (("TRUE".equals(ics.GetVar("AllowEmbeddedLinks").toUpperCase()))? 1: 0) + "'," +
									  "'cs_environment': '" + ics.GetVar("cs_environment") + "'," +
									  "'CSSitePath': '" + ics.GetVar("cgipath") + "'," +
									  "'AutoDetectLanguage': 'false'," +
									  "'allowedassettypes': '" + allowedassettypes + "'," +
									  "'script': '" + script + "'," +
									  "'ip_assettypename': '" + ASSETTYPENAME + "'," +
									  "'ip_attributetypename': '" + ATTRIBUTETYPENAME + "'," +
									  "'ip_attributename': '" + ATTRIBUTENAME + "'," +
									  "'imageassettype': '" + imageassettype + "'," +
									  "'toolbar': '" + _toolbar + "'," +
									  "'BasePath': '" + ics.GetVar("basepath") + "'," +
									  "'doHtml': 'true'," +
									  "'doHtmlEncoding': 'true'," +
									  "'DEPTYPE': '" + DEPTYPE + "'" +
									  inline_params+
									 "}, " + 
							 "'width': '" + width + "', " +
							 "'isComplexData': '" + true + "'" +
						  "}";
	
%> 
		<ics:callelement element="OpenMarket/Gator/AttributeTypes/RenderMultiValuedTextEditor">
			<ics:argument name="editorName" 	value="fw.ui.dijit.form.FormCKEditor" />
			<ics:argument name="editorParams" 	value='<%= editorParams %>' />
			<ics:argument name="multiple" 		value="true" />
			<ics:argument name="isUrlField"		value="<%= String.valueOf(isUrlField) %>" />
			<ics:argument name="maximumValues" 	value="<%= maximumValues %>" />
		</ics:callelement> 

		<ics:if condition='<%="multiple-ordered".equals(ics.GetVar("EditingStyle"))%>'>
		<ics:then>
			<ics:if condition='<%=ics.GetVar("__fckeditorscripts") == null%>'>
			<ics:then>
				<ics:setvar name="__fckeditorscripts" value="true" />
			</ics:then>
			</ics:if>
		</ics:then>
		</ics:if> 
	</ics:else>
	</ics:if>
	
	</td>
	<td></td>
</tr>	
	
</cs:ftcs>
