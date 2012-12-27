<%@ page contentType="text/html; charset=UTF-8"
		 import="COM.FutureTense.Interfaces.IList,
		 		com.openmarket.assetframework.interfaces.AssetTypeManagerFactory,
		 		com.openmarket.assetframework.interfaces.IAssetTypeManager,
		 		com.openmarket.gator.interfaces.IAttributableAssetInstance" %>
<%@ page import="com.openmarket.gator.interfaces.IAttributeTypeManager" %>
<%@ page import="com.openmarket.gator.interfaces.IPresentationObject" %>
<%@ page import="com.openmarket.gator.interfaces.ITemplateAssetManager" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="com.fatwire.assetapi.data.AssetId" %>
<%@ page import="com.openmarket.gator.attributetypes.PresentationObject" %>
<%@ page import="COM.FutureTense.Interfaces.Utilities" %>
<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="hash" uri="futuretense_cs/hash.tld" %>
<%@ taglib prefix="assetset" uri="futuretense_cs/assetset.tld" %>
<%@ taglib prefix="string" uri="futuretense_cs/string.tld" %>
<%
//
// OpenMarket/Xcelerate/AssetType/Page/AssocAttr
//    This will use selected PageDefinition to create and display the
//    associated attributes for the selected PageDefinition.
//    Asset. Modified to Not Gather the Flex Asset specifically No Parents or Groups
//    without Parents [asset|group]
//
%>
<cs:ftcs>

<!-- user code here -->
<SCRIPT LANGUAGE="JavaScript">

    function GotoNextStep(form, MyCounter, valcount, selection)
	{
		var obj = document.forms[0].elements[0];
		if (obj.form.elements['doSubmit'].value == "yes")
		{
		<%
		if (ics.GetVar("tagtype").equals("flexgrouptemplates"))
        {
            %>obj.form.TemplateChosen.value = obj.form.flexgrouptemplateid.value;<%
        }
        else
        {
            %>obj.form.TemplateChosen.value = obj.form.flextemplateid.value;<%
        }
		%>
			obj.form.MultiAttrVals.value = "addanother";
            if (selection == "add")
                obj.form.elements[MyCounter].value = parseInt(valcount) + 1;
            else
                obj.form.elements[MyCounter].value = parseInt(valcount) - 1;

			repostFlexContentForm();
		}
	}
</SCRIPT>


<!--setvar NAME="errno" VALUE="0"/-->
<!-- this is needed when multi value form is posted.. -->
<INPUT TYPE="hidden" NAME="flextemplateid" VALUE="<%=ics.GetVar("flextemplateid")%>" />

<!-- are we working with a Flex Group Or Flex Asset? -->
<!-- performance improvement by use of new tag getattributeinfo -->
<%

    String templateType= ics.GetVar("templatetype") ;
    String tagType = ics.GetVar("tagtype")  ;
    String templateId = ics.GetVar("templateid") ;

    Hashtable enabledAttributes = (Hashtable) ics.GetObj("enabledAttributes");

	IAssetTypeManager atm = AssetTypeManagerFactory.getATM(ics);

    ics.SetObj("pgtmgr", atm.locateAssetManager(ics.GetVar("templatetype")));

    ITemplateAssetManager itam = (ITemplateAssetManager) atm.locateAssetManager(ics.GetVar("templatetype"));

    String tmplID=ics.GetVar("flextemplateid");

    // Get the list of attributes for Flextemplate  ( PageDefinition )
	IList tmplattrlist = itam.getAttributeInfo(tmplID);
                   
    // Make the list available for other elements.
	ics.RegisterList("tmplattrlist", tmplattrlist);

    // Save the desired subtypes/asset reference too - but this only needs to be a local variable.
	java.util.Map<String,IList> subtypeMap = itam.getAttributeSubtypeInfo(tmplID);

	ics.SetVar("MyAttrVal", ics.GetVar("empty"));
