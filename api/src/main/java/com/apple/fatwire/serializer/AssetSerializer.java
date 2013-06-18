package com.apple.fatwire.serializer;

import java.io.InputStream;

import com.fatwire.assetapi.data.AssetDataImpl;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.system.Session;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.thoughtworks.xstream.XStream;

public class AssetSerializer {

	//private static final Log log = LogFactory.getLog(AssetSerializer.class);

	AssetDataManager adm;
	XStream xstream;

	public AssetSerializer(Session session) {
		adm = (AssetDataManager) session.getManager(//
				AssetDataManager.class.getName());

		// prepare the xstream
		xstream = new XStream();
		xstream.alias("assets", AssetList.class);
		xstream.addImplicitCollection(AssetList.class, "assets");
		xstream.useAttributeFor("rootelement", AssetList.class);
		xstream.useAttributeFor("filename", AssetList.class);
		xstream.useAttributeFor("type", AssetList.class);
		xstream.useAttributeFor("name", AssetList.class);
		
		xstream.alias("asset", AssetDataImpl.class);
		xstream.alias("blob", BlobObjectImpl.class);
		xstream.alias("assetid", AssetIdImpl.class);
		xstream.alias("attrdata", AttributeDataImpl.class);
		xstream.registerConverter(new AssetDataConverter(adm));
		xstream.registerConverter(new BlobConverter());
		xstream.registerConverter(new AssetIdConverter());
		xstream.registerConverter(new AttributeDataConverter());
		
		xstream.registerConverter(new AssetMapConverter());
	}

	public String serialize(AssetList assetList) {
		return xstream.toXML(assetList);
	}

	public AssetList deserialize(InputStream in) {
		return (AssetList) xstream.fromXML(in);
	}
}
