package wcs.java;

import java.util.Collections;
import java.util.Iterator;
import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.common.SiteAccessException;
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

abstract public class Setup implements wcs.core.Setup {

	private static Log log = new Log(Setup.class);

	/**
	 * Return the site
	 */
	public abstract Site getSite();

	/**
	 * Return templates to create
	 * 
	 * @return
	 */
	public abstract Template[] getTemplates();

	/**
	 * Return cselements to create
	 * 
	 * @return
	 */
	public abstract CSElement[] getCSElements();

	/**
	 * Return cselements to create
	 * 
	 * @return
	 */
	public abstract SiteEntry[] getSiteEntries();

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

			for (CSElement cselement : getCSElements())
				sb.append("\n" + insertOrUpdate(cselement));

			for (SiteEntry siteentry : getSiteEntries())
				sb.append("\n" + insertOrUpdate(siteentry));

			for (Template template : getTemplates())
				sb.append("\n" + insertOrUpdate(template));

		} catch (SiteAccessException e) {
			sb.append("Exception: " + e.getMessage());
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
		Condition c = ConditionFactory.createCondition(type, OpTypeEnum.EQUALS,
				name);
		Query query = new SimpleQuery(type, subtype, c,
				Collections.singletonList("name"));
		try {
			for (AssetData data : adm.read(query)) {
				return data;
			}
		} catch (AssetAccessException e) {
			log.warn("EXCEPTION " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Insert or update
	 * 
	 * @param adm
	 * @param helper
	 */
	String insertOrUpdate(Asset asset) {
		String what = asset.getName() + ":" + asset.getId() + "("
				+ asset.getName() + ")";
		AssetData data = findByName(asset.getName(), asset.getId().type, null);
		try {
			if (data == null) {
				adm.insert(Util.listData(asset.data()));

			} else {
				adm.update(Util.listData(asset.data()));
			}
			return what + " OK";
		} catch (AssetAccessException e) {
			e.printStackTrace();
			return what + " ERROR: " + e.getMessage();
		}

	}
}
