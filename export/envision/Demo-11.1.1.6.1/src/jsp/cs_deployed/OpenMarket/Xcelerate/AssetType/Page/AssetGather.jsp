<%@ page import="COM.FutureTense.Interfaces.FTValList,
				 COM.FutureTense.Interfaces.IList,
				 java.util.HashSet,
				 java.util.StringTokenizer,
				 java.util.Collection,
                 com.openmarket.basic.interfaces.IListBasic,
				 java.util.Iterator,
				 com.openmarket.xcelerate.util.ConverterUtils" %>
<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="hash" uri="futuretense_cs/hash.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="flexasset" uri="futuretense_cs/flexasset.tld" %>
<%@ taglib prefix="flexgroup" uri="futuretense_cs/flexgroup.tld" %>
<%@ taglib prefix="listobject" uri="futuretense_cs/listobject.tld" %>
<%@ taglib prefix="currency" uri="futuretense_cs/currency.tld" %>
<%
//
// OpenMarket/Xcelerate/AssetType/Page/AssetGather
//
// INPUT
//   Gather all Variavles for  ExtensivePage SAVE new and/or Eddit
//   Asset. Modified to Not Gather the Flex Asset specifically No Parents or Groups
//          without Parents [asset|group]
// OUTPUT
%>

<%!

/**
 * Returns a comma separated string with out the starting and ending comma.
 * e.g. ",abc,xyz,123," => "abc,xyz,123"
 * @param s a comma separated string with the starting and ending comma.
 * @return a comma separated string with out the starting and ending comma.
 */
private static String cleanupCSString(String s)
{
	int l = s.length();
	return l <= 1 ? "" : s.substring(1, l - 1);
}

private static Collection getCollectionFromCSString(String s)
{
	if (s == null) return null;
	HashSet enabled = new HashSet();
	StringTokenizer st = new StringTokenizer(s, "[, ]");
	while (st.hasMoreTokens()) {
		enabled.add(st.nextToken());
	}
	return enabled;
}

private static boolean isID(String id) {
	if (null == id) return false;
	// The id length should not be more than 19 chars
	if (id.length() > 19) return false;
	// All the characters should be numbers.
	for (int i = 0; i < id.length(); i++) {
		if (!Character.isDigit(id.charAt(i))) return false;
	}
	return true;
}

private static final String ASSET = "asset";
private static final String FLEXASSET = "flexasset";
private static final String FLEXASSET_DEFCOL = "flextemplateid";
private static final String FLEXASSET_FIELDLIST = "name,description,flextemplateid,template,filename,path,Dimension,Dimension-parent,startdate,enddate,subtype,category";

%>

<cs:ftcs>

<%

String sFlexType = FLEXASSET;
String sFlexDefCol = "flextemplateid";
String sFieldList = FLEXASSET_FIELDLIST;
boolean bIsFlexAsset = false;


if (ics.GetVar("flextype").equals(ASSET)) {
    //bIsFlexAsset = true;
}

String assetType = ics.GetVar("AssetType") ;
String EpagetemplateType = ics.GetVar("templatetype") ;

FTValList vN = new FTValList();
vN.setValString("ASSETTYPE", ics.GetVar("AssetType"));
vN.setValString("VARNAME", "templatetype");
if (bIsFlexAsset) {
    ics.runTag("fatm.gettemplatetype", vN);
} else {
    //ics.runTag("fgtm.gettemplatetype", vN);
     ics.runTag("fatm.gettemplatetype", vN);
     EpagetemplateType = ics.GetVar("templatetype") ;
}

 ics.SetVar("templatetype", "PageDefinition");
 EpagetemplateType = ics.GetVar("templatetype") ;


vN.removeAll();
vN.setValString("ASSETTYPE", ics.GetVar("AssetType"));
vN.setValString("VARNAME", "attributetype");
if (bIsFlexAsset) {
    ics.runTag("fatm.getattributetype", vN);
} else {
    ics.runTag("fgtm.getattributetype", vN);
}


String templateType= ics.GetVar("templatetype") ;
String attributetype = ics.GetVar("attributetype") ;

if (bIsFlexAsset)
{
    if (ics.GetVar(sFlexType+"s:template")!=null) {
    	vN.removeAll();
        vN.setValString("ASSETTYPE", ics.GetVar("AssetType"));
        vN.setValString("LISTVARNAME", "lTemplates");
        vN.setValString("PUBID", ics.GetSSVar("pubid"));
        ics.runTag("templatemanager.gettemplatenames", vN);
        IList allTemplates = ics.GetList("lTemplates");
        int c = 0 ;
        do {
            if (allTemplates.getValue("tname").equals(ics.GetVar(sFlexType+"s:template"))) {
                ++c; //System.out.println("\t\t\tTemplate __t@"+c+"_@"+allTemplates.getValue("name"));
            %>
              <asset:list type='Template' list='MyTemplate' field1='name' value1='<%=allTemplates.getValue("name")%>'/> <%
            }
        } while (allTemplates.moveToRow(IList.next,0));
        IList MyTemplate = ics.GetList("MyTemplate");
        if (MyTemplate != null && MyTemplate.hasData()) {
            ics.SetVar(sFlexType+"s:renderid", MyTemplate.getValue("id"));
        }
    } else if (!ics.GetVar("updatetype").equals("remotepost")) {
       ics.SetVar(sFlexType+"s:renderid", "");
    }
}
%>

<hash:create name='refreshHash'/>

<!--have done a gather or scatter, check variables now-->
<%


