package wcs.java;

import java.util.Iterator;
import java.util.List;

import COM.FutureTense.Interfaces.ICS;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.query.Condition;
import com.fatwire.assetapi.query.ConditionFactory;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;
import com.openmarket.xcelerate.asset.AssetIdImpl;

abstract public class Setup implements wcs.core.Setup {

	private static Log log = new Log(Setup.class);

	/**
	 * Return the site
	 */
	public abstract Site getSite();

	/**
	 * Return assets to create
	 * 
	 * @return
	 */
	public abstract Asset[] getAssets();

	// implementation

	private Session session;
	private AssetDataManager adm;

	// future use
	// private SiteManager sim;

	/**
	 * Execute the setup creating the assets using the Asset API
	 * 
	 * @param username
	 * @param password
	 */
	public String exec(ICS ics, String username, String password) {

		session = SessionFactory.newSession(username, password);
		adm = (AssetDataManager) session.getManager(AssetDataManager.class
				.getName());

		// future use
		// sim = (SiteManager) session.getManager(SiteManager.class.getName());

		log.debug("created session, adm, sim");

		StringBuilder sb = new StringBuilder();
		sb.append("Setup.exec BEGIN");

		try {

			// future use
			// sb.append("\nSite: " + getSite().createOrUpdate(sim));

			for (Asset asset : getAssets()) {
				asset.setSite(this.getSite().getName());
				sb.append("\n" + insertOrUpdate(ics, asset));
			}

		} catch (Exception e) {
			sb.append("\nException: " + log.error(e));
		}
		sb.append("\nSetup.exec END\n");
		return sb.toString();
	}

	/**
	 * Find an asset by id
	 * 
	 * @param c
	 * @param cid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	AssetData findById(String c, long cid) {
		AssetId aid = new AssetIdImpl(c, cid);
		Iterator it;
		try {
			it = (Iterator) adm.read(Util.list(aid)).iterator();
			if (it.hasNext())
				return (AssetData) it.next();
		} catch (Exception e) {
			log.warn(e.getMessage());
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * Find an asset by name
	 * 
	 * @param name
	 * @param type
	 * @param subtype
	 * @return
	 */
	MutableAssetData findByName(String name, String type, String subtype,
			List<String> attributes) {
		log.debug("findByName " + name + ":" + type);
		Condition c = ConditionFactory.createCondition("name",
				OpTypeEnum.EQUALS, name);
		SimpleQuery query = new SimpleQuery(type, subtype, c, attributes);
		// query.getProperties().setIsBasicSearch(true);
		// query.getProperties().setReadAll(false);
		try {
			for (MutableAssetData data : adm.readForUpdate(query)) {
				log.trace("found asset");
				return data;
			}
		} catch (AssetAccessException e) {
			log.warn("EXCEPTION " + e.getMessage());
			e.printStackTrace();
		}
		log.trace("not found");
		return null;
	}

	/**
	 * Insert or update
	 * 
	 * @param adm
	 * @param helper
	 */
	String insertOrUpdate(ICS ics, Asset asset) {
		log.debug("insertOrUpdate " + asset);

		// if (!false)
		// return asset+" OK";

		String what = asset.toString();

		try {

			MutableAssetData data = findByName(asset.getName(),
					asset.getType(), null, asset.getAttributes());

			// inserting
			if (data == null)
				return what + " INSERTING: "
						+ insert(asset, Long.parseLong(ics.genID(true)));

			// updating
			return what + " UPDATING: " + update(asset, data);

		} catch (Exception e) {
			log.error(e);
			return what + " ERROR: " + e.getMessage();
		}
	}

	String insert(Asset asset, long id) throws AssetAccessException {
		log.debug("inserting " + asset);

		AssetId aid = new AssetIdImpl(asset.getType(), id);

		MutableAssetData data = adm.newAssetData(asset.getType(),
				asset.getSubtype());

		data.setAssetId(aid);

		getSite().setData(data);
		data.getAttributeData("name").setData(asset.getName());
		data.getAttributeData("description").setData(asset.getDescription());
		asset.setData(data);
		try {
			adm.insert(Util.listData(data));
		} catch (Exception e) {
			log.error(e);
			return "ERROR: " + e;
		}
		return "INSERT OK";
	}

	String update(Asset asset, MutableAssetData data)
			throws AssetAccessException {
		log.debug("update " + asset);

		getSite().setData(data);
		asset.setData(data);

		// String dump = "\nAsset: " + data.getAssetId() + Util.dump(data);
		// log.debug(dump);
		try {
			adm.update(Util.listData(data));
		} catch (Exception e) {
			log.error(e);
			return "ERROR: " + e;
		}
		return "OK";
	}

}
