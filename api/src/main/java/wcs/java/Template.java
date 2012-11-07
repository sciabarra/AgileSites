package wcs.java;

import static wcs.java.Util.attrArray;
import static wcs.java.Util.attrBlob;
import static wcs.java.Util.attrString;
import static wcs.java.Util.attrStruct;
import static wcs.java.Util.attrStructKV;
import static wcs.java.Util.id;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.MutableAssetData;
import com.openmarket.xcelerate.common.ElementCatalogEntry;
import com.openmarket.xcelerate.common.SiteCatalogEntry;
import com.openmarket.xcelerate.interfaces.IElementCatalogEntry;
import com.openmarket.xcelerate.interfaces.ISiteCatalogEntry;

public class Template extends Asset {

	private final static Log log = new Log(Template.class);

	public Template(Long id, String name, String description, String element,
			String cscache, String sscache) {
		super(id("Template", id), "", name, description);
		this.element = element;
		this.cscache = cscache;
		this.sscache = sscache;
	}

	private String element;

	private String cscache;

	private String sscache;

	public String getElement() {
		return element;
	}

	public String getCscache() {
		return cscache;
	}

	public String getSscache() {
		return sscache;
	}

	// <attribute name="rootelement"><string value="/fpLayout"/></attribute>
	// data.getAttributeData("rootelement").setData(element);

	// <attribute name="category"><string value="page"/></attribute>
	// data.getAttributeData("category").setData("page");

	// <attribute name="ttype"><string value="x"/></attribute>
	// data.getAttributeData("ttype").setData("x");

