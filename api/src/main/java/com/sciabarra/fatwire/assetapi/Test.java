package com.sciabarra.fatwire.assetapi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.fatwire.assetapi.site.SiteManager;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.openmarket.xcelerate.asset.AttributeDefImpl;
import com.thoughtworks.xstream.XStream;

public abstract class Test {

	static Log log = LogFactory.getLog(Test.class);

	Session ses;
	AssetDataManager adm;
	SiteManager sim;

	public Test() {
		ses = SessionFactory.newSession("fwadmin", "xceladmin");
		sim = (SiteManager) ses.getManager(SiteManager.class.getName());
		adm = (AssetDataManager) ses.getManager(AssetDataManager.class
				.getName());

	}

	// save an asset
	public AssetId save(AssetData asset) throws AssetAccessException {
		List<AssetData> assetList = new LinkedList<AssetData>();
		assetList.add(asset);
		adm.insert(assetList);
		return asset.getAssetId();
	}

	// create a list of asset id...
	public List<AssetId> list(AssetId... objs) {
		List<AssetId> result = new LinkedList<AssetId>();
		for (AssetId obj : objs)
			result.add(obj);
		// System.out.println(result);
		return result;
	}

	// create a list of asset id...
	public List<String> list(String... objs) {
		List<String> result = new LinkedList<String>();
		for (String obj : objs)
			result.add(obj);
		// System.out.println(result);
		return result;
	}

	// find an asset
	public AssetData find(String c, long cid) {
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

	AttributeDataImpl attrData(String name, AttributeTypeEnum type, Object value) {
		return new AttributeDataImpl(new AttributeDefImpl(name, type), name,
				type, value);
	}

	AttributeDataImpl attrData(String name, boolean isRequired) {
		return attrData(name, AttributeTypeEnum.STRING, isRequired ? "true"
				: "false");
	}

	AttributeDataImpl attrData(String name, String value) {
		return attrData(name, AttributeTypeEnum.STRING, value);
	}

	public void dump(Object object) {
		XStream xstream = new XStream();
		System.out.println(xstream.toXML(object));

	}

	abstract public String doIt() throws Exception;

}
