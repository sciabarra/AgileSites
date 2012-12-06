<%@ taglib prefix="cs" uri="futuretense_cs/ftcs1_0.tld" %>
<%@ taglib prefix="ics" uri="futuretense_cs/ics.tld" %>
<%@ taglib prefix="satellite" uri="futuretense_cs/satellite.tld" %>
<%@ taglib prefix="listobject" uri="futuretense_cs/listobject.tld" %>
<%@ taglib prefix="asset" uri="futuretense_cs/asset.tld" %>
<%@ taglib prefix="assettype" uri="futuretense_cs/assettype.tld" %>
<%@ taglib prefix="xlat" uri="futuretense_cs/xlat.tld" %>
<%@ taglib prefix="hash" uri="futuretense_cs/hash.tld" %>
<%//
// OpenMarket/Gator/FlexibleAssets/Common/ParseTreeSelect
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
<%@ page import="java.util.HashMap"%>
<cs:ftcs>

<!-- user code here -->
<%
// First, convert the current list of recommendation ids into a hash table
java.util.HashMap currentRecMap = new java.util.HashMap();
String currentRecommendationsList = ics.GetVar("uniqueRecids");
if (currentRecommendationsList != null)
{
	java.util.StringTokenizer currentRecList = new java.util.StringTokenizer(currentRecommendationsList,",");
	while (currentRecList.hasMoreTokens())
	{
		String activeRecommendation = currentRecList.nextToken();
		currentRecMap.put(activeRecommendation,activeRecommendation);
	}
}

if (ics.GetVar("hasMarketStudio")!=null && ics.GetVar("hasMarketStudio").equals("true"))
{
	// For parsing the tree selection for recommendations, we only need to scan for selections that might have resulted
	// from pressing a "Select from tree" button that displayed on the form.
	// 
	// The issue currently is that these selections are done by "recommendation number", which is the position that the
	// recommendation occupies on the form.  That's a pretty poor way to do it, without sending the actual list of displayed
	// recommendation ID's along in a hidden form element.  Using a hidden, though, suffers from the problem that a potentially
	// large number of recommendation ID's may need to be specified in it.  I don't expect there to be huge numbers of asset local
	// recommendations, though, so I think we can safely get away with it for now.
	// If this should prove to be a bad assumption, the way the "select from tree" buttons are encoded will have to change, and then
	// this element would change accordingly.
    
	// Obviously, NOTHING need be scanned if the recommendations were not even displayed.
  
  
	String recommendationsList = ics.GetVar("recommendationsList");
	if (recommendationsList == null)
		recommendationsList = "";

	java.util.StringTokenizer reclist = new java.util.StringTokenizer(recommendationsList, ",");
	int recCounter = 0;
	while (reclist.hasMoreTokens())
	{
		String recID = reclist.nextToken();
		String treeVariableName = Integer.toString(recCounter)+"AssetStr";
		String treeVariable = ics.GetVar(treeVariableName);
		if (treeVariable!=null)
		{
			if (!treeVariable.startsWith("null"))
			{
				java.util.StringTokenizer assetlist = new java.util.StringTokenizer(treeVariable, ":");

				int numassets = 0;
				String numassetsString = ics.GetVar(recID+"numassets");
				if (numassetsString!=null)
					numassets = Integer.parseInt(numassetsString);
				String oldAssetType = ics.GetVar("AssetType");
				while (assetlist.hasMoreTokens())
				{
					// <!-- ParseTreeNodeID sets AssetType, not good... -->
%>
					<ics:callelement element='OpenMarket/Gator/UIFramework/ParseTreeNodeID'>
						<ics:argument name='TreeNodeID' value='<%=assetlist.nextToken()%>'/>
					</ics:callelement>
<%
					String idValue = ics.GetVar("ID");
					String assetTypeValue = ics.GetVar("AssetType");
			
					if (idValue!=null && !idValue.equals("0"))
					{
%>
						<asset:list list='AssetFields' type='<%=assetTypeValue%>' field1='id' value1='<%=idValue%>'/>
<%
						ics.SetVar(recID+"assetid"+numassets, idValue);
						ics.SetVar(recID+"assettype"+numassets, assetTypeValue);
						currentRecMap.put(recID,recID);
						numassets++;
					}
				}

				ics.SetVar("AssetType", oldAssetType);
				ics.SetVar(recID+"numassets", numassets);
			}
			ics.SetVar("TreeSelect", "xxx");
		}

		recCounter++;
	}
}