	// <attribute name="element"><array>
	// <struct>
	// <field name="elementname"><string value="/fpLayout"/></field>
	// <field name="url"><file name="Typeless/fpLayout.jsp">
	// <![CDATA[PCUtLQ0KQ29weXJpZ2h0IChDKSAyMDExIGJ5IE1pY2hlbGUgU2NpYWJhcnJhJw0KDQpQZXJtaXNzaW9uIGlzIGhlcmVieSBncmFudGVkLCBmcmVlIG9mIGNoYXJnZSwgdG8gYW55IHBlcnNvbiBvYnRhaW5pbmcgYSBjb3B5DQpvZiB0aGlzIHNvZnR3YXJlIGFuZCBhc3NvY2lhdGVkIGRvY3VtZW50YXRpb24gZmlsZXMgKHRoZSAiU29mdHdhcmUiKSwgdG8gZGVhbA0KaW4gdGhlIFNvZnR3YXJlIHdpdGhvdXQgcmVzdHJpY3Rpb24sIGluY2x1ZGluZyB3aXRob3V0IGxpbWl0YXRpb24gdGhlIHJpZ2h0cw0KdG8gdXNlLCBjb3B5LCBtb2RpZnksIG1lcmdlLCBwdWJsaXNoLCBkaXN0cmlidXRlLCBzdWJsaWNlbnNlLCBhbmQvb3Igc2VsbA0KY29waWVzIG9mIHRoZSBTb2Z0d2FyZSwgYW5kIHRvIHBlcm1pdCBwZXJzb25zIHRvIHdob20gdGhlIFNvZnR3YXJlIGlzDQpmdXJuaXNoZWQgdG8gZG8gc28sIHN1YmplY3QgdG8gdGhlIGZvbGxvd2luZyBjb25kaXRpb25zOg0KDQpUaGUgYWJvdmUgY29weXJpZ2h0IG5vdGljZSBhbmQgdGhpcyBwZXJtaXNzaW9uIG5vdGljZSBzaGFsbCBiZSBpbmNsdWRlZCBpbg0KYWxsIGNvcGllcyBvciBzdWJzdGFudGlhbCBwb3J0aW9ucyBvZiB0aGUgU29mdHdhcmUuDQoNClRIRSBTT0ZUV0FSRSBJUyBQUk9WSURFRCAiQVMgSVMiLCBXSVRIT1VUIFdBUlJBTlRZIE9GIEFOWSBLSU5ELCBFWFBSRVNTIE9SDQpJTVBMSUVELCBJTkNMVURJTkcgQlVUIE5PVCBMSU1JVEVEIFRPIFRIRSBXQVJSQU5USUVTIE9GIE1FUkNIQU5UQUJJTElUWSwNCkZJVE5FU1MgRk9SIEEgUEFSVElDVUxBUiBQVVJQT1NFIEFORCBOT05JTkZSSU5HRU1FTlQuIElOIE5PIEVWRU5UIFNIQUxMIFRIRQ0KQVVUSE9SUyBPUiBDT1BZUklHSFQgSE9MREVSUyBCRSBMSUFCTEUgRk9SIEFOWSBDTEFJTSwgREFNQUdFUyBPUiBPVEhFUg0KTElBQklMSVRZLCBXSEVUSEVSIElOIEFOIEFDVElPTiBPRiBDT05UUkFDVCwgVE9SVCBPUiBPVEhFUldJU0UsIEFSSVNJTkcgRlJPTSwNCk9VVCBPRiBPUiBJTiBDT05ORUNUSU9OIFdJVEggVEhFIFNPRlRXQVJFIE9SIFRIRSBVU0UgT1IgT1RIRVIgREVBTElOR1MgSU4NClRIRSBTT0ZUV0FSRS4NCiANCi0tJT48JUAgdGFnbGliIHByZWZpeD0iY3MiIHVyaT0iZnV0dXJldGVuc2VfY3MvZnRjczFfMC50bGQiJT48JUAgdGFnbGliDQoJcHJlZml4PSJhc3NldCIgdXJpPSJmdXR1cmV0ZW5zZV9jcy9hc3NldC50bGQiJT48JUAgdGFnbGliDQoJcHJlZml4PSJhc3NldHNldCIgdXJpPSJmdXR1cmV0ZW5zZV9jcy9hc3NldHNldC50bGQiJT48JUAgdGFnbGliDQoJcHJlZml4PSJjb21tZXJjZWNvbnRleHQiIHVyaT0iZnV0dXJldGVuc2VfY3MvY29tbWVyY2Vjb250ZXh0LnRsZCIlPjwlQCB0YWdsaWINCglwcmVmaXg9ImljcyIgdXJpPSJmdXR1cmV0ZW5zZV9jcy9pY3MudGxkIiU+PCVAIHRhZ2xpYg0KCXByZWZpeD0ibGlzdG9iamVjdCIgdXJpPSJmdXR1cmV0ZW5zZV9jcy9saXN0b2JqZWN0LnRsZCIlPjwlQCB0YWdsaWINCglwcmVmaXg9InJlbmRlciIgdXJpPSJmdXR1cmV0ZW5zZV9jcy9yZW5kZXIudGxkIiU+PCVAIHRhZ2xpYg0KCXByZWZpeD0ic2l0ZXBsYW4iIHVyaT0iZnV0dXJldGVuc2VfY3Mvc2l0ZXBsYW4udGxkIiU+PCVAIHRhZ2xpYg0KCXByZWZpeD0ic2VhcmNoc3RhdGUiIHVyaT0iZnV0dXJldGVuc2VfY3Mvc2VhcmNoc3RhdGUudGxkIiU+PCVAIHBhZ2UNCglpbXBvcnQ9IkNPTS5GdXR1cmVUZW5zZS5JbnRlcmZhY2VzLiosDQogICAgICAgICAgICAgICAgICAgQ09NLkZ1dHVyZVRlbnNlLlV0aWwuZnRNZXNzYWdlLA0KICAgICAgICAgICAgICAgICAgIENPTS5GdXR1cmVUZW5zZS5VdGlsLmZ0RXJyb3JzIiU+DQo8Y3M6ZnRjcz48IURPQ1RZUEUgaHRtbCBQVUJMSUMgIi0vL1czQy8vRFREIFhIVE1MIDEuMCBTdHJpY3QvL0VOIiAiaHR0cDovL3d3dy53My5vcmcvVFIveGh0bWwxL0RURC94aHRtbDEtc3RyaWN0LmR0ZCI+DQo8aHRtbCB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94aHRtbCI+PCUtLSAvZnBMYXlvdXQNCiANCiBGYXRQaG9uZSBsYXlvdXQNCg0KSU5QVVQNCg0KT1VUUFVUDQoNCi0tJT48JS0tIFJlY29yZCBkZXBlbmRlbmNpZXMgZm9yIHRoZSBUZW1wbGF0ZSAtLSU+DQo8aWNzOmlmIGNvbmRpdGlvbj0nPCU9aWNzLkdldFZhcigidGlkIikhPW51bGwlPic+PGljczp0aGVuPjxyZW5kZXI6bG9nZGVwIGNpZD0nPCU9aWNzLkdldFZhcigidGlkIiklPicgYz0iVGVtcGxhdGUiIC8+PC9pY3M6dGhlbj48L2ljczppZj4NCjxoZWFkPg0KCTxtZXRhIGNvbnRlbnQ9InllcyIgbmFtZT0iYXBwbGUtbW9iaWxlLXdlYi1hcHAtY2FwYWJsZSIgLz4NCgk8bWV0YSBjb250ZW50PSJ0ZXh0L2h0bWw7IGNoYXJzZXQ9dXRmLTgiIGh0dHAtZXF1aXY9IkNvbnRlbnQtVHlwZSIgLz4NCgk8bWV0YQ0KCQljb250ZW50PSJtaW5pbXVtLXNjYWxlPTEuMCwgd2lkdGg9ZGV2aWNlLXdpZHRoLCBtYXhpbXVtLXNjYWxlPTAuNjY2NywgdXNlci1zY2FsYWJsZT1ubyINCgkJbmFtZT0idmlld3BvcnQiIC8+DQoJPGxpbmsgaHJlZj0iL2ZwL0ZyYW1ld29yay9jc3Mvc3R5bGUuY3NzIiByZWw9InN0eWxlc2hlZXQiIG1lZGlhPSJzY3JlZW4iDQoJCXR5cGU9InRleHQvY3NzIiAvPg0KCTxzY3JpcHQgc3JjPSIvZnAvRnJhbWV3b3JrL2phdmFzY3JpcHQvZnVuY3Rpb25zLmpzIiB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiPjwvc2NyaXB0Pg0KDQo8YXNzZXQ6bG9hZCBuYW1lPSJjdXJyZW50QXNzZXQiIA0KICB0eXBlPSc8JT1pY3MuR2V0VmFyKCJjIikgJT4nDQogIG9iamVjdGlkPSc8JT1pY3MuR2V0VmFyKCJjaWQiKSAlPicvPg0KPGFzc2V0OmdldCBuYW1lPSJjdXJyZW50QXNzZXQiIGZpZWxkPSJuYW1lIiBvdXRwdXQ9InRpdGxlIi8+DQo8JSBpZihpY3MuR2V0VmFyKCJ0aXRsZSIpLnN0YXJ0c1dpdGgoImZwIikpDQoJIGljcy5TZXRWYXIoInRpdGxlIiwgaWNzLkdldFZhcigidGl0bGUiKS5zdWJzdHJpbmcoMikpOw0KIC8vVE9ETyBzcGxpdCBjYW1lbCBjYXNlIGluIHNlcGFyYXRlIHdvcmRzDQolPg0KCTx0aXRsZT48JT1pY3MuR2V0VmFyKCJ0aXRsZSIpICU+PC90aXRsZT4NCgk8JS0tIA0KCTxtZXRhIGNvbnRlbnQ9ImtleXdvcmQxLGtleXdvcmQyLGtleXdvcmQzIiBuYW1lPSJrZXl3b3JkcyIgLz4NCgk8bWV0YSBjb250ZW50PSJEZXNjcmlwdGlvbiBvZiB5b3VyIHBhZ2UiIG5hbWU9ImRlc2NyaXB0aW9uIiAvPg0KCS0tJT4NCjwvaGVhZD4NCjxyZW5kZXI6Y2FsbHRlbXBsYXRlIA0KICBzaXRlPSc8JT1pY3MuR2V0VmFyKCJzaXRlIiklPicNCiAgdGlkPSc8JT1pY3MuR2V0VmFyKCJ0aWQiKSU+JyANCiAgYz0nPCU9aWNzLkdldFZhcigiYyIpJT4nIA0KICBjaWQ9JzwlPWljcy5HZXRWYXIoImNpZCIpJT4nDQogIHRuYW1lPSdmcEJvZHknIA0KICBzbG90bmFtZT0nZnBCb2R5Jz4NCgkgPHJlbmRlcjphcmd1bWVudCBuYW1lPSJwIiANCgkgICB2YWx1ZT0nPCU9IGljcy5HZXRWYXIoInAiKSAlPicvPg0KPC9yZW5kZXI6Y2FsbHRlbXBsYXRlPg0KPC9odG1sPg0KPC9jczpmdGNzPg0K]]></file>
	// </field>
	// </struct>
	// </array>
	// </attribute>