if (ics.GetVar("GetOrSet").equals("set"))
{

%>
    <ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/AGHandleTempObjects'/>
	<!-- Prior to gather, call the asset relations gather element, to get data into gatherable form -->
	<ics:callelement element='OpenMarket/Xcelerate/Actions/AssetMgt/AssetChildrenFormNewGather'/>

<%

    Collection enabledFields = getCollectionFromCSString(ics.GetVar("enabledFields"));
	//Collection enabledParents = getCollectionFromCSString(ics.GetVar("enabledParents"));
	Collection enabledAttributes = getCollectionFromCSString(ics.GetVar("enabledAttributes"));

	String fieldlist = enabledFields != null ? cleanupCSString(ics.GetVar("enabledFields")) : sFieldList;

%>
	<asset:gather name='theCurrentAsset' prefix='<%=sFlexType+"s"%>' fieldlist='<%=fieldlist%>'/>
<%
    if (ics.GetErrno() != 0)
	{
%>
		<br/><xlat:stream key='dvin/FlexibleAssets/FlexAssets/AssetGatherFailed2' evalall='false'>
			<xlat:argument name='errno' value='<%=String.valueOf(ics.GetErrno())%>' />
            <xlat:argument name='id' value='<%=ics.GetVar("id")%>' />
		</xlat:stream>
		<br/><xlat:stream key='dvin/UI/ErrorDetailErrdetail1'/> <%
	}
%>

<%
	vN = new FTValList();
	vN.setValString("NAME", "theCurrentAsset");
	if (enabledAttributes == null) {
		//delete all the attributes if there is no vision control
		ics.runTag(sFlexType+".clearattributes", vN);
	} else {
        //with vision control on, only enabled attributes are clear in side the loop.
	}

	if (bIsFlexAsset)
	{
		vN = new FTValList();
		vN.setValString("NAME", "theCurrentAsset");
		vN.setValString("ID", ics.GetVar(sFlexType+":renderid"));
		ics.runTag(sFlexType+".setrendertemplate", vN);
	}
%>

	<!--  Adding Parent Groups -->
<%
	if (ics.GetVar("updatetype").equals("remotepost"))
	{
		if (ics.GetVar("sMyParentGroups")!=null && ics.GetVar("sMyParentGroups").length()!=0) {
			java.util.StringTokenizer parentgroups = new java.util.StringTokenizer(ics.GetVar("sMyParentGroups"), ";");
			while (parentgroups.hasMoreTokens()) {
				vN = new FTValList();
				vN.setValString("NAME", "theCurrentAsset");
				vN.setValString("ID", parentgroups.nextToken());
				ics.runTag(sFlexType+".addparent", vN);
			}
		}
	}

    String templatetype = ics.GetVar("templatetype") ;
    String flextempid =  ics.GetVar("flextemplateid")   ;

    // Get the Template ( Page Definition ) Manager and the definition id ..

    ics.SetVar("templateid", flextempid);

   // if (!ics.GetVar(sFlexType+"s:"+sFlexDefCol).equals(""))
	//{
	//	ics.SetVar("templateid", ics.GetVar(sFlexType+"s:"+sFlexDefCol));
	//}
	//else
	//{
		// <!-- This is a variable from remotepost -->
	//	ics.SetVar("templateid", ics.GetVar(sFlexDefCol));
//	}


    ics.SetVar("templateid", ics.GetVar(sFlexType+"s:"+sFlexDefCol));
    String templateid = ics.GetVar("templateid") ;

    if (ics.GetVar("templateid")!=null)
	{

		vN = new FTValList();
		vN.setValString("TYPE", ics.GetVar("templatetype"));
		vN.setValString("VARNAME", "pgtmgr");
		ics.runTag("atm.locate", vN);

        vN = new FTValList();
		vN.setValString("NAME", "pgtmgr");
		vN.setValString("ID", ics.GetVar("templateid"));
		vN.setValString("LISTVARNAME", "tmplattrlist");
		ics.runTag("flextemplates.getattributeinfo", vN);
		IList tmplattrlist = ics.GetList("tmplattrlist");
		ics.ClearErrno();

        if (tmplattrlist!=null && tmplattrlist.hasData())
		{
			do
			{
				if (enabledAttributes != null)
				{
					if (!enabledAttributes.contains(tmplattrlist.getValue("name")))
					{
						continue;
					}
					else
					{
						//clear the enabled attribute value

%>
						<flexasset:setattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>'/>
<%
					}
				}
				ics.SetVar("AttrTypeID","");
				ics.SetVar("myid", tmplattrlist.getValue("assetid"));
				ics.SetVar("n", tmplattrlist.getValue("name"));
				ics.SetVar("type", tmplattrlist.getValue("type"));
				ics.SetVar("EditingStyle", tmplattrlist.getValue("valuestyle"));
				// In Falcon we have only one type of multiple value i.e. Multiple ordered.
				// So setting all the multiple type to multiple ordered. 
				if ("M".equalsIgnoreCase(ics.GetVar("EditingStyle"))) 
					ics.SetVar("EditingStyle", "O");
				ics.SetVar("AttrTypeID", tmplattrlist.getValue("attributetype"));
				ics.SetVar("UploadDir", tmplattrlist.getValue("upload"));
				ics.RemoveVar("MyAttributeType");

				if (!ics.GetVar("AttrTypeID").equals(""))
				{
					vN = new FTValList();
					vN.setValString("TYPE", "AttrTypes");
					vN.setValString("VARNAME", "FAAGatmgr");
					ics.runTag("atm.locate", vN);

					vN = new FTValList();
					vN.setValString("ID", tmplattrlist.getValue("attributetype"));
					vN.setValString("OBJNAME", "FAAGPresObj");
					vN.setValString("NAME", "FAAGatmgr");
					ics.runTag("attributetypes.getpresentation", vN);

					if (ics.GetErrno()==0)
					{
						vN = new FTValList();
						vN.setValString("NAME", "FAAGPresObj");
						vN.setValString("VARNAME", "MyAttributeType");
						ics.runTag("presentation.gettype", vN);
					}
				}

				// Laziness: Instead of conditionalizing the code properly elsewhere, turn blobs into urls
				if (ics.GetVar("type").equals("blob"))
				{
					ics.SetVar("type", "url");
				}

				// for TempObjects delete by FatWireJP
				if (ics.GetVar("type").equals("url"))
				{
					String updatetype = ics.GetVar("updatetype");
					if ("remotepost".equals(updatetype))
					{
						String myURLAttrs = ics.GetVar("myURLAttrs");
						String myid = ics.GetVar("myid");
						if (myid != null && myid.length() > 0)
						{
							if (myURLAttrs == null || myURLAttrs.length() == 0)
							{
								myURLAttrs = myid;
							}
							else if (myURLAttrs.indexOf(myid) < 0)
							{
								myURLAttrs = myURLAttrs + "," + myid;
							}
							if (myURLAttrs != null && myURLAttrs.length() > 0)
							{
								ics.SetVar("myURLAttrs", myURLAttrs);
							}
						}
					}
				}
				// FatWireJP

				if (!ics.GetVar("EditingStyle").equals("S") && !ics.GetVar("EditingStyle").equals("N"))
				{
					// MULTIPLE VALUE ATTRIBUTE

					// if it is multiple values, then fetch the count and concatenate the values
					if (ics.GetVar(ics.GetVar("n")+"VC")==null)
					{
						ics.SetVar("NCounter", "1");
					}
					else
					{
						ics.SetVar("NCounter", ics.GetVar(ics.GetVar("n")+"VC"));
					}

					ics.SetCounter("TCounter", 1);

					if (ics.GetVar("type").equals("url"))
					{
%>
						<listobject:create name='myList' columns='cgi,file'/>
<%
					}
					else
					{
						if (ics.GetVar("EditingStyle").equals("O"))
						{
%>
							<listobject:create name='myList' columns='value,ordinal'/>
<%
			                        }
						else
						{
%>
							<listobject:create name='myList' columns='value'/>
<%
						}

					}

					int valueNumber = 0;
					if (ics.GetVar("MS"+ics.GetVar("n"))!=null && ics.GetVar("MS"+ics.GetVar("n")).equals("yes"))
					{
%>
			                        <ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
			                          <ics:argument name='cs_AttrName' value='<%=ics.GetVar("n")%>'/>
			                          <ics:argument name='cs_IsMultiple' value='true'/>
			                          <ics:argument name='cs_IsMultiSelect' value='true'/>
			                          <ics:argument name='cs_Varname' value='cs_CurrentInputName'/>
			                        </ics:callelement>
<%
						if (ics.GetVar(ics.GetVar("cs_CurrentInputName"))!=null)
						{
							java.util.StringTokenizer values = new java.util.StringTokenizer(ics.GetVar(ics.GetVar("cs_CurrentInputName")), ";");
							valueNumber = 1;
							while (values.hasMoreTokens())
							{
%>
				                                <ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
				                                  <ics:argument name='cs_AttrName' value='<%=ics.GetVar("n")%>'/>
				                                  <ics:argument name='cs_IsMultiple' value='true'/>
				                                  <ics:argument name='cs_ValueNum' value='<%=String.valueOf(valueNumber)%>'/>
				                                  <ics:argument name='cs_IsMultiSelect' value='false'/>
				                                  <ics:argument name='cs_Varname' value='cs_CurrentInputName'/>
				                                </ics:callelement>
<%
								ics.SetVar(ics.GetVar("cs_CurrentInputName"), values.nextToken());
								valueNumber++;
							}
							ics.SetVar("NCounter", String.valueOf(valueNumber-1));
						}
						else
						{
%>
			 				<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
                         				     <ics:argument name='cs_AttrName' value='<%=ics.GetVar("n")%>'/>
                         				     <ics:argument name='cs_IsMultiple' value='true'/>
                         				     <ics:argument name='cs_ValueNum' value='1'/>
                         				     <ics:argument name='cs_IsMultiSelect' value='false'/>
                         				     <ics:argument name='cs_Varname' value='cs_CurrentInputName'/>
                         				</ics:callelement>
<%
							ics.SetVar(ics.GetVar("cs_CurrentInputName"), "");
						}
					}

					// for multiple-ordered attrs we'll default the ordinals if none are set
					if (ics.GetVar("EditingStyle").equals("O"))
					{
						if (valueNumber == 0)
						{
							valueNumber = Integer.parseInt(ics.GetVar("NCounter"))+1;
                      				}
						String currentOrdinals = ics.GetVar("cs_"+ics.GetVar("n")+"_ordinals");
						if (currentOrdinals != null && currentOrdinals.length() != 0)
						{
							java.util.StringTokenizer ordinals = new java.util.StringTokenizer(currentOrdinals, ",");
                        				if (ordinals.countTokens() == (valueNumber-1))
							{
                              					int ordinalNumber = 1;
								while (ordinals.hasMoreTokens())
								{
									ics.SetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(ordinalNumber), String.valueOf(ordinalNumber));
									ordinalNumber++;
								}
							}
						}
						else
						{
							for (int valueCount = 1; valueCount < valueNumber; valueCount++)
							{
								ics.SetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(valueCount), String.valueOf(valueCount));
							}
						}
					}

					ics.SetCounter("deleteCount", 0);
					int nCounterValue = Integer.parseInt(ics.GetVar("NCounter"));
					String vectorName = "v" + tmplattrlist.getValue("assetid");
					boolean newFileUploaded = false;
					for (int i = 0; i < nCounterValue; i++)
					{
%>
						<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
                        			  <ics:argument name='cs_AttrName' value='<%=ics.GetVar("n")%>'/>
                        			  <ics:argument name='cs_IsMultiple' value='true'/>
                        			  <ics:argument name='cs_ValueNum' value='<%=String.valueOf(ics.GetCounter("TCounter"))%>'/>
                        			  <ics:argument name='cs_IsMultiSelect' value='false'/>
                        			  <ics:argument name='cs_Varname' value='cs_CurrentInputName'/>
                        			</ics:callelement>
<%
						if (ics.IsElement("OpenMarket/Gator/AttributeTypes/"+ics.GetVar("MyAttributeType")+"FlexAssetGather"))
						{
							// Call the attribute type's element, if there is one.
%>
							<ics:callelement element='<%="OpenMarket/Gator/AttributeTypes/"+ics.GetVar("MyAttributeType")+"FlexAssetGather"%>'>
								<ics:argument name="loopNumber" value='<%= String.valueOf(i) %>'/>
								<ics:argument name="vectorName" value='<%= vectorName %>'/>
								<ics:argument name="currAttrID" value='<%= tmplattrlist.getValue("assetid") %>'/>
							</ics:callelement>
<%
							ics.SetVar("takeDetour", "true");
							newFileUploaded = true;

						}
						else
							ics.SetVar("takeDetour", "false");
						
						if (ics.GetVar("type").equals("url") && "false".equalsIgnoreCase(ics.GetVar("takeDetour")))
						{							
							int numValues;
							if (ics.GetObj(vectorName) != null)
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								vN.setValString("VARNAME", "numValues");
								ics.runTag("vector.length", vN);
								numValues = Integer.parseInt(ics.GetVar("numValues"));
							}
							else
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								ics.runTag("vector.create", vN);
								numValues = 0;
							}

							int BLOBindex = 0;
							String filename = null;
							String attribute = ics.GetVar("cs_CurrentInputName");

							// Look to see if the current attribute has a posted file associated with it.
							if (ics.GetVar(attribute+"_file")!=null)
							{
								filename = ics.GetVar(attribute+"_file");
							}
							else
							{
								// It didn't.  Not sure why, but the logic strips off a leading "Attribute_", if
								// it is there, and tries again.

								int index = attribute.indexOf( "Attribute_" );
								if ( index > -1 )
									attribute = attribute.substring( "Attribute_".length() );
								filename = ics.GetVar( attribute + "_file");
							}

							if ( filename != null )
							{
								// New file posted!  This is for value # i

								ics.ClearErrno();

								ics.RemoveVar("tempid");

								if (isID(ics.GetVar(attribute))) {
									ics.SetVar("tempid", ics.GetVar(attribute));
								} else {
									vN = new FTValList();
									vN.setValString("FILE", ics.GetVar(attribute + "_file") );
									vN.setValString("BINVARNAME", attribute );
									vN.setValString("VARNAME", "tempid");
									ics.runTag("tempobjects.set", vN);									
								}								

								if (ics.GetVar("tempid")!=null)
								{
                                    if (numValues > ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount"))
									{
										BLOBindex = ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount");
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", String.valueOf(BLOBindex));
										vN.setValString("VARNAME", "currentTempID");
										ics.runTag("vector.get", vN);

										vN = new FTValList();
										vN.setValString("ID", ics.GetVar("currentTempID"));
										ics.runTag("tempobjects.delete", vN);

										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", String.valueOf(BLOBindex));
										ics.runTag("vector.remove", vN);
									}
									else
									{
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("VARNAME", "index");
										ics.runTag("vector.length", vN);

										BLOBindex = Integer.parseInt(ics.GetVar("index"));
									}
									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									vN.setValString("VALUE", ics.GetVar("tempid"));
									ics.runTag("vector.add", vN);
								}
								else
								{
                                    if (numValues > ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount"))
									{
										BLOBindex = ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount");
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", String.valueOf(BLOBindex));
										vN.setValString("VARNAME", "currentTempID");
										ics.runTag("vector.get", vN);

										vN = new FTValList();
										vN.setValString("ID", ics.GetVar("currentTempID"));
										ics.runTag("tempobjects.delete", vN);

										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", String.valueOf(BLOBindex));
										ics.runTag("vector.remove", vN);

                                        ics.SetCounter("deleteCount", ics.GetCounter("deleteCount")+1);
                                    }
								}
							}
							else
							{
								// no new file posted
								if (ics.GetVar("_DEL_"+ics.GetVar("cs_CurrentInputName"))!=null)
								{	
									//checking for deleted entries
									if (numValues > ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount"))
									{	//checking boundary of vector
									BLOBindex = ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount");
									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									vN.setValString("VARNAME", "currentTempID");
									ics.runTag("vector.get", vN);

									vN = new FTValList();
									vN.setValString("ID", ics.GetVar("currentTempID"));
									ics.runTag("tempobjects.delete", vN);

									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									ics.runTag("vector.remove", vN);

									ics.SetCounter("deleteCount", ics.GetCounter("deleteCount")+1);
								}
								}else if(ics.GetVar(ics.GetVar("cs_CurrentInputName")+"_hasData")!=null)
								{	
									//replacing temp object with edited object [ImageEditor Fix]
									//ics.GetVar("cs_CurrentInputName")+"_hasData" --> this variable is set in 
									//IMAGEEDITORFlexAssetGather.jsp element to check data availability
									//other editors should not get affected
								
									BLOBindex = ics.GetCounter("TCounter") - 1 - ics.GetCounter("deleteCount");
									
									//removing the existing temp object from vector and tempobjects list
									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									vN.setValString("VARNAME", "currentTempID");
									ics.runTag("vector.get", vN);

									vN = new FTValList();
									vN.setValString("ID", ics.GetVar("currentTempID"));
									ics.runTag("tempobjects.delete", vN);
									
									//adding the edited object to the vector as well as the tempobjects list
									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									ics.runTag("vector.remove", vN);

									attribute="Attribute_"+attribute;
									vN = new FTValList();
									vN.setValString("FILE", ics.GetVar(attribute+"_tfile") );
									vN.setValString("BINVARNAME", ics.GetVar("cs_CurrentInputName"));
									vN.setValString("VARNAME", "tempid");
									ics.runTag("tempobjects.set", vN);

									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("INDEX", String.valueOf(BLOBindex));
									vN.setValString("VALUE", ics.GetVar("tempid"));
									ics.runTag("vector.add", vN);
									
								}
							}
						}
						else if (ics.GetVar("type").equals("date"))
						{
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"year")!=null)
							{
								String attrvalue = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"year");
								attrvalue = attrvalue + "-"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"month");
								attrvalue = attrvalue + "-"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"day");

								String tmpvar1 = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"hour");
								if (tmpvar1==null)
								{
									attrvalue = attrvalue + " 00";
								}
								else
								{
									attrvalue = attrvalue + " " + tmpvar1;
								}

								tmpvar1 = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"min");
								if (tmpvar1==null)
								{
									attrvalue = attrvalue + ":00";
								}
								else
								{
									attrvalue = attrvalue + ":" + tmpvar1;
								}

								tmpvar1 = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"sec");
								if (tmpvar1==null)
								{
									attrvalue = attrvalue + ":00";
								}
								else
								{
									attrvalue = attrvalue + ":" + tmpvar1;
								}
