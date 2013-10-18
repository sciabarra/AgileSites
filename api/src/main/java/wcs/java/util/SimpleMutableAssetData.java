package wcs.java.util;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AssetTypeDef;
import com.fatwire.assetapi.def.AttributeDef;
import com.fatwire.assetapi.def.AttributeTypeEnum;

import java.util.*;

public class SimpleMutableAssetData implements MutableAssetData{

    Map<String, AttributeData> attributeDatas = new LinkedHashMap<String, AttributeData>();

    @Override
    public void setAttributeData(List<AttributeData> attributeDatas) {
        throw new IllegalStateException();
    }

    @Override
    public void addAttributeData(AttributeData attributeData) {
        attributeDatas.put(attributeData.getAttributeName(),attributeData);
    }

    @Override
    public void removeAttributeData(String s) {
        attributeDatas.remove(s);
    }

    @Override
    public void setParents(List<AssetId> assetIds) throws AssetAccessException {
        throw new IllegalStateException();
    }

    @Override
    public void addAssociation(String s, List<AssetId> assetIds) {
        throw new IllegalStateException();
    }

    @Override
    public void setAssociation(String s, List<AssetId> assetIds) {
        throw new IllegalStateException();
    }

    @Override
    public AssetId getAssetId() {
        throw new IllegalStateException();
    }

    @Override
    public List<String> getAttributeNames() {
        return new ArrayList<String>(attributeDatas.keySet());
    }

    @Override
    public AssetTypeDef getAssetTypeDef() {
        throw new IllegalStateException();
    }

    @Override
    public AttributeData getAttributeData(String s) {
        AttributeData data = attributeDatas.get(s);
        if (data == null) {
            data =  new SimpleAtttributeData(s);
            attributeDatas.put(s, data);
        }
        return data;
    }

    @Override
    public AttributeData getAttributeData(String s, boolean b) {
        return attributeDatas.get(s);
    }

    @Override
    public List<AttributeData> getAttributeData() {
      return new ArrayList<AttributeData>(attributeDatas.values());
    }

    @Override
    public List<AssetId> getAssociatedAssets(String s) {
        throw new IllegalStateException();
    }

    @Override
    public List<AssetId> getParents() throws AssetAccessException {
        throw new IllegalStateException();
    }

    @Override
    public List<AssetId> getImmediateParents() throws AssetAccessException {
        throw new IllegalStateException();
    }

    @Override
    public List<AssetId> getImmediateParents(String s) throws AssetAccessException {
        throw new IllegalStateException();
    }

    @Override
    public void setAssetId(AssetId assetId) {
        throw new IllegalStateException();
    }


    class SimpleAtttributeData implements AttributeData {

        String attributeName;
        Object data;
        List<Object> listdata;

        public SimpleAtttributeData(String attributeName) {
            this.attributeName = attributeName;
        }

        @Override
        public String getAttributeName() {
            return attributeName;
        }

        @Override
        public AttributeTypeEnum getType() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object getData() {
            return data;
        }

        @Override
        public AttributeDef getAttributeDef() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public List getDataAsList() {
            return listdata;
        }

        @Override
        public void setData(Object o) {
            this.data = o;
        }

        @Override
        public void setDataAsList(List list) {
            this.listdata = list;
        }

        @Override
        public void addData(Object o) {
            listdata.add(o);
        }

        @Override
        public void removeData(Object o) {
            listdata.remove(o);
        }
    }
}