// Rebuild the uniqueRecids variable
StringBuffer sb = new StringBuffer();
java.util.Iterator iter = currentRecMap.keySet().iterator();
while (iter.hasNext())
{
	if (sb.length() > 0)
		sb.append(",");
	sb.append((String)iter.next());
}
ics.SetVar("uniqueRecids",sb.toString());
ics.SetVar("numberRecommendations",Integer.toString(currentRecMap.size()));

ics.SetVar("AssetStr", ics.GetVar("ParentSelect"));
if (ics.GetVar("ParentSelect")!=null)
{
	if (!ics.GetVar("AssetStr").startsWith("null"))
	{
		java.util.StringTokenizer assetlist = new java.util.StringTokenizer(ics.GetVar("AssetStr"), ":");
%>
		<listobject:create name='errors' columns='message'/>
<%
		if (assetlist.countTokens()!=0)
		{
			String oldassettype = ics.GetVar("AssetType");
			while (assetlist.hasMoreTokens())
			{
				// <!-- ParseTreeNodeID sets AssetType, not good... -->
%>
				<ics:callelement element='OpenMarket/Gator/UIFramework/ParseTreeNodeID'>
					<ics:argument name='TreeNodeID' value='<%=assetlist.nextToken()%>'/>
				</ics:callelement>
<%
				ics.SetVar("currentNodeType", ics.GetVar("AssetType"));
				ics.SetVar("AssetType", oldassettype);

				if (ics.GetVar("ID")!=null && !ics.GetVar("ID").equals("0"))
				{
%>
					<asset:list list='AssetFields' type='<%=ics.GetVar("currentNodeType")%>' field1='id' value1='<%=ics.GetVar("ID")%>'/>
<%
					IList assetfields = ics.GetList("AssetFields");
					if (ics.GetVar("id")!=null && !ics.GetVar("id").equals(ics.GetVar("ID")))
					{
						if (ics.GetVar("currentNodeType").equals(ics.GetVar("grouptype")))
						{
							// <!-- xml needs to make sure that an irrelevant group is not picked -->
							boolean ignorethisone = false;
							boolean goodparent = false;
							if (!ics.GetVar("numParents").equals("1"))
							{
								int numselectboxes = Integer.parseInt(ics.GetVar("numParents")) - 1;
								int currentselectbox = 1;

								for (int i = 0; i < numselectboxes; i++)
								{
									java.util.StringTokenizer templateinfo = new java.util.StringTokenizer(ics.GetVar("_ParentDef_"+currentselectbox+"_Info_"), ",");
									String required = templateinfo.nextToken();
									String multiple = templateinfo.nextToken();
									String defname = templateinfo.nextToken();
									String defid = templateinfo.nextToken();
									if (defid.equals(assetfields.getValue("flexgrouptemplateid")))
									{
%>
										<assettype:load name='type' type='<%=ics.GetVar("currentNodeType")%>'/>
										<assettype:scatter name='type' prefix='AssetTypeObj'/>
										<xlat:lookup key='dvin/UI/Error/assetmustbechosenfromselectbox' evalall='false' varname='_XLAT_'>
											<xlat:argument name='typeDesc' value='<%=ics.GetVar("AssetTypeObj:description")%>'/>
											<xlat:argument name='assetName' value='<%=assetfields.getValue("name")%>'/>
											<xlat:argument name='defName' value='<%=defname%>'/>
										</xlat:lookup>
										<listobject:addrow name='errors'>
											<listobject:argument name='message' value='<%=ics.GetVar("_XLAT_")%>'/>
										</listobject:addrow>
<%
										ics.SetVar("pickerrors", "true");
										ignorethisone = true;
										break;
									}
									currentselectbox++;
								}
							}
							//<!-- also make sure not more than 1 group is selected for a single valued
							//	tree pick -->
							if (!ignorethisone)
							{
								int currentselectbox = Integer.parseInt(ics.GetVar("numParents"));
								for (int i = 0; i < Integer.parseInt(ics.GetVar("numTreeTmpls")); i++)
								{
									java.util.StringTokenizer templateinfo = new java.util.StringTokenizer(ics.GetVar("_ParentDef_"+currentselectbox+"_Info_"), ",");
									String required = templateinfo.nextToken();
									String multiple = templateinfo.nextToken();
									String defname = templateinfo.nextToken();
									String defid = templateinfo.nextToken();
									if (defid.equals(assetfields.getValue("flexgrouptemplateid")))
									{
										goodparent = true;
										if (multiple.equals("false"))
										{
											String currentVal = ics.GetVar("_ParentDef_"+defname+"_HasParents_");
											if (currentVal != null && currentVal.equals("true"))
											{
%>
												<assettype:load name='type' type='<%=ics.GetVar("currentNodeType")%>'/>
												<assettype:scatter name='type' prefix='AssetTypeObj'/>
												<xlat:lookup key='dvin/UI/Error/defmayonlyhaveoneparent' evalall='false' varname='_XLAT_'>
													<xlat:argument name='typeDesc' value='<%=ics.GetVar("AssetTypeObj:description")%>'/>
													<xlat:argument name='assetName' value='<%=assetfields.getValue("name")%>'/>
													<xlat:argument name='defName' value='<%=defname%>'/>
												</xlat:lookup>
												<listobject:addrow name='errors'>
													<listobject:argument name='message' value='<%=ics.GetVar("_XLAT_")%>'/>
												</listobject:addrow>
<%
												ics.SetVar("pickerrors", "true");
												ignorethisone = true;
												goodparent = false;
											}
										}
										break;
									}
									currentselectbox++;
								}
								if (!ignorethisone)
								{
									if (!goodparent)
									{
%>
										<assettype:load name='type' type='<%=ics.GetVar("currentNodeType")%>'/>
										<assettype:scatter name='type' prefix='AssetTypeObj'/>
										<xlat:lookup key='dvin/UI/Error/namenotbasedonlegaltype' evalall='false' varname='_XLAT_'>
											<xlat:argument name='typeDesc' value='<%=ics.GetVar("AssetTypeObj:description")%>'/>
											<xlat:argument name='assetName' value='<%=assetfields.getValue("name")%>'/>
										</xlat:lookup>
										<listobject:addrow name='errors'>
											<listobject:argument name='message' value='<%=ics.GetVar("_XLAT_")%>'/>
										</listobject:addrow> <%
										ics.SetVar("pickerrors", "true");
										ignorethisone = true;
									}

									if (ics.GetVar("_ParentDef_"+ics.GetVar("numParents")+"_SelectedParents_")==null)
									{
										ics.SetVar("_ParentDef_"+ics.GetVar("numParents")+"_SelectedParents_", ics.GetVar("ID"));
									}
									else
									{
										int idIndex = (","+ics.GetVar("_ParentDef_"+ics.GetVar("numParents")+"_SelectedParents_")+",").indexOf(","+ics.GetVar("ID")+",");
										if (idIndex==-1)
										{
											ics.SetVar("_ParentDef_"+ics.GetVar("numParents")+"_SelectedParents_", ics.GetVar("_ParentDef_"+ics.GetVar("numParents")+"_SelectedParents_")+";"+ics.GetVar("ID"));
										}
									}
								}
							}
						}
						else
						{
%>
							<assettype:load name='type' type='<%=ics.GetVar("currentNodeType")%>'/>
							<assettype:scatter name='type' prefix='AssetTypeObj'/>
							<assettype:load name='type' type='<%=ics.GetVar("grouptype")%>'/>
							<assettype:scatter name='type' prefix='AssetTypeObj1'/>
							<xlat:lookup key='dvin/UI/Error/parentsmustbeofthistype' evalall='false' varname='_XLAT_'>
								<xlat:argument name='typeDesc' value='<%=ics.GetVar("AssetTypeObj:description")%>'/>
								<xlat:argument name='typeDesc2' value='<%=ics.GetVar("AssetTypeObj1:description")%>'/>
								<xlat:argument name='assetName' value='<%=assetfields.getValue("name")%>'/>
							</xlat:lookup>
							<listobject:addrow name='errors'>
								<listobject:argument name='message' value='<%=ics.GetVar("_XLAT_")%>'/>
							</listobject:addrow>
<%
							ics.SetVar("pickerrors", "true");
						}
					}
					else
					{
%>
						<xlat:lookup key='dvin/UI/Error/namecannotbeaparentofitself' evalall='false' varname='_XLAT_'>
							<xlat:argument name='assetName' value='<%=assetfields.getValue("name")%>'/>
						</xlat:lookup>
						<listobject:addrow name='errors'>
							<listobject:argument name='message' value='<%=ics.GetVar("_XLAT_")%>'/>
						</listobject:addrow>
<%
						ics.SetVar("pickerrors", "true");
					}
				}
			}
		}
	}
	ics.SetVar("TreeSelect", "xxx");
}

