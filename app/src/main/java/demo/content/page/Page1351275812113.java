package demo.content.page;
    
import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("demo/contents.txt")
public class Page1351275812113 extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
    	data.getAttributeData("Publist").setDataAsList(list(getSite()));
		data.getAttributeData("flextemplateid").setData(ref("PageDefinition",1351275812156l));
		data.getAttributeData("Summary").setData("<div>\n\tThis is a SUMMARY.</div>\n");
		data.getAttributeData("fw_uid").setData("4adc5be5-d187-4c2b-a299-670a3011382f");
		data.getAttributeData("updateddate").setData(date("2013-06-10 05:28:36.169"));
		data.getAttributeData("status").setData("ED");
		data.getAttributeData("subtype").setData("Content");
		data.getAttributeData("updatedby").setData("fwadmin");
		data.getAttributeData("createdby").setData("fwadmin");
		data.getAttributeData("template").setData("DmContentLayout");
		data.getAttributeData("createddate").setData(date("2013-06-28 13:19:51.145"));
		data.getAttributeData("description").setData("Home Page1");
		data.getAttributeData("name").setData("Home1");
	}
    
	public static AssetSetup setup() {
		return new Page1351275812113();
	}

	public Page1351275812113() {
		super(1351275812113l, "Page", "Content1", "Home1");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list("Attribute_Summary","flextemplateid","fw_uid","updateddate","status","subtype","updatedby","createdby","template","createddate","description","name","Publist");
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) list(id("PageDefinition",1351275812156L));
	}
    
}
