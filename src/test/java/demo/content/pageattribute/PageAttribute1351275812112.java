package demo.content.pageattribute;
    
import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("demo/contents.txt")
public class PageAttribute1351275812112 extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
    	data.getAttributeData("Publist").setDataAsList(list(getSite()));
		data.getAttributeData("editing").setData("L");
		data.getAttributeData("fw_uid").setData("eeff1b67-f704-4bd4-9b6a-1d712dcea3f4");
		data.getAttributeData("updateddate").setData(date("2013-06-10 05:28:36.859"));
		data.getAttributeData("status").setData("PL");
		data.getAttributeData("attributetype").setData(ref("AttrTypes",1351275812113l));
		data.getAttributeData("updatedby").setData("fwadmin");
		data.getAttributeData("createdby").setData("fwadmin");
		data.getAttributeData("type").setData("text");
		data.getAttributeData("storage").setData("L");
		data.getAttributeData("createddate").setData(date("2013-06-28 13:19:50.168"));
		data.getAttributeData("description").setData("Summary");
		data.getAttributeData("embedtype").setData("U");
		data.getAttributeData("name").setData("Summary");
		data.getAttributeData("valuestyle").setData("S");
	}
    
	public static AssetSetup setup() {
		return new PageAttribute1351275812112();
	}

	public PageAttribute1351275812112() {
		super(1351275812112l, "PageAttribute", "", "Summary");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list("editing","fw_uid","updateddate","status","attributetype","updatedby","createdby","type","storage","createddate","description","embedtype","name","Publist","valuestyle");
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) list(id("AttrTypes",1351275812113L));
	}
    
}
