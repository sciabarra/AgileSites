package demo.content.pagedefinition;
    
import java.util.List;

import com.fatwire.assetapi.data.AssetId;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("demo/contents.txt")
public class PageDefinition1351275812156 extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
    	data.getAttributeData("Publist").setDataAsList(list(getSite()));
		data.getAttributeData("createdby").setData("fwadmin");
		data.getAttributeData("description").setData("Content1");
		data.getAttributeData("Attributes").setDataAsList(list(
			map(
				kv("assetid", AttributeTypeEnum.ASSET, ref("PageAttribute",1351275812112L)),
				kv("required", AttributeTypeEnum.STRING, "true"),
				kv("ordinal", AttributeTypeEnum.INT, 4L)),
			map(
				kv("assetid", AttributeTypeEnum.ASSET, ref("PageAttribute",1351275812112L)),
				kv("required", AttributeTypeEnum.STRING, "true"),
				kv("ordinal", AttributeTypeEnum.INT, 4L))));
		data.getAttributeData("createddate").setData(date("2013-06-28 13:19:50.769"));
		data.getAttributeData("updatedby").setData("fwadmin");
		data.getAttributeData("status").setData("PL");
		data.getAttributeData("name").setData("Content1");
		data.getAttributeData("updateddate").setData(date("2013-06-10 05:28:36.877"));
		data.getAttributeData("fw_uid").setData("5136914e-a0f7-47d6-a014-b3c1ade5319f");
	}
    
	public static AssetSetup setup() {
		return new PageDefinition1351275812156();
	}

	public PageDefinition1351275812156() {
		super(1351275812156l, "PageDefinition", "", "Content1");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list("createdby","description","Publist","Attributes","createddate","updatedby","status","name","updateddate","fw_uid");
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) list(id("PageAttribute",1351275812112L),id("PageAttribute",1351275812112L));
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<AssetId> getParents() {
        return (List<AssetId>) list();
    }
}
