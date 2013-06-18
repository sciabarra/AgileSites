	package com.apple.fatwire.assetapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.fatwire.assetapi.query.SimpleQuery;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.openmarket.xcelerate.asset.AttributeDefImpl;

public class DefinitionTest extends Test {
	private Map<String, AttributeData> attributeMap(AssetData attribute,
			boolean isRequired, int order) {

		Map<String, AttributeData> map = new HashMap<String, AttributeData>();

		AttributeDataImpl ordinal = new AttributeDataImpl(new AttributeDefImpl(
				"ordinal", AttributeTypeEnum.INT), "ordinal",
				AttributeTypeEnum.INT, new Integer(order));
		map.put("ordinal", ordinal);
		AttributeDataImpl required = new AttributeDataImpl(
				new AttributeDefImpl("required", AttributeTypeEnum.STRING),
				"required", AttributeTypeEnum.STRING, isRequired ? "true"
						: "false");
		map.put("required", required);
		AttributeDataImpl asset = new AttributeDataImpl(new AttributeDefImpl(
				"assetid", AttributeTypeEnum.ASSET), "assetid",
				AttributeTypeEnum.ASSET, attribute.getAssetId());
		map.put("assetid", asset);
		//System.out.println("attributeMap="
		//		+ AssetBeanFactory.getInstance().toXML(map));
		return map;
	}

	
	private Map<String, AttributeData> groupMap(AssetData attribute,
			boolean isRequired, boolean isMultiple) {

		Map<String, AttributeData> map = new HashMap<String, AttributeData>();

		/*
		 * AttributeDataImpl required = new AttributeDataImpl( new
		 * AttributeDefImpl("required", AttributeTypeEnum.STRING), "required",
		 * AttributeTypeEnum.STRING, isRequired ? "true" : "false");
		 * map.put("required", required);
		 */

		map.put("required", attrData("required", isRequired));

		/*
		 * AttributeDataImpl multiple = new AttributeDataImpl( new
		 * AttributeDefImpl("multiple", AttributeTypeEnum.STRING), "multiple",
		 * AttributeTypeEnum.STRING, isMultiple ? "true" : "false");
		 * map.put("multiple", multiple);
		 */
		map.put("multiple", attrData("multiple", isMultiple));

		/*
		 * AttributeDataImpl asset = new AttributeDataImpl(new AttributeDefImpl(
		 * "assetid", AttributeTypeEnum.ASSET), "assetid",
		 * AttributeTypeEnum.ASSET, attribute.getAssetId()); map.put("assetid",
		 * asset);
		 */

		map.put("assetid",
				attrData("assetid", AttributeTypeEnum.ASSET,
						attribute.getAssetId()));

		//System.out.println("attributeMap="
		//		+ AssetBeanFactory.getInstance().toXML(map));
		return map;
	}

	private MutableAssetData createParentDefinition(AssetData attr)
			throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_ContentItem_PD", "");
		d.setAssetId(new AssetIdImpl("APL_ContentItem_PD", 1000000000004l));	

		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("name").setData("TestParentDefinition");
		d.getAttributeData("Attributes").addData(attributeMap(attr, true, 1));
		return d;
	}

	private MutableAssetData createContentDefinition(AssetData text,
			AssetData richText, AssetData parentDef)
			throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_ContentItem_CD", "");
		d.setAssetId(new AssetIdImpl("APL_ContentItem_CD", 1000000000005l));	
		
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("name").setData("TestContentDefinition");
		d.getAttributeData("Attributes").addData(attributeMap(text, true, 1));
		d.getAttributeData("Attributes").addData(
				attributeMap(richText, false, 2));
		d.getAttributeData("Groups").addData(groupMap(parentDef, true, false));
		return d;
	}

	@Override
	public String doIt() throws AssetAccessException {
		List<AssetId> result = new LinkedList<AssetId>();

		AssetData richText = null;
		AssetData text = null;

		for (AssetData curr : adm.read(new SimpleQuery("APL_Attribute", "",
				null, Arrays.asList("name")))) {
			log.info(curr.getAttributeData());
			String name = curr.getAttributeData("name").getData().toString();
			if (name.equals("TestRichTextAttribute")) {
				richText = curr;
			}
			if (name.equals("TestText")) {
				text = curr;
			}
		}

		MutableAssetData pd = createParentDefinition(text);
		result.add(save(pd));

		MutableAssetData cd = createContentDefinition(text, richText, pd);
		result.add(save(cd));

		return result.toString();
	}

}