%>
								<listobject:addrow name='myList'>
									<listobject:argument name='value' value='<%=attrvalue%>'/>
<%
									String sCurrentOrdinal = ics.GetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(ics.GetCounter("TCounter")));
									if (sCurrentOrdinal != null)
									{
%>
										<listobject:argument name='ordinal' value='<%=sCurrentOrdinal%>'/>
<%
									}
%>
								</listobject:addrow>
<%
							}
							else
							{
								// <!-- maybe attribute editor -->
								String tempval = ics.GetVar(ics.GetVar("cs_CurrentInputName"));
								if (tempval !=null)
					                tempval = ConverterUtils.convertDateToDBFormat(tempval, ics.GetSSVar("locale"));


								if (tempval!=null && !tempval.equals(""))
								{
%>
									<listobject:addrow name='myList'>
										<listobject:argument name='value' value='<%=tempval%>'/>
<%
										String sCurrentOrdinal = ics.GetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(ics.GetCounter("TCounter")));
										if (sCurrentOrdinal != null)
										{
%>
											<listobject:argument name='ordinal' value='<%=sCurrentOrdinal%>'/>
<%
										}
%>
									</listobject:addrow>
<%
								}
							}
						}
						else if (ics.GetVar("type").equals("money"))
						{
							String tempval = ics.GetVar(ics.GetVar("cs_CurrentInputName"));
							if (tempval!=null)
							{
%>
								<currency:create name='currency'/>
								<currency:readcurrency name='currency' value='<%=tempval%>' varname='mycurrency'/>
<%
								String mycurrency = ics.GetVar("mycurrency");
								if (mycurrency!=null && !mycurrency.equals(""))
								{
%>
									<listobject:addrow name='myList'>
										<listobject:argument name='value' value='<%=mycurrency%>'/>
<%
										String sCurrentOrdinal = ics.GetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(ics.GetCounter("TCounter")));
										if (sCurrentOrdinal != null)
										{
%>
											<listobject:argument name='ordinal' value='<%=sCurrentOrdinal%>'/>
<%
										}
%>
									</listobject:addrow>
<%
								}
							}
						}
						else
						{
							// not url, date, money
							if ("false".equalsIgnoreCase(ics.GetVar("takeDetour"))) {
							
								String tempval = ics.GetVar(ics.GetVar("cs_CurrentInputName"));
								if (tempval != null && !tempval.equals(""))
								{
	%>
									<listobject:addrow name='myList'>
									<listobject:argument name='value' value='<%=tempval%>'/>
	<%
									String sCurrentOrdinal = ics.GetVar("cs_"+ics.GetVar("n")+"_ordinal_"+String.valueOf(ics.GetCounter("TCounter")));
														if (sCurrentOrdinal != null)
									{
	%>
										<listobject:argument name='ordinal' value='<%=sCurrentOrdinal%>'/>
	<%
									}
	%>
									</listobject:addrow>
	<%
								}
							}
						}
						ics.SetCounter("TCounter", ics.GetCounter("TCounter")+1);
					} // for each value

					if (ics.GetVar("type").equals("url"))
					{
						vN = new FTValList();
						vN.setValString("NAME", vectorName);
						vN.setValString("VARNAME", "tempids");
						vN.setValString("DELIM", ",");
						ics.runTag("vector.tostring", vN);
						if (ics.GetVar("tempids")!=null && ics.GetVar("tempids").length()!=0)
						{
%>
							<listobject:create name='temp' columns='id'/>
<%
							java.util.StringTokenizer tempidTokens = new java.util.StringTokenizer(ics.GetVar("tempids"),",");
							int i = 1;
							StringBuilder tempIndexForids = new StringBuilder();
							while (tempidTokens.hasMoreTokens())
							{
								String nextToken = tempidTokens.nextToken();
								if(newFileUploaded)
								{ 
									// Prepare a string to preserve the order of iteration by keeping track of i.
									
									tempIndexForids.append(tempIndexForids.toString()).
													append( nextToken).
													append(":").
													append(i).
													append(",");
									i++;
								}
%>
								<listobject:addrow name='temp'>
									<listobject:argument name='id' value='<%=nextToken%>'/>
								</listobject:addrow>
<%
							}
%>
							<listobject:tolist name='temp' listvarname='tempids'/>
<%
							IListBasic tempidsList = (IListBasic) ics.GetList("tempids");
							if (tempidsList.numRows() > 0) {
								vN = new FTValList();
								vN.setValString("LIST", "tempids");
								vN.setValString("LISTVARNAME", "AttrValueList");
								vN.setValString("COLUMN", "urlvalue");
								ics.runTag("tempobjects.getlist", vN);							

								IListBasic attrvals = (IListBasic) ics.GetList("AttrValueList");
%>
								<ics:if condition="<%= null != attrvals && attrvals.numRows() > 0 %>">
								<ics:then>
									<ics:callelement element='OpenMarket/Gator/Util/FixBLOBList'>
										<ics:argument name='cs_ListToReplace' value='AttrValueList'/>
										<% if(newFileUploaded) {
											/*
												We use the string prepared above {tempIndexForids} to track order of iteration ,
												which is a map from ids to their respective index.
												This fix is required because the tempobjects.getlist, returns
												 a list {AttrValueList} which is different from the list { tempids }passed to it.
												 
												 eg. if   {101,109,103} is the list passed to tempobjects.getlist, it re-orders
												 to natural sort order {101,103,109}.
												 
												 Check the FixBlobList code to see how this is used.
											*/
											%>
											<ics:argument name='listOrder' value='<%=tempIndexForids.toString()%>'/>
											<%} %>
									</ics:callelement>
								</ics:then>
								</ics:if>  
<%
							}
						}
						else
						{
%>
							<listobject:tolist name='myList' listvarname='AttrValueList'/>
<%
						}
					}
					else
					{
%>
						<listobject:tolist name='myList' listvarname='AttrValueList'/>
<%
					}
