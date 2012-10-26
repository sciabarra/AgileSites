package wcs.java;

import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.query.Condition;
import com.fatwire.assetapi.query.ConditionFactory;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.Query;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.assetapi.site.SiteManager;
import com.fatwire.system.Session;
import com.fatwire.system.SessionFactory;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import java.util.Collections;
import java.util.Iterator;

import wcs.java.Asset;

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
	private SiteManager sim;

	/**
	 * Execute the setup creating the assets using the Asset API
	 * 
	 * @param username
	 * @param password
	 */
	public String exec(String username, String password) {

		session = SessionFactory.newSession(username, password);
		adm = (AssetDataManager) session.getManager(AssetDataManager.class
				.getName());
		sim = (SiteManager) session.getManager(SiteManager.class.getName());

		log.debug("created session, adm, sim");

		StringBuilder sb = new StringBuilder();
		sb.append("Setup.exec BEGIN");

		try {
			sb.append("\nSite: " + getSite().createOrUpdate(sim));

			for (Asset asset : getAssets())
				sb.append("\n" + insertOrUpdate(asset));

		} catch (Exception e) {
			sb.append("\nException: " + e.getMessage());
			log.warn(e.getMessage());
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
	AssetData findByName(String name, String type, String subtype) {
		log.debug("findByName " + name + ":" + type);
		Condition c = ConditionFactory.createCondition(type, OpTypeEnum.EQUALS,
				name);
		Query query = new SimpleQuery(type, subtype, c,
				Collections.singletonList("name"));
		try {
			for (AssetData data : adm.read(query)) {
				log.trace("found");
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
	String insertOrUpdate(Asset asset) {
		log.debug("insertOrUpdate " + asset);

		// if (!false)
		// return asset+" OK";

		String what = asset.getName() + ":" + asset.getQid() + "("
				+ asset.getName() + ")";

		AssetData data = findByName(asset.getName(), asset.getQid().type, null);

		try {

			// inserting
			if (data == null)
				return what + ": " + insert(asset);

			// updating
			return what + ": " + update(asset, data);

		} catch (AssetAccessException e) {
			e.printStackTrace();
			return what + " ERROR: " + e.getMessage();
		}
	}

	String insert(Asset asset) throws AssetAccessException {
		log.debug("inserting " + asset);
		AssetData data = adm.newAssetData(asset.getQid().type,
				asset.getQid().subtype);
		getSite().setData(data);
		data.getAttributeData("name").setData(asset.getName());
		data.getAttributeData("description").setData(asset.getDescription());
		asset.setData(data);
		adm.insert(Util.listData(data));
		return "OK";
	}

	String update(Asset asset, AssetData data) throws AssetAccessException {
		log.debug("update " + asset);
		getSite().setData(data);
		asset.setData(data);
		adm.update(Util.listData(data));
		return "OK";
	}

}