// This clause is apparently used to support the PICKFROMTREE attribute editor.  As such, its location is just plain wrong, but I'm not going to reorganize
// it right now.  In theory this code should be moved into PICKFROMTREEflexassetgather, or some such, so that it is local to the attribute editor in question.
//
// I've had to modify this part to support subtypes.  That means that selected asset types are filtered so that we don't select from assets that have the wrong
// such type.  I've borrowed from the parent select above to model the error reporting etc.
//
// The implementation needs two things: First, the list of attributes that need to be examined (in the treePickAttrs variable), and second, the
// set of posted form values that we need to reconcile.  The latter consists of:
//
// (1) A variable called "TP<assetid>", which gets filled with the tree item(s) that were selected;
// (2) A variable called "MS<index>", which is either "yes" or something else, which describes whether the multiple value variable has
//     been set for the current item.
// (3) A variable called "TPS<assetid>, which contains a semicolon-delimited list of the legal subtypes for the asset selection, or is empty if no restriction.
//
if (ics.GetVar("treePickAttrs")!=null && !ics.GetVar("treePickAttrs").equals("none"))
{
	java.util.StringTokenizer treepicks = new java.util.StringTokenizer(ics.GetVar("treePickAttrs"), ",");
	String currentPick = null;
	while (treepicks.hasMoreTokens())
	{
		currentPick = treepicks.nextToken();
		String currentAssetList = ics.GetVar("TP"+currentPick);
		if (currentAssetList!= null && !currentAssetList.startsWith("null"))
		{
			ics.SetVar("TreeSelect", "xxx");
			
			// Separate the picked assets individually
			java.util.StringTokenizer assetlist = new java.util.StringTokenizer(currentAssetList, ":");
			
			// Now, do the same thing for the legal subtypes, except put those into a hash table
			java.util.Map legalSubtypes = null;
			String currentSubtypeList = ics.GetVar("TPS"+currentPick);
			if (currentSubtypeList != null && currentSubtypeList.length() > 0)
			{
				legalSubtypes = new java.util.HashMap();
				java.util.StringTokenizer subtypeList = new java.util.StringTokenizer(currentSubtypeList, ";");
				while (subtypeList.hasMoreTokens())
				{
					String subtype = subtypeList.nextToken();
					legalSubtypes.put(subtype,subtype);
				}
			}
			
%>

			<asset:load name='ainst' type='<%=ics.GetVar("attributetype")%>' field='id' value='<%=currentPick%>'/>
			<asset:get name='ainst' field='name' output='n'/>
<%
			String attrAssetName = ics.GetVar("n");
			
			FTValList vN = new FTValList();
			vN.setValString("NAME", "ainst");
			vN.setValString("VARNAME", "attrassettype");
			ics.runTag("attribute.getassettype", vN);

			vN = new FTValList();
			vN.setValString("NAME", "ainst");
			vN.setValString("VARNAME", "attrstyle");
			ics.runTag("attribute.getvaluestyle", vN);
%>

			<ics:callelement element='OpenMarket/Gator/FlexibleAssets/Common/GetInputName'>
				<ics:argument name='cs_AttrName' value='<%=attrAssetName%>'/>
				<ics:argument name='cs_IsMultiple' value='true'/>
				<ics:argument name='cs_IsMultiSelect' value='true'/>
				<ics:argument name='cs_Varname' value='cs_MultipleInputName'/>
			</ics:callelement>
<%

			String msValue = ics.GetVar("MS"+attrAssetName);
			if (msValue!=null && msValue.equals("yes"))
			{
%>
				<hash:create name='goodVals'/>
<%
				String previousSelectedValues = ics.GetVar(ics.GetVar("cs_MultipleInputName"));
				if (previousSelectedValues!=null && previousSelectedValues.length()!=0)
				{
					java.util.StringTokenizer allvals = new java.util.StringTokenizer(previousSelectedValues, ";");
					while (allvals.hasMoreTokens())
					{
%>
						<hash:add name='goodVals' value='<%=allvals.nextToken()%>'/>
<%
					}
				}
			}
%>

			<listobject:create name='errors' columns='message'/>
<%
			String oldassettype = ics.GetVar("AssetType");
			while (assetlist.hasMoreTokens())
			{
				// <!-- ParseTreeNodeID sets AssetType, not good... -->
%>
                    
				<ics:callelement element='OpenMarket/Gator/UIFramework/ParseTreeNodeID'>
					<ics:argument name='TreeNodeID' value='<%=assetlist.nextToken()%>'/>
				</ics:callelement>
<%
				String currentNodeType = ics.GetVar("AssetType");
				ics.SetVar("currentNodeType", currentNodeType);
				ics.SetVar("AssetType", oldassettype);
				if (ics.GetVar("ID")!=null && !ics.GetVar("ID").equals("0"))
				{
					String attrAssetType = ics.GetVar("attrassettype");
					if (attrAssetType!=null && attrAssetType.equals(currentNodeType))
					{
						// If we're restricting on subtypes, make sure the subtype is right too
						boolean isMatchingSubtype;
						if (legalSubtypes != null)
						{
							// Get the subtype of the selected asset
%>
							<asset:getsubtype type='<%=currentNodeType%>' objectid='<%=ics.GetVar("ID")%>' output="thesubtype"/>
<%
							String thesubtype = ics.GetVar("thesubtype");
							isMatchingSubtype = (legalSubtypes.get(thesubtype) != null);
						}
						else
							isMatchingSubtype = true;
							
						if (isMatchingSubtype)
						{
%>
							<hash:contains name='goodVals' value='<%=ics.GetVar("ID")%>' varname='hasdata'/>
<%
							if (ics.GetVar("hasdata").equals("false"))
							{
								if (ics.GetVar("attrstyle").equals("single"))
								{
%>
									<hash:hasdata name='goodVals' varname='hasdata'/>
<%
									if (ics.GetVar("hasdata").equals("true"))
									{
%>
										<asset:list type='<%=currentNodeType%>' list='AssetFields' field1='id' value1='<%=ics.GetVar("ID")%>'/>
										<listobject:addrow name='errors'>
											<listobject:argument name='message' value='<%=attrAssetName+" may only have 1 value.  ("+currentNodeType+") "+ics.GetList("AssetFields").getValue("name")+" cannot also be selected."%>'/>
										</listobject:addrow>
<%
										ics.SetVar("pickerrors", "true");
									}
								}
								if (ics.GetVar("pickerrors")==null || !ics.GetVar("pickerrors").equals("true"))
								{
%>
									<hash:add name='goodVals' value='<%=ics.GetVar("ID")%>'/>
<%
								}
							}
						}
						else
						{
							// Subtype didn't match, so note a complaint and keep going
%>
							<asset:list type='<%=currentNodeType%>' list='AssetFields' field1='id' value1='<%=ics.GetVar("ID")%>'/>
							<listobject:addrow name='errors'>
								<listobject:argument name='message' value='<%="("+currentNodeType+") "+ics.GetList("AssetFields").getValue("name")+" cannot be a value of "+attrAssetName+".  Selected asset subtype ("+ics.GetVar("thesubtype")+") is not allowed."%>'/>
							</listobject:addrow>
<%
							ics.SetVar("pickerrors", "true");
						}
					}
					else
					{
%>
						<asset:list type='<%=currentNodeType%>' list='AssetFields' field1='id' value1='<%=ics.GetVar("ID")%>'/>
						<listobject:addrow name='errors'>
							<listobject:argument name='message' value='<%="("+currentNodeType+") "+ics.GetList("AssetFields").getValue("name")+" cannot be a value of "+attrAssetName+".  Values must be of the ("+ics.GetVar("attrassettype")+") type."%>'/>
						</listobject:addrow>
<%
						ics.SetVar("pickerrors", "true");
					}
				}
			}
%>
			<hash:hasdata name='goodVals' varname='hasdata'/>
<%
			if (ics.GetVar("hasdata").equals("true"))
			{
%>
				<hash:tostring name='goodVals' varname='<%=ics.GetVar("cs_MultipleInputName")%>' delim=';'/>
<%
				ics.SetVar("MS"+attrAssetName, "yes");
			}
		}
	}
}
%>

</cs:ftcs>

