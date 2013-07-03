package com.sciabarra.fatwire.assetapi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.openmarket.xcelerate.asset.AttributeDefImpl;

public class AllTest {
	static Log log = LogFactory.getLog(AllTest.class);

	Session ses;
	AssetDataManager adm;

	public AllTest() {
		ses = SessionFactory.getSession();
		adm = (AssetDataManager) ses.getManager(AssetDataManager.class
				.getName());
	}

	public AssetId save(AssetData asset) throws AssetAccessException {
		List<AssetData> assetList = new LinkedList<AssetData>();
		assetList.add(asset);
		adm.insert(assetList);
		return asset.getAssetId();
	}

	private List<AssetId> list(AssetId... objs) {
		List<AssetId> result = new LinkedList<AssetId>();
		for (AssetId obj : objs)
			result.add(obj);
		System.out.println(result);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private AssetData find(String c, long cid) {
		AssetId aid = new AssetIdImpl(c, cid);
		Iterator it;
		try {
			it = (Iterator) adm.read(list(aid)).iterator();
			if (it.hasNext())
				return (AssetData) it.next();
		} catch (Exception e) {
			log.warn(e);
		}
		return null;
	}

	public List<AssetId> createAll() throws AssetAccessException {

		List<AssetId> result = new LinkedList<AssetId>();

		MutableAssetData attrEdit = createAttributeEditor();
		result.add(save(attrEdit));

		MutableAssetData attr = createAttribute(attrEdit.getAssetId());
		result.add(save(attr));

		// AssetData attr = find("APL_Attribute", 1325014467107l);

		System.out.println("attr=" + attr);

		MutableAssetData pd = createParentDefinition(attr);
		result.add(save(pd));

		MutableAssetData cd = createContentDefinition(attr, pd);
		result.add(save(cd));

		return result;

	}

	public String deleteAll() throws AssetAccessException {
		return "TODO";
	}

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
		// System.out.println("attributeMap="
		// + AssetBeanFactory.getInstance().toXML(map));
		return map;
	}

	private MutableAssetData createParentDefinition(AssetData attr)
			throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_ContentItem_PD", "");
		d.getAttributeData("name").setData("Parent Definition");
		d.getAttributeData("Attributes").addData(attributeMap(attr, true, 1));
		return d;
	}

	private MutableAssetData createContentDefinition(AssetData attr,
			AssetData parentDef) throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("APL_ContentItem_CD", "");
		d.getAttributeData("name").setData("Content Definition");
		d.getAttributeData("Attributes").addData(attributeMap(attr, true, 1));

		return d;
	}

	MutableAssetData createAttributeEditor() throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("AttrTypes", "");
		d.getAttributeData("name").setData("RichTextEditor");
		d.getAttributeData("description").setData("Rich Text Editor");

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
		d.getAttributeData("name").setData("RichTextAttribute");
		d.getAttributeData("type").setData("text");
		d.getAttributeData("valuestyle").setData("S");
		d.getAttributeData("editing").setData("L");
		d.getAttributeData("storage").setData("L");
		d.getAttributeData("deptype").setData("E");
		d.getAttributeData("embedtype").setData("U");
		d.getAttributeData("attributetype").setData(attrEditId);
		return d;
	}

	// TODO
	MutableAssetData createTemplate() throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("Template", "");
		d.getAttributeData("name").setData("TestTemplate");
		return d;
	}

}
