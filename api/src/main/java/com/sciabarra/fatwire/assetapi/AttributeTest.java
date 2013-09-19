package com.sciabarra.fatwire.assetapi;

import java.util.LinkedList;
import java.util.List;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;
import com.openmarket.xcelerate.asset.AssetIdImpl;

public class AttributeTest extends Test {

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

	MutableAssetData createAttributeEditor() throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("AttrTypes", "");
		d.setAssetId(new AssetIdImpl("AttrTypes", 1000000000001l));	
		d.getAttributeData("name").setData("TestRichTextEditor");
		d.getAttributeData("description").setData("Rich Text Editor");
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));

		String xml = "<?xml version=\"1.0\"?>\n"
				+ "<!DOCTYPE PRESENTATIONOBJECT SYSTEM \"presentationobject.dtd\">\n"
				+ "<PRESENTATIONOBJECT NAME=\"RichText\">\n"
				+ "<FCKEDITOR XSIZE=\"580\" YSIZE=\"200\">\n"
				+ "</FCKEDITOR>\n" + "</PRESENTATIONOBJECT>";

		BlobObjectImpl blob = new BlobObjectImpl("AttrTypes",
				"richtexteditor.xml", //
				xml.getBytes());

		d.getAttributeData("urlxml").setData(blob);
		return d;
	}

	MutableAssetData createAttribute(AssetId attrEditId)
			throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_Attribute", "");
		d.setAssetId(new AssetIdImpl("APL_Attribute", 1000000000002l));	
		d.getAttributeData("name").setData("TestRichTextAttribute");
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("type").setData("text");
		d.getAttributeData("valuestyle").setData("S");
		d.getAttributeData("editing").setData("L");
		d.getAttributeData("storage").setData("L");
		d.getAttributeData("deptype").setData("E");
		d.getAttributeData("embedtype").setData("U");
		d.getAttributeData("attributetype").setData(attrEditId);
		return d;
	}

	MutableAssetData createAttribute2(AssetId attrEditId)
			throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_Attribute", "");
		d.setAssetId(new AssetIdImpl("APL_Attribute", 1000000000003l));	
		d.getAttributeData("name").setData("TestText");
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("type").setData("text");
		d.getAttributeData("valuestyle").setData("M");
		d.getAttributeData("editing").setData("L");
		d.getAttributeData("storage").setData("L");
		d.getAttributeData("deptype").setData("E");
		d.getAttributeData("embedtype").setData("U");
		return d;
	}

	@Override
	public String doIt() throws Exception {
		List<AssetId> result = new LinkedList<AssetId>();

		MutableAssetData attrEdit = createAttributeEditor();
		result.add(save(attrEdit));

		MutableAssetData attr = createAttribute(attrEdit.getAssetId());
		result.add(save(attr));

		MutableAssetData attr2 = createAttribute2(attrEdit.getAssetId());
		result.add(save(attr2));

		return result.toString();

	}

}
