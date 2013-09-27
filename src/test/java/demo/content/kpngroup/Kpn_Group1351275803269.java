package demo.content.kpngroup;
    
import java.util.List;

import com.fatwire.assetapi.data.AssetId;
import wcs.core.Id;
import wcs.java.AssetSetup;
import wcs.java.util.AddIndex;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;

@AddIndex("demo/contents.txt")
public class Kpn_Group1351275803269 extends AssetSetup {

    @Override
	public void setData(MutableAssetData data) {
    	data.getAttributeData("Publist").setDataAsList(list(getSite()));
		data.getAttributeData("Group_Group").setData(ref("Kpn_Group",1351275803270l));
		data.getAttributeData("flexgrouptemplateid").setData(ref("Kpn_PD",1351275803255l));
		data.getAttributeData("fw_uid").setData("8b7ba271-ba0b-4f0b-b2c4-bb25aac9ab6c");
		data.getAttributeData("updateddate").setData(date("2013-09-23 09:56:39.357"));
		data.getAttributeData("status").setData("PL");
		data.getAttributeData("subtype").setData("Group");
		data.getAttributeData("updatedby").setData("fwadmin");
		data.getAttributeData("createdby").setData("fwadmin");
		data.getAttributeData("createddate").setData(date("2013-09-23 13:50:55.273"));
		data.getAttributeData("name").setData("Factuurinfo");
		data.getAttributeData("description").setData("Factuurinfo");
	}
    
	public static AssetSetup setup() {
		return new Kpn_Group1351275803269();
	}

	public Kpn_Group1351275803269() {
		super(1351275803269l, "Kpn_Group", "Group", "Factuurinfo");
	}

    @Override
	@SuppressWarnings("unchecked")
	public List<String> getAttributes() {
		return (List<String>)list("Group_Group","flexgrouptemplateid","fw_uid","updateddate","status","subtype","updatedby","createdby","createddate","name","description");
	}

    @Override
    @SuppressWarnings("unchecked")
	public List<Id> getReferences() {
  		return (List<Id>) list(id("Kpn_Group",1351275803270L),id("Kpn_PD",1351275803255L));
	}

    @Override
    @SuppressWarnings("unchecked")
    public List<AssetId> getParents() {
        return (List<AssetId>) list();
    }
}