%>
					<flexasset:setattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>' list='AttrValueList'/>
<%
				}
				else
				{
					// Single valued attribute!
					String vectorName = "v"+tmplattrlist.getValue("assetid");
%>
					<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
						<ics:argument name='cs_AttrName' value='<%=ics.GetVar("n")%>'/>
						<ics:argument name='cs_IsMultiple' value='false'/>
						<ics:argument name='cs_Varname' value='cs_CurrentInputName'/>
					</ics:callelement>
<%
						if (ics.IsElement("OpenMarket/Gator/AttributeTypes/"+ics.GetVar("MyAttributeType")+"FlexAssetGather"))
						{
							// Call the attribute type's element, if there is one.
%>
							<ics:callelement element='<%="OpenMarket/Gator/AttributeTypes/"+ics.GetVar("MyAttributeType")+"FlexAssetGather"%>'>
								<ics:argument name="vectorName" value='<%=vectorName%>'/>
								<ics:argument name="currAttrID" value='<%= tmplattrlist.getValue("assetid") %>'/>
								<ics:argument name="loadedAsset" value='theCurrentAsset'/>
								<ics:argument name="loopNumber" value='<%= "0" %>'/>
							</ics:callelement>
<%
							ics.SetVar("takeDetour", "true");

						}
						else
							ics.SetVar("takeDetour", "false");							

					if (ics.GetVar("type").equals("url") && "false".equalsIgnoreCase(ics.GetVar("takeDetour")))
					{
						// Single valued url or blob!

			                        //url/blob type attribute needs special handling
						//Removing the attrvalue set by previous attribute to avoid
                        			//executing the <flexasset:setsingleattribute> underneath
						ics.RemoveVar("attrvalue");
						//just for testing
						String filename = null;

						String attribute = ics.GetVar("cs_CurrentInputName");
						if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"_file")!=null)
						{
							filename = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"_file");
						}
						else
						{
							int index = attribute.indexOf( "Attribute_" );
							if ( index > -1 )
								attribute = attribute.substring( 10 );
							filename = ics.GetVar( attribute + "_file");
						}
						if ( filename != null )
						{
							vN = new FTValList();
							vN.setValString("NAME", vectorName);
							vN.setValString("VARNAME", "hasdata");
							ics.runTag("vector.hasdata", vN);
							if (ics.GetVar("hasdata").equals("true"))
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								vN.setValString("VARNAME", "currentTempID");
								vN.setValString("INDEX", "0");
								ics.runTag("vector.get", vN);

								vN = new FTValList();
								vN.setValString("ID", ics.GetVar("currentTempID"));
								ics.runTag("tempobjects.delete", vN);

								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								vN.setValString("INDEX", "0");
								ics.runTag("vector.remove", vN);
							}
							ics.RemoveVar("tempid");

							if (isID(ics.GetVar(attribute))) {
								ics.SetVar("tempid", ics.GetVar(attribute));
							} else {
								vN = new FTValList();
								vN.setValString( "BINVARNAME", attribute );
								vN.setValString( "FILE", filename );
								vN.setValString( "VARNAME", "tempid" );
								ics.runTag("tempobjects.set", vN);
							}

							if (ics.GetVar("tempid")!=null)
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								vN.setValString("VALUE", ics.GetVar("tempid"));
								ics.runTag("vector.add", vN);
								%>
								<listobject:create name='temp' columns='id'/>
								<listobject:addrow name='temp'>
									<listobject:argument name='id' value='<%=ics.GetVar("tempid")%>'/>
								</listobject:addrow>
								<listobject:tolist name='temp' listvarname='tempids'/>
								<%
								vN = new FTValList();
								vN.setValString("LISTVARNAME", "AttrValueList");
								vN.setValString("COLUMN", "urlvalue");
								vN.setValString("LIST", "tempids");
								ics.runTag("tempobjects.getlist", vN);