%>
<hash:create name='treePickAttrs' />
<%
	int numAttrs = 0;

	// Error checking: Since all of the above is direct java invocations, if something goes wrong it will throw an AssetException.
	// Thus, no point in checking for errno here...
	if (tmplattrlist != null && tmplattrlist.hasData())
	{

%>
<!-- determine whether to display attribute names or descriptions -->
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/CheckAttributeDisplayStyle' />

<hash:create name='hmyURLAttrs' />
<%
        ics.SetVar("SpacerBar", "1");
        ics.SetVar("firstPassword", "1");
        ics.SetVar("myPasswordAttrs", "none");
        /*This variable is used only for realobject integration please don't use any where else
          This is for all instance java script as to be included once. This apprach is better then session
          approch. This should be some handled in real object itself */
        ics.SetVar("includeRealObjectHandle", "true");

        com.openmarket.gator.page.Page epage = (com.openmarket.gator.page.Page)ics.GetObj("theCurrentAsset");

        // Has an Asset Id been generated at this point when we render the Associated Extended Attributes.. 
        AssetId crntassetId = epage.getId();

    for(int i = 1; tmplattrlist.moveTo(i); i++)
    {
    	// Reinitializing Attribute type for every Attribute
		// An empty value resembles that a default attribute editor will be applied.
		ics.SetVar("MyAttributeType", "");
    	if(enabledAttributes == null || enabledAttributes.containsKey(tmplattrlist.getValue("name")))
        {
            if(numAttrs == 0)
            {
                //Display the attribute section header only if at least 1 attribute is enabled.
%>
<%
				}
				numAttrs++;
				ics.SetVar("tmplattrlistIndex",i);
				String attrAssetID = tmplattrlist.getValue("assetid");

                ics.SetVar("AttrID", attrAssetID);
				ics.SetVar("AttrName", tmplattrlist.getValue("name"));
				ics.SetVar("AttrDesc", tmplattrlist.getValue("description"));
				ics.SetVar("type", tmplattrlist.getValue("type"));
				ics.SetVar("uploaddir", tmplattrlist.getValue("upload"));
				ics.SetVar("attrassettype", tmplattrlist.getValue("assettypename"));
				// Also, find and register the ordered list of allowed subtypes for this type.  Will be null if "any" subtype is allowed.
				ics.RegisterList("attrassetsubtypes", subtypeMap.get(attrAssetID));
				ics.SetVar("AttrTypeID", tmplattrlist.getValue("attributetype"));
				ics.SetVar("EditingStyle", "single");
				
				// Initializing the common variables used across attribute editors.
				ics.SetObj("strAttrValues", null);
				ics.RemoveVar("imageurl");
				ics.RemoveVar("tempval");
				ics.RemoveVar("_tempval_");
				ics.RemoveVar("renderMultiValWidget");
				ics.RemoveVar("MAXVALUES");				
				ics.RemoveVar("editorName");
				
				ics.RemoveVar("RequireInfo");
				ics.RemoveVar("MultiValReqInfo");
				ics.RemoveVar("MyAttrVal");

				//Sets the display attribute description or name based on element CheckAttributeDisplayStyle
				ics.SetVar("currentAttrNameorDesc", ics.GetVar("AttrName"));

                if (ics.GetVar("attrDisplayStyle").equals("description")
					&& !ics.GetVar("AttrDesc").equals("") && ics.GetVar("AttrDesc")!=null)
				{
                    //System.out.println("\t\t [*??]ics.SetVar_"+numAttrs+"_@"+tmplattrlist.getValue("name")+"@"+ics.GetVar("attrDisplayStyle") );

%>

<string:encode varname="currentAttrNameorDesc" value='<%=ics.GetVar("AttrDesc")%>'/>
<%
				}

				if (ics.GetVar("type").equals("blob"))
				{
					ics.SetVar("type", "url");
				}

				if (!(tmplattrlist.getValue("valuestyle")).equals("S") && !(tmplattrlist.getValue("valuestyle")).equals("N"))
				{
					if (tmplattrlist.getValue("valuestyle").equals("M"))
					{
						ics.SetVar("EditingStyle", "multiple");
					}
					else
					{
						ics.SetVar("EditingStyle", "multiple-ordered");
					}

				}

				ics.SetVar("RequiredAttr", "true");
				if ((tmplattrlist.getValue("required")).equals("F"))
				{
					ics.SetVar("RequiredAttr", "false");
				}

				ics.SetVar("AllowEmbeddedLinks", "false");
				if ((tmplattrlist.getValue("embedtype")).equals("H"))
				{
					ics.SetVar("AllowEmbeddedLinks", "true");
				}

				// this is a convenience for single valued attrs, multi valued attrs need to call this again in the value loop
                // System.out.println("\t\t [**]ics.SetVar_"+numAttrs+"_{ name="+tmplattrlist.getValue("name")+", attrAssetID="+attrAssetID+", type="+tmplattrlist.getValue("type"));

%>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
	<ics:argument name='cs_AttrName' value='<%=ics.GetVar("AttrName")%>' />
	<ics:argument name='cs_IsMultiple' value='false' />
	<ics:argument name='cs_Varname' value='cs_CurrentInputName' />
</ics:callelement>

<ics:callelement element='OpenMarket/Xcelerate/UIFramework/Util/RowSpacer' />
<%
				if (!(ics.GetVar("AttrName").equals("GAProductSet")))
				{
                    String attrassetId = tmplattrlist.getValue("assetid")  ;

                    //IAttributableAssetInstance iaa = (IAttributableAssetInstance) ics.GetObj("theCurrentAsset");
                    com.openmarket.gator.page.Page iaa = (com.openmarket.gator.page.Page) ics.GetObj("theCurrentAsset");

                    //System.out.println("\t\t [GOT-IAttributableAssetInstance]ics.SetVar_"+numAttrs+"_@"+tmplattrlist.getValue("name")+"@iaa@"+iaa );
                                                           
                    ics.RegisterList("AttrValueList", iaa.getAttribute(tmplattrlist.getValue("assetid")));
                    IList attrValueList = ics.GetList("AttrValueList");
					IPresentationObject presentationObj = null;
					String XMLParseError = null;

					ics.SetVar("doDefaultDisplay", "yes");
					if (!(ics.GetVar("AttrTypeID").equals(""))) {
						ics.SetObj("atmgr", atm.locateAssetManager("AttrTypes"));
						IAttributeTypeManager iam = (IAttributeTypeManager) ics.GetObj("atmgr");
						
						presentationObj = iam.getPresentationObject(ics.GetVar("AttrTypeID"));

					} else {
						// Assign default attribute editors 
						String defaultAttributeEditorName = "";
						String defaultAttributeEditorXML = "";
						String attrType = ics.GetVar("type");
						String eol = System.getProperty("line.separator");
						
						if ("date".equalsIgnoreCase(attrType))
							defaultAttributeEditorName = "DATEPICKER";
						else if ("asset".equalsIgnoreCase(attrType))
							defaultAttributeEditorName = "PICKASSET";
						else if ("url".equalsIgnoreCase(attrType))
							defaultAttributeEditorName = "UPLOADER";
						else 
							defaultAttributeEditorName = "TEXTFIELD";
						
						defaultAttributeEditorXML = "<?XML VERSION=\"1.0\"?>" + eol +
								   					"<!DOCTYPE PRESENTATIONOBJECT SYSTEM \"presentationobject.dtd\">" + eol +
								   					"<PRESENTATIONOBJECT NAME=\"defaultAttributeEditor\"><" + defaultAttributeEditorName + "></" + defaultAttributeEditorName + "></PRESENTATIONOBJECT>";
						

						presentationObj = new PresentationObject(ics, defaultAttributeEditorXML);
						
					}

                    //System.out.println("\tExtensiblePage.AssocAttr@Loaded..{ IAttributeTypeManager="+iam.getClass()+" }" );
					try
					{
						ics.SetObj("PresObj", presentationObj);
					}
					catch (Exception e)
					{
						XMLParseError = ics.GetVar("errno");
					}

					if (XMLParseError == null)
					{
						IPresentationObject ipo = (IPresentationObject) ics.GetObj("PresObj");
						ics.SetVar("MyAttributeType", ipo.getTypeName());

						if (ics.IsElement("OpenMarket/Gator/AttributeTypes/" + ics.GetVar("MyAttributeType")))
						{
							/* For each Presentation Object you may define an element of the same name in
							- OpenMarket/Gator/AttributeTypes. That element needs to display an edit mechanism
							- for attribute data. The name of the INPUT must be the name of the Attribute for single valued
							- Attributes. For multi valued Attributes it is the name prepended by a counter.
							- Attribute values are contained in the 'value' column of the global AttrValueList.
							- TBD: describe javascript error checking
							-
							- INPUT:
							- PresInst - instance of current Presentation Object
							- AttrName - name of current Attribute
							- AttrType - type of current Attribute
							- cs_SingleInputName - name of input for single valued and multiselect widgets
							- cs_MultipleInputName - name of input for multiple single value widgets
							- MultiValueEntry - ("yes", "no") whether this call expects you to loop
							- on all values for multi valued attributes
							- OUTPUT:
							- doDefaultDisplay - ("yes, "no") whether to display the default edit mechanism
							*/
%>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
	<ics:argument name='cs_AttrName' 	value='<%=ics.GetVar("AttrName")%>' />
	<ics:argument name='cs_IsMultiple' 	value='false' />
	<ics:argument name='cs_Varname' 	value='cs_SingleInputName' />
</ics:callelement>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
	<ics:argument name='cs_AttrName' value='<%=ics.GetVar("AttrName")%>' />
	<ics:argument name='cs_IsMultiple' value='true' />
	<ics:argument name='cs_ValueNum' value='1' />
	<ics:argument name='cs_IsMultiSelect' value='false' />
	<ics:argument name='cs_Varname' value='cs_MultipleInputName' />
</ics:callelement>

<%
							if ("url".equalsIgnoreCase(ics.GetVar("type"))) {
%>
								<hash:add name='hmyURLAttrs' value='<%=tmplattrlist.getValue("assetid")%>' />
<%
							}

							String multiValueEntry = "";
							String myAttrVal = "";
									
							if ("single".equalsIgnoreCase(ics.GetVar("EditingStyle"))) {
								multiValueEntry = "no";
								
								if (!"url".equalsIgnoreCase(ics.GetVar("type")) && null != ics.GetList("AttrValueList") && ics.GetList("AttrValueList").hasData()) {
%>
									<hash:create name='hMyAttrVal' list='AttrValueList' column='value' />
									<hash:tostring name='hMyAttrVal' varname='MyAttrVal' delim=',' />
<%
								}
								myAttrVal = ics.GetVar("MyAttrVal");
								
								if (null == myAttrVal || "Variables.MyAttrVal".equals(myAttrVal)) 
									ics.SetVar("MyAttrVal", "");
							} else {
								multiValueEntry = "yes";
%>
								<ics:callelement element='OpenMarket/Gator/AttributeTypes/ProcessValues'>
									<ics:argument name='AttrType' value='<%=ics.GetVar("type")%>' />
								</ics:callelement>
<%									
							}
%>

<ics:callelement element='<%="OpenMarket/Gator/AttributeTypes/"+ics.GetVar("MyAttributeType")%>'>
<ics:argument name='PresInst' value='PresObj' />
<ics:argument name='AttrID' value='<%=ics.GetVar("AttrID")%>' />
<ics:argument name='AttrName' value='<%=ics.GetVar("AttrName")%>' />
<ics:argument name='AttrType' value='<%=ics.GetVar("type")%>' />
<ics:argument name='AttrNumber' value='<%="A"+String.valueOf(numAttrs)+"_"%>' />
<ics:argument name='MultiValueEntry' value='<%= multiValueEntry %>' />
<ics:argument name='cs_SingleInputName' value='<%=ics.GetVar("cs_CurrentInputName")%>' />
<ics:argument name='cs_MultipleInputName' value='<%=ics.GetVar("cs_MultipleInputName")%>' />
<ics:argument name='AllowEmbeddedLinks' value='<%=ics.GetVar("AllowEmbeddedLinks")%>' />
<ics:argument name='id' value='<%=ics.GetVar("id")%>'/>
<ics:argument name='AssetType' value='<%=ics.GetVar("AssetType")%>'/>
</ics:callelement>

<%
							String requireValue = "true".equalsIgnoreCase(ics.GetVar("RequiredAttr")) ? "True" : "False";
							int attrVCValue = 0 == attrValueList.numRows() ? 1 : attrValueList.numRows();
							StringBuilder strReqInfo = new StringBuilder("");
							
							if (!Utilities.goodString(ics.GetVar("RequireInfo"))) {
								if ("single".equalsIgnoreCase(ics.GetVar("EditingStyle"))) {
									strReqInfo.append("*").append(ics.GetVar("cs_SingleInputName"))
											  .append("*").append(ics.GetVar("currentAttrNameorDesc")) 
											  .append("*Req").append(requireValue) 
											  .append("*").append(ics.GetVar("AttrType"))
											  .append("!");
								} else {
									strReqInfo = new StringBuilder(ics.GetVar("MultiValReqInfo"));
								}
								
								ics.SetVar("RequireInfo", strReqInfo.toString());
							}
%>
<% if (!"yes".equalsIgnoreCase(ics.GetVar("doDefaultDisplay"))) {%>
<INPUT TYPE="hidden" NAME='<%= ics.GetVar("AttrName") %>VC' VALUE="<%= attrVCValue %>" />
<% }%>
<%
						}
						else
						{
							ics.SetVar("XMLparseErrorforEditor", ics.GetVar("errno"));
							ics.SetVar("ErrorforEditor", "MissingAttributeEditorElement");
						}

					}
					else
					{
						ics.SetVar("XMLparseErrorforEditor", ics.GetVar("errno"));
						ics.SetVar("ErrorforEditor", "XMLparseErrorforEditor");
					}

					if (ics.GetVar("doDefaultDisplay").equals("yes"))
					{
						ics.SetVar("MyAttrVal", ics.GetVar("empty"));

						/* Determine if the attribute is single or multiple valued
							and call the presentation style to present the attributes */
						if (ics.GetVar("EditingStyle").equals("single"))
						{
							if (ics.GetVar("RequiredAttr").equals("true"))
							{
								ics.SetVar("RequireInfo", "*" + ics.GetVar("cs_CurrentInputName") + "*" + ics.GetVar("currentAttrNameorDesc") + "*ReqTrue*" + ics.GetVar("type") + "!");
							}
							else
							{
								ics.SetVar("RequireInfo", "*" + ics.GetVar("cs_CurrentInputName") + "*" + ics.GetVar("currentAttrNameorDesc") + "*ReqFalse*" + ics.GetVar("type") + "!");
							}
						}
						else
						{
							ics.SetVar("RequireInfo", ics.GetVar("empty"));
						}

						ics.SetVar("disableEdit", "false");
						if (!(tmplattrlist.getValue("editing").equals("L")))
						{
							if (ics.GetVar("AttrTypeID").equals(""))
							{
								if (tmplattrlist.getValue("storage").equals("L"))
								{
%>
<INPUT TYPE="hidden" NAME="<%=ics.GetVar("cs_CurrentInputName")%>" VALUE="<%=ics.GetVar("MyAttrVal")%>" />
<%
								}
								else
								{
									// we will skip this value on the form
									numAttrs--;
									// set up variables for parametrizing code
									// create assetset and log dependency

%>
<assetset:setasset name='as' type='<%=ics.GetVar("AssetType")%>' id='<%=ics.GetVar("id")%>' />
<assetset:getattributevalues name='as' attribute='<%=ics.GetVar("AttrName")%>' listvarname='AttrValueList' typename='<%=ics.GetVar("attributetype")%>' />
<%
								}

								ics.SetVar("disableEdit", "true");
								ics.SetVar("SpacerBar", "0");
%>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/ShowAttributesCD' />
<%
								ics.SetVar("doDefaultDisplay", "false");
%>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/ValueStyle' />
<%
							}
						}
						else
						{
%>
<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/ValueStyle' />
<%
						}
					}
					// doDefaultDisplay
				}
				else
				{
					ics.SetVar("isProductSet", "true");
					ics.SetVar("RequireInfo", "*" + ics.GetVar("cs_CurrentInputName") + 
											  "*" + ics.GetVar("currentAttrNameorDesc") +
											  "*ReqFalse" +
											  "*" + ics.GetVar("type") +
											  "!");
				}
				// Not ProductSet
%>
<INPUT TYPE="hidden" NAME="A<%=numAttrs%>_RequireInfo" VALUE="<%= ics.GetVar("RequireInfo") %>" />
<%				
			}
			ics.RemoveVar("AttrName");
		}
%>
<hash:hasdata name='hmyURLAttrs' varname='hasit' />
<%
		if (ics.GetVar("hasit").equals("true"))
		{
%>
<hash:tostring name='hmyURLAttrs' varname='myURLAttrs' delim=',' />
<input TYPE="hidden" NAME="myURLAttrs" VALUE="<%=ics.GetVar("myURLAttrs")%>" />
<%
			if (ics.GetVar("ccsourceid") != null)
			{
%>
<input TYPE="hidden" NAME="ccsourceid" VALUE="<%=ics.GetVar("ccsourceid")%>" />
<%
			}
		}

%>
<input TYPE="hidden" NAME="myPasswordAttrs" VALUE="<%=ics.GetVar("myPasswordAttrs")%>" />
<%

	} // if (tmplattrlist != null && tmplattrlist.hasData())
%>
<INPUT TYPE="hidden" NAME="numAttrs" VALUE="<%=String.valueOf(numAttrs)%>" />
<hash:hasdata name='treePickAttrs' varname='hasdata' />
<%

	if (ics.GetVar("hasdata").equals("true"))
	{
%>
<hash:tostring name='treePickAttrs' varname='treePickAttrs' delim=',' />
<INPUT TYPE="hidden" NAME="treePickAttrs" VALUE="<%=ics.GetVar("treePickAttrs")%>" />
<%
	}
	else
	{
%>
<INPUT TYPE="hidden" NAME="treePickAttrs" VALUE="none" />
<%
	}
%>
</cs:ftcs>

