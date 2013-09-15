package demo.content.attrtypes;
    
import java.util.List;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("demo/contents.txt")
public class AttrTypes1351275812113 extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
    	data.getAttributeData("Publist").setDataAsList(list(getSite()));
		data.getAttributeData("createdby").setData("fwadmin");
		data.getAttributeData("description").setData("CkEditor200x200");
		data.getAttributeData("createddate").setData(date("2013-06-28 13:19:50.106"));
		data.getAttributeData("updatedby").setData("fwadmin");
		data.getAttributeData("status").setData("PL");
		data.getAttributeData("name").setData("CkEditor200x200");
		data.getAttributeData("updateddate").setData(date("2013-06-10 05:28:35.665"));
		data.getAttributeData("urlxml").setData(blob("703\\622/1351275793617.txt",
"<?XML VERSION=\"1.0\"?>\r\n"+
"<PRESENTATIONOBJECT NAME=\"ckeditor\">\r\n"+
"<CKEDITOR WIDTH=\"200px\" HEIGHT=\"200px\" /> </PRESENTATIONOBJECT>"));
		data.getAttributeData("fw_uid").setData("c34deb4f-0be5-47aa-a5d4-ccb86e59794d");
	}
    
	public static AssetSetup setup() {
		return new AttrTypes1351275812113();
	}

	public AttrTypes1351275812113() {
		super(1351275812113l, "AttrTypes", "", "CkEditor200x200");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list("createdby","description","Publist","createddate","updatedby","status","name","updateddate","urlxml","fw_uid");
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) list();
	}
    
}