%>
				                                <ics:callelement element='OpenMarket/Gator/Util/FixBLOBList'>
				                                  <ics:argument name='cs_ListToReplace' value='AttrValueList'/>
				                                </ics:callelement>

								<flexasset:setattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>' list='AttrValueList'/> <%
							}
							else
							{
%>
								<flexasset:setattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>'/>
<%
							}
						}
						else
						{
							// no posted file
							if (ics.GetVar("_DEL_"+ics.GetVar("cs_CurrentInputName"))==null)
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								vN.setValString("VARNAME", "hasdata");
								ics.runTag("vector.hasdata", vN);

								if ("true".equals(ics.GetVar("hasdata")))
								{
									//replacing temp object with edited object [ImageEditor Fix]
									//ics.GetVar("cs_CurrentInputName")+"_hasData" --> this variable is set in 
									//IMAGEEDITORFlexAssetGather to check data availability
									//other editors should not get affected
									if(ics.GetVar(ics.GetVar("cs_CurrentInputName")+"_hasData")!=null){
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", "0");
										vN.setValString("VARNAME", "currentTempID");
										ics.runTag("vector.get", vN);
	
										vN = new FTValList();
										vN.setValString("ID", ics.GetVar("currentTempID"));
										ics.runTag("tempobjects.delete", vN);
										
										//adding the edited object to the vector as well as the tempobjects list
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", "0");
										ics.runTag("vector.remove", vN);
	
										vN = new FTValList();
										vN.setValString("FILE", ics.GetVar(ics.GetVar("cs_CurrentInputName")+"_tfile") );
										vN.setValString("BINVARNAME", ics.GetVar("cs_CurrentInputName"));
										vN.setValString("VARNAME", "tempid");
										ics.runTag("tempobjects.set", vN);
	
										vN = new FTValList();
										vN.setValString("NAME", vectorName);
										vN.setValString("INDEX", "0");
										vN.setValString("VALUE", ics.GetVar("tempid"));
										ics.runTag("vector.add", vN);
									}

									vN = new FTValList();
									vN.setValString("NAME", vectorName);
									vN.setValString("VARNAME", "tempids");
									vN.setValString("DELIM", ",");
									ics.runTag("vector.tostring", vN);

									java.util.StringTokenizer tempidTokens = new java.util.StringTokenizer(ics.GetVar("tempids"), ",");
%>
									<listobject:create name='temp' columns='id'/>
<%
									while (tempidTokens.hasMoreTokens())
									{
%>
										<listobject:addrow name='temp'>
											<listobject:argument name='id' value='<%=tempidTokens.nextToken()%>'/>
										</listobject:addrow>
<%
									}
%>
									<listobject:tolist name='temp' listvarname='tempids'/>
<%
									vN = new FTValList();
									vN.setValString("LISTVARNAME", "valList");
									vN.setValString("COLUMN", "urlvalue");
									vN.setValString("LIST", "tempids");
									ics.runTag("tempobjects.getlist", vN);
%>
									<ics:callelement element='OpenMarket/Gator/Util/FixBLOBList'>
                                    					  <ics:argument name='cs_ListToReplace' value='valList'/>
                                    					</ics:callelement>

									<flexasset:setattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>' list='valList'/> <%
								}
							}
							else
							{
								vN = new FTValList();
								vN.setValString("NAME", vectorName);
								ics.runTag("vector.create", vN);
							}
						}
					}
					else if (ics.GetVar("type").equals("date"))
					{
						String attrvalue = null;
						if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"year")!=null)
						{
							attrvalue = ics.GetVar(ics.GetVar("cs_CurrentInputName")+"year")+"-"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"month")+"-"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"day");
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"hour")!=null)
							{
								attrvalue = attrvalue+" "+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"hour");
							}
							else
							{
								attrvalue = attrvalue+" 00";
							}
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"min")!=null)
							{
								attrvalue = attrvalue+":"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"min");
							}
							else
							{
								attrvalue = attrvalue+":00";
							}
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName")+"sec")!=null)
							{
								attrvalue = attrvalue+":"+ics.GetVar(ics.GetVar("cs_CurrentInputName")+"sec");
							}
							else
							{
								attrvalue = attrvalue+":00";
							}
						}
						else
						{
							// <!-- maybe attribute editor -->
							attrvalue = "";
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName"))!=null)
							{
								attrvalue = ics.GetVar(ics.GetVar("cs_CurrentInputName"));
				                attrvalue = ConverterUtils.convertDateToDBFormat(attrvalue, ics.GetSSVar("locale"));
							}
						}
						ics.SetVar("attrvalue", attrvalue);
					}
					else if (ics.GetVar("type").equals("money"))
					{
						if (ics.GetVar(ics.GetVar("cs_CurrentInputName"))!=null)
						{
%>
							<currency:create name='currency'/>
							<currency:readcurrency name='currency' value='<%=ics.GetVar(ics.GetVar("cs_CurrentInputName"))%>' varname='attrvalue'/>
<%
						}
						else
						{
							ics.SetVar("attrvalue", "");
						}
					}
					else
					{
						//not url, date, money
						if ("false".equalsIgnoreCase(ics.GetVar("takeDetour"))) {
							if (ics.GetVar(ics.GetVar("cs_CurrentInputName"))!=null)
							{
								ics.SetVar("attrvalue", ics.GetVar(ics.GetVar("cs_CurrentInputName")));
							}
							else
							{
								ics.SetVar("attrvalue", "");
							}
						}	
					}
					if (ics.GetVar("attrvalue")!=null && !ics.GetVar("attrvalue").equals(""))
					{
                        String attrValue = ics.GetVar("attrvalue") ;
                        //System.out.println("\tAssetGather. <*> Tag@flexasset:setsingleattribute { attrid="+tmplattrlist.getValue("assetid")+",value:"+attrValue+" }");
%>
						<flexasset:setsingleattribute name='theCurrentAsset' id='<%=tmplattrlist.getValue("assetid")%>' value='<%=ics.GetVar("attrvalue")%>'/>
<%
						ics.SetVar("attrvalue", "");
					}
				} // single or multi valued
			}
			while (tmplattrlist.moveToRow(IList.next,0));
		} // there are attributes
	}  // GetVar("templateid")!=null
