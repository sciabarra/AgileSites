package com.sciabarra.fatwire.assetapi;

import java.util.LinkedList;
import java.util.List;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.MutableAssetData;
import com.openmarket.xcelerate.asset.AssetIdImpl;

public class FilterTest extends Test {

	/*
	 * TO COMPLETE private AttributeDataImpl filterMap(AssetData attribute) {
	 * 
	 * Map<String, AttributeData> map = new HashMap<String, AttributeData>();
	 * AttributeDataImpl result = new AttributeDataImpl( new
	 * AttributeDefImpl("Arguments", AttributeTypeEnum.STRUCT), "Arguments",
	 * map);
	 * 
	 * AttributeDataImpl required = new AttributeDataImpl( new
	 * AttributeDefImpl("name", AttributeTypeEnum.STRING), "name", "aaa");
	 * map.put("required", required); AttributeDataImpl multiple = new
	 * AttributeDataImpl( new AttributeDefImpl("multiple",
	 * AttributeTypeEnum.STRING), "multiple", AttributeTypeEnum.STRING,
	 * isMultiple ? "true" : "false"); map.put("multiple", multiple);
	 * AttributeDataImpl asset = new AttributeDataImpl(new AttributeDefImpl(
	 * "assetid", AttributeTypeEnum.ASSET), "assetid", AttributeTypeEnum.ASSET,
	 * attribute.getAssetId()); map.put("assetid", asset);
	 * System.out.println("attributeMap=" +
	 * AssetBeanFactory.getInstance().toXML(map));
	 * 
	 * return result;
	 * 
	 * }
	 */

	MutableAssetData createFilterAsset() throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_Filter", "");
		d.setAssetId(new AssetIdImpl("APL_Filter", 1323977279516l));	
		d.getAttributeData("name").setData("TestRichTextEditor");
		d.getAttributeData("description").setData("Rich Text Editor");
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("class").setData("Field Copier");
		return d;
	}

	@Override
	public String doIt() throws Exception {
		List<AssetId> result = new LinkedList<AssetId>();

		MutableAssetData filterEdit = createFilterAsset();
		result.add(save(filterEdit));
		
		return result.toString();
	}

}