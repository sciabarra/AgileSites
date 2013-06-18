package com.apple.fatwire.assetapi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

public class CodeTest extends Test {

	private AttributeData templateStruct(String tid) {

		Map<String, AttributeData> map = new HashMap<String, AttributeData>();

		// map.put("required", attrData("required", isRequired));
		// map.put("multiple", attrData("multiple", isMultiple));
		// map.put("assetid",
		// attrData("assetid", AttributeTypeEnum.ASSET,
		// attribute.getAssetId()));
		String s;
		map.put(s = "resdetails1", attrData(s, "tid=" + tid));
		// map.put(s="resdetails2", attrData(s, ""));
		map.put(s = "cscacheinfo", attrData(s, "false"));
		map.put(s = "sscacheinfo", attrData(s, "false"));
		// map.put(s="description", attrData(s, ""));
		map.put(s = "csstatus", attrData(s, "live"));

		// map.put(s="siteentry", attrData(s, "tid="+tid));
		map.put(s = "elementname", attrData(s, "tid=" + tid));
		map.put(s = "url", attrData(s, "tid=" + tid));

		return attrData("Structure Element", AttributeTypeEnum.STRUCT, map);
	}

	MutableAssetData createTemplate(AssetData attr) throws AssetAccessException {
		MutableAssetData d = adm.newAssetData("Template", "");
		d.getAttributeData("Publist").setDataAsList(list("TestSite"));
		d.getAttributeData("name").setData("Parent Definition");
		// d.getAttributeData("element").setData(templateStruct() );
		return d;
	}

	@Override
	public String doIt() {
		List<AssetId> result = new LinkedList<AssetId>();
		return result.toString();
	}
}