%>

	<!-- We only initialize from the asset instance once.  If this is a repost, we want
			to initialize from the posted tempobject ids. -->
<%
	// myURLAttrs is a comma-separated list of url attributes generated by going through the attributes, and posted from the edit form.
	// This code uses its existence to determine whether or not to load an attribute's url/blob data into tempobjects.
	//
	// ???
	// The issue is that all manipulations of the resulting vector are done above, so this is done too late to work properly.  Furthermore,
	// the vector is reset on every post, which is just plain wrong.  This section therefore needs to be moved above and integrated with the
	// code that sees new posted files, etc.
	//
	// There are really three cases:
	// (1) myURLAttrs has been posted, but there is no variable named for the url attribute included in that vector.  In this case, the
	//     code must get the data out of the existing attribute and put it into tempobjects.
	// (2) myURLAttrs has been posted, and there IS a variable corresponding to the url attribute.  In this case, the code
	//     should use the temp objects vector.
	//
	if (ics.GetVar("myURLAttrs")!=null)
	{
		java.util.StringTokenizer urlattrids = new java.util.StringTokenizer(ics.GetVar("myURLAttrs"), ",");
		String currentURLAttrID = null;
		vN = new FTValList();
		vN.setValString("NAME", "allTempIDs");
		ics.runTag("vector.create", vN);

		while (urlattrids.hasMoreTokens())
		{
			currentURLAttrID = urlattrids.nextToken();
			String vectorName = "v"+currentURLAttrID;
%>
			<flexasset:getattribute name='theCurrentAsset' id='<%=currentURLAttrID%>' listvarname='<%=currentURLAttrID%>'/>
<%

			vN = new FTValList();
			vN.setValString("NAME", vectorName);
			vN.setValString("VARNAME", "currentTempIDs");
			vN.setValString("DELIM", ",");
			ics.runTag("vector.tostring", vN);

			java.util.StringTokenizer tempidTokens = new java.util.StringTokenizer(ics.GetVar("currentTempIDs"), ",");
			if (!"true".equalsIgnoreCase(ics.GetVar("isReposted")) &&
					!"true".equalsIgnoreCase(ics.GetVar(currentURLAttrID + "_isValueId"))) {
				while (tempidTokens.hasMoreTokens())
				{
					vN = new FTValList();
					vN.setValString("ID", tempidTokens.nextToken());
					ics.runTag("tempobjects.delete", vN);
				}				
			}

			if (!"true".equalsIgnoreCase(ics.GetVar("isReposted")) &&
					!"true".equalsIgnoreCase(ics.GetVar(currentURLAttrID + "_isValueId"))) {
				vN = new FTValList();
				vN.setValString("LIST", currentURLAttrID);
				vN.setValString("COLUMN", "urlvalue");
				vN.setValString("LISTVARNAME", "tempids");
				ics.runTag("tempobjects.setlist", vN);
				
				vN = new FTValList();
				vN.setValString("NAME", vectorName);
				vN.setValString("LIST", "tempids");
				vN.setValString("COLUMN", "id");
				ics.runTag("vector.create", vN);	
			}
			
			vN = new FTValList();
			vN.setValString("NAME", vectorName);
			vN.setValString("VARNAME", "currentSize");
			ics.runTag("vector.length", vN);

			if (ics.GetVar("currentSize").equals("0"))
			{
%>
				<INPUT TYPE="HIDDEN" NAME="<%=currentURLAttrID%>" VALUE="noData"/>
<%
			}
			else
			{
				vN = new FTValList();
				vN.setValString("NAME", vectorName);
				vN.setValString("VARNAME", "currentTempIDs");
				vN.setValString("DELIM", ",");
				ics.runTag("vector.tostring", vN);

				vN = new FTValList();
				vN.setValString("NAME", "allTempIDs");
				vN.setValString("VALUE", ics.GetVar("currentTempIDs"));
				ics.runTag("vector.add", vN);
%>
				<INPUT TYPE="HIDDEN" NAME="<%=currentURLAttrID%>" VALUE="<%=ics.GetVar("currentTempIDs")%>"/>
<%
			}

			vN = new FTValList();
			vN.setValString("NAME", "allTempIDs");
			vN.setValString("VARNAME", "currentSize");
			ics.runTag("vector.length", vN);

			if (!ics.GetVar("currentSize").equals("0"))
			{
				vN = new FTValList();
				vN.setValString("NAME", "allTempIDs");
				vN.setValString("VARNAME", "currentTempIDs");
				vN.setValString("DELIM", ",");
				ics.runTag("vector.tostring", vN);

				ics.SetVar("__TEMPOBJECTS__", ics.GetVar("currentTempIDs"));
%>
				<INPUT TYPE="HIDDEN" NAME="__TEMPOBJECTS__" VALUE="<%=ics.GetVar("currentTempIDs")%>"/>
<%
			}
		}
	}
}
%>
</cs:ftcs>