	@Override
	List<String> getAttributes() {
		return Util.listString("name", "description", "category",
				"rootelement", "element", "ttype", "pagecriteria", "acl",
				"applicablesubtypes", "Thumbnail");
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void setData(MutableAssetData data) {

		// log.info(Util.dump(data));

		final String body = "<b>" + element + "</b>";
		final AttributeData blob = attrBlob("url", element, body);

		HashMap mapElement = new HashMap<String, Object>();

		mapElement.put("elementname", attrString("elementname", element));
		mapElement.put("description", attrString("description", element));
		mapElement.put("resdetails1",
				attrString("resdetails1", "tid=" + getId().id));
		mapElement.put("resdetails2", attrString("resdetails2", "agilewcs=1"));
		mapElement.put("csstatus", attrString("csstatus", "live"));
		mapElement.put("cscacheinfo", attrString("cscacheinfo", "false"));
		mapElement.put("sscacheinfo", attrString("sscacheinfo", "false"));
		mapElement.put("url", blob);

		HashMap mapSiteEntry = new HashMap<String, Object>();
		mapSiteEntry.put("pagename", attrString("pagename", element));
		mapSiteEntry.put(
				"defaultarguments", //
				attrArray(
						"defaultarguments", //
						attrStructKV("site", "Test"),
						attrStructKV("rendermode", "live")));
		mapElement.put(
				"siteentry",
				attrArray("siteentry",
						attrStruct("Structure siteentry", mapSiteEntry)));

		data.getAttributeData("category").setData("banr");

		data.getAttributeData("rootelement").setData(element);

		data.getAttributeData("element").setData(
				Util.list(attrStruct("Structure Element", mapElement)));

		data.getAttributeData("ttype").setData("x");

		data.getAttributeData("pagecriteria").setDataAsList(
				Util.listString("c", "cid", "context", "p", "rendermode",
						"site", "sitepfx", "ft_ss"));

		data.getAttributeData("acl").setDataAsList(Util.listString(""));

		data.getAttributeData("applicablesubtypes").setData("*");

		// data.getAttributeData("urlexternaldoc").setData(blob.getData());
		// data.getAttributeData("urlexternaldocxml").setData(blob.getData());

	
		//_populateElement(data);
	}

	private void _populateElement(AssetData data) {
		if (null != data) {
			AttributeData d = data.getAttributeData("element");
			List structureElements = d.getDataAsList();
			int size = null != structureElements ? structureElements.size() : 0;
			IElementCatalogEntry elems[] = new IElementCatalogEntry[size];
			int i = 0;
			List acls = data.getAttributeData("acl").getDataAsList();
			String acl = "";
			if (acls.size() > 0)
				acl = (String) acls.get(0);
			List pagecriteria = data.getAttributeData("pagecriteria")
					.getDataAsList();
			for (Iterator i$ = structureElements.iterator(); i$.hasNext();) {
				log.debug("#### in element loop");
				AttributeData structureElement = (AttributeData) i$.next();
				Map structureMap = (Map) structureElement.getData();
				String elementname = (String) ((AttributeData) structureMap
						.get("elementname")).getData();
				log.debug("#### elementname " + elementname);

				IElementCatalogEntry elem = new ElementCatalogEntry(null,
						elementname);
				AttributeData descr = (AttributeData) structureMap
						.get("description");
				log.debug("#### descr " + descr);
				elem.setDescription(descr == null ? null : (String) descr
						.getData());
				AttributeData res1 = (AttributeData) structureMap
						.get("resdetails1");
				log.debug("#### res1 " + res1.getData());
				elem.setResDetails1(res1 == null ? null : (String) res1
						.getData());
				AttributeData res2 = (AttributeData) structureMap
						.get("resdetails2");
				log.debug("#### res2 " + res2.getData());
				elem.setResDetails2(res2 == null ? null : (String) res2
						.getData());
				BlobObject blob = (BlobObject) ((AttributeData) structureMap
						.get("url")).getData();
				elem.setUrlSpec(blob.getFilename());
				StringBuilder fileData = new StringBuilder();
				InputStream st = blob.getBinaryStream();
				if (null != st)
					try {
						byte bytes[] = new byte[st.available()];
						st.read(bytes);
						fileData.append(new String(bytes));
						st.close();
						log.debug("########" + new String(bytes));
					} catch (IOException e) {
					}
				elem.setUrlFileData(fileData.toString());
				AttributeData cscacheinfoData = (AttributeData) structureMap
						.get("cscacheinfo");
				String cscacheinfo = cscacheinfoData == null ? null
						: (String) cscacheinfoData.getData();
				AttributeData sscacheinfoData = (AttributeData) structureMap
						.get("sscacheinfo");
				String sscacheinfo = sscacheinfoData == null ? null
						: (String) sscacheinfoData.getData();
				AttributeData csstatusData = (AttributeData) structureMap
						.get("csstatus");
				String csstatus = csstatusData == null ? null
						: (String) csstatusData.getData();

				List siteentries = (List) ((AttributeData) structureMap
						.get("siteentry")).getData();

				ISiteCatalogEntry sEntry;
				System.out.println("###1 " + structureMap.get("siteentry"));
				System.out.println("###2 " + siteentries);
				for (Iterator iii$ = siteentries.iterator(); iii$.hasNext(); /*
																			 * templateInstance
																			 */) {
					AttributeData siteentry = (AttributeData) iii$.next();
					sEntry = new SiteCatalogEntry();
					Map structureSiteEntry = (Map) siteentry.getData();
					String pagename = (String) ((AttributeData) structureSiteEntry
							.get("pagename")).getData();
					List args = (List) ((AttributeData) structureSiteEntry
							.get("defaultarguments")).getData();
					Map defaultArgs = new HashMap();
					String name;
					String value;
					for (Iterator ii$ = args.iterator(); ii$.hasNext(); defaultArgs
							.put(name, value)) {
						AttributeData arg = (AttributeData) ii$.next();
						HashMap map = (HashMap) arg.getData();
						name = (String) ((AttributeData) map.get("name"))
								.getData();
						value = (String) ((AttributeData) map.get("value"))
								.getData();
					}

					sEntry.setPagename(pagename);
					sEntry.setRootelement(elementname);
					sEntry.set("cscacheinfo", cscacheinfo);
					sEntry.set("sscacheinfo", sscacheinfo);
					sEntry.setCsstatus(csstatus);
					sEntry.setAcl(acl);
					sEntry.getPageData().setPageCriteria(pagecriteria);
					sEntry.getPageData().setDefaultArguments(defaultArgs);
				}

				elems[i] = elem;
				i++;
			}

			// templateInstance.setElementEntries(elems);
		}
	}

	/*
	 * private void _populate(IAsset asset, AssetData data, boolean
	 * filterNoSetAttributes, List ignoreAttributes) throws AssetAccessException
	 * { AssetTypeDef adef = data.getAssetTypeDef(); List attributes; if (null
	 * != adef) attributes = adef.getAttributeDefs(); //else //attributes =
	 * _getAssetTypeDef(data.getAssetId().getType(),
	 * //AssetUtil.getSubtype(_ics, data.getAssetId())) //.getAttributeDefs();
	 * boolean isFlex = false; //AssetUtil.isFlexAsset(_ics, data.getAssetId()
	 * //.getType()); Iterator i$ = attributes.iterator(); do { if
	 * (!i$.hasNext()) break; AttributeDef def = (AttributeDef) i$.next();
	 * String name = def.getName(); boolean required = def.isDataMandatory(); if
	 * ( !"id".equals(name) && !"fw_uid".equals(name) && (!isFlex ||
	 * def.isMetaDataAttribute())) if ("Publist".equals(name))
	 * //populatePublist(asset, data); ; else if ("Dimension".equals(name)) {
	 * DimensionableAssetInstance dimAsset = (DimensionableAssetInstance) asset;
	 * Object attributeData = data.getAttributeData("Dimension") .getData();
	 * List dimensions = new ArrayList(); Object _dimensions = null !=
	 * attributeData ? attributeData : ((Object) (Collections.emptyList())); if
	 * ((_dimensions instanceof Collections) || (_dimensions instanceof
	 * Collection)) dimensions.addAll((Collection) _dimensions); else if
	 * (_dimensions instanceof Dimension) dimensions.add((Dimension)
	 * _dimensions); dimAsset.setDimensions(dimensions); } else if
	 * ("Dimension-parent".equals(name)) { DimensionableAssetInstance dimAsset =
	 * (DimensionableAssetInstance) asset; Collection parents = (Collection)
	 * data.getAttributeData( "Dimension-parent").getData();
	 * dimAsset.setDimensionableAssetParents(((Collection) (null != parents ?
	 * parents : ((Collection) (Collections.emptyList()))))); } else if
	 * ("SegRating".equalsIgnoreCase(name)) { AttributeData aData =
	 * data.getAttributeData(name); List arl = new ArrayList(); if (null !=
	 * aData) { List structureMapping = aData.getDataAsList(); if (null !=
	 * structureMapping) { String key; String in; String out; for (Iterator i$ =
	 * structureMapping.iterator(); i$ .hasNext(); arl .add(new
	 * com.openmarket.xcelerate.common.AssetRatingValues.AssetRating( new
	 * AssetRatingValues(), key, in, out))) { AttributeData mappingData =
	 * (AttributeData) i$ .next(); Map m = (Map) mappingData.getData(); key =
	 * (String) ((AttributeData) m.get("SegId")) .getData(); in = (String)
	 * ((AttributeData) m .get("inRating")).getData(); out = (String)
	 * ((AttributeData) m .get("outRating")).getData(); }
	 * 
	 * } } ((Asset) asset).setRatings(arl); } else if
	 * ("fwtags".equalsIgnoreCase(name)) { AttributeData tags =
	 * data.getAttributeData("fwtags"); ((Asset) asset).setTags((List) (tags !=
	 * null ? tags .getData() : Collections.emptyList())); } else if
	 * ("flextemplateid".equalsIgnoreCase(name) ||
	 * "flexgrouptemplateid".equalsIgnoreCase(name)) { AttributeData aData =
	 * data.getAttributeData(name); Object value = aData.getData(); if (null !=
	 * value) asset.Set(name, Long.toString(((AssetId) value).getId())); } else
	 * if (isFlex && "Relationships".equals(name) && def.isMetaDataAttribute())
	 * { _populateRelationShips(data.getAttributeData(name), (BaseFlexInstance)
	 * asset); } else { AttributeData aData = data.getAttributeData(name); if
	 * (null != aData) { Object value = aData.getData(); String sValue = null;
	 * if (null != value) { if (def.getProperties() .getValueCount()
	 * .equals(com.
	 * fatwire.assetapi.def.AttributeDefProperties.ValueCount.SINGLE) ||
	 * def.getProperties() .getValueCount()
	 * .equals(com.fatwire.assetapi.def.AttributeDefProperties
	 * .ValueCount.SINGLEUNIQUE)) { if (def.getType()
	 * .equals(AttributeTypeEnum.DATE)) { sValue = Util.formatJdbcDate((Date)
	 * value); asset.Set(name, sValue); } else if (def.getType().equals(
	 * AttributeTypeEnum.URL) || def.getType().equals( AttributeTypeEnum.BLOB))
	 * { BlobObject b = (BlobObject) value; try { InputStream s =
	 * b.getBinaryStream(); if (null != s) { byte bytes[] = new byte[s
	 * .available()]; s.read(bytes, 0, bytes.length); asset.Set(name, bytes);
	 * asset.Set( (new StringBuilder()) .append(name) .append("_file")
	 * .toString(), Utilities.fileFromSpec(b .getFilename())); asset.Set( (new
	 * StringBuilder()) .append(name) .append("_folder") .toString(),
	 * Utilities.pathFromSpec(b .getFilename())); s.close(); } else if (null !=
	 * b.getFilename()) { asset.Set(name, ""); asset.Set( (new StringBuilder())
	 * .append(name) .append("_file") .toString(), Utilities.fileFromSpec(b
	 * .getFilename())); asset.Set( (new StringBuilder()) .append(name)
	 * .append("_folder") .toString(), Utilities.pathFromSpec(b
	 * .getFilename())); } } catch (IOException e) { throw new
	 * AssetAccessException( (new StringBuilder())
	 * .append("Error reading binary data for attribute ") .append(name)
	 * .toString()); } } else if (def.getType().equals(
	 * AttributeTypeEnum.ASSET)) { asset.Set( name, null != value ? ((Number)
	 * (Long .valueOf(((AssetId) value) .getId()))) : null); } else { sValue =
	 * value.toString(); asset.Set(name, sValue); } } else { List values =
	 * aData.getDataAsList(); if (required && (null == values || values.size()
	 * == 0)) throw new AssetAccessException( (new StringBuilder())
	 * .append("Field ") .append(name) .append(" is required for AssetType ")
	 * .append(data.getAssetId() .getType()) .append(" and can not be null")
	 * .toString()); if (def.getType() .equals(AttributeTypeEnum.DATE)) {
	 * StringBuilder builder = new StringBuilder(); Object v; for (Iterator i$ =
	 * values.iterator(); i$ .hasNext(); builder.append(
	 * Util.formatJdbcDate((Date) v)) .append(",")) v = i$.next();
	 * 
	 * if (builder.length() > 0) sValue = builder.deleteCharAt( builder.length()
	 * - 1) .toString(); } else { StringBuilder builder = new StringBuilder();
	 * Object v; for (Iterator i$ = values.iterator(); i$ .hasNext();
	 * builder.append( v.toString()).append(",")) v = i$.next();
	 * 
	 * if (builder.length() > 0) sValue = builder.deleteCharAt( builder.length()
	 * - 1) .toString(); } asset.Set(name, sValue); } } else { if (required)
	 * throw new AssetAccessException( (new StringBuilder()) .append("Field ")
	 * .append(name) .append(" is required for AssetType ")
	 * .append(data.getAssetId() .getType()) .append(" and can not be null")
	 * .toString()); asset.Set(name, ""); } } } } while (true);
	 * //populateAssociations(asset, data); }
	 */
}
