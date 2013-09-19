package com.sciabarra.fatwire.serializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.query.ConditionExpression;
import com.fatwire.assetapi.query.ConditionExpressionImpl;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.SimpleCondition;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;

public class AssetCrudManager {

	static Log log = LogFactory.getLog(AssetCrudManager.class);

	AssetDataManager adm;

	public AssetCrudManager(String username, String password) {
		this(username != null && password != null //
		? SessionFactory.newSession(username, password) : SessionFactory
				.getSession());
	}

	AssetSerializer serializer;

	public AssetCrudManager(Session session) {
		adm = (AssetDataManager) session.getManager(AssetDataManager.class
				.getName());
		serializer = new AssetSerializer(session);
	}

	// return a list of VOID assets
	public AssetList findByName(String type, String name)
			throws AssetAccessException {
		SimpleQuery query = new SimpleQuery(type, null);
		ConditionExpression ce = new ConditionExpressionImpl("name",
				OpTypeEnum.EQUALS, Arrays.asList(name));
		query.setCondition(new SimpleCondition(ce));
		AssetList result = new AssetList(adm.read(query).iterator());
		log.debug("findByName(type=" + type + ",name=" + name + ")=" + result);
		return result;
	}

	// load assets
	public AssetList loadByName(String type, String name)
			throws AssetAccessException {
		AssetList onlyIds = findByName(type, name);
		AssetList result = new AssetList(adm.read(onlyIds.getIds()).iterator());
		log.debug("loadByName(type=" + type + ",name=" + name + ")=" + result);
		return result;

	}

	// delete assets
	public String delete(String type, String name) {
		try {
			AssetList toBeDeleted = findByName(type, name);
			log.debug("toBeDeleted=" + toBeDeleted);
			adm.delete(toBeDeleted.getIds());
			log.debug("delete(type=" + type + ",name=" + name + ")="
					+ toBeDeleted);
			return "<ok>deleting " + toBeDeleted + "</ok>";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "<error>" + ex.getMessage() + "</error>";
		}
	}

	public String load(String type, String name) {
		try {
			log.debug("load(type=" + type + ",name=" + name + ")");
			return serializer.serialize(loadByName(type, name));
		} catch (AssetAccessException e) {
			e.printStackTrace();
			return "<error>" + e.getMessage() + "</error>";
		}
	}

	public String save(byte[] xml) {
		log.debug("save()");
		try {

			InputStream in = new ByteArrayInputStream(xml);

			// System.out.println("*** deserialize ***");
			AssetList newAssets = serializer.deserialize(in);

			// this to figure out what we get from deserializing
			// System.out.println(serializer.serialize(newAssets));

			// System.out.println("newAssets=" + newAssets);

			AssetList oldAssets = findByName(newAssets.firstType(),
					newAssets.firstName());

			// System.out.println("oldAssets=" + oldAssets);

			List<AssetData> toBeUpdated = new ArrayList<AssetData>();
			List<AssetData> toBeInserted = new ArrayList<AssetData>();

			for (AssetData data : newAssets.getAssets())
				if (oldAssets.contains(data)) {
					toBeUpdated.add(data);
				} else
					toBeInserted.add(data);

			if (toBeUpdated.size() > 0)
				adm.update(toBeUpdated);

			if (toBeInserted.size() > 0) {
				try {
					adm.insert(toBeInserted);
				} catch (Exception ex) {
					log.info("ignored error :" + ex.getMessage());
				}
				// update them anyway in case of a void asset
				adm.update(toBeInserted);
			}

			String result = "<ok>"
					+ "\n<inserted>"
					+ serializer.serialize(new AssetList(toBeInserted
							.iterator()))
					+ "</inserted>"
					+ "\n<updated>"
					+ serializer
							.serialize(new AssetList(toBeUpdated.iterator()))
					+ "</updated>" + "</ok>";

			return result;

		} catch (Exception e) {
			log.warn(e);
			return "<error>" + e.getMessage() + "</error>";
		}

	}

}
