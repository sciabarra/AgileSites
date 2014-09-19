package wcs.java;

import wcs.Api;
import wcs.api.Log;
import wcs.core.tag.AssetTag;
import wcs.java.util.Util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

import COM.FutureTense.Interfaces.ICS;

/**
 * The setup class. Invoking the setup method will initialize templates and
 * cselements.
 * 
 * @author msciab
 * 
 */
abstract public class Setup implements wcs.core.Setup {

	private static Log log = Log.getLog(Setup.class);

	private Site _site = null;

	/**
	 * Return the current site - this is set by exec
	 * 
	 * @return
	 */
	public Site getSite() {
		return _site;
	}

	/**
	 * Return assets to create
	 * 
	 * @return
	 */
	public abstract Class<?>[] getAssets();

	// implementation

	private Session session;
	private AssetDataManager adm;
	private ICS ics;

	// future use
	// private SiteManager sim;

	/**
	 * Execute the setup creating the assets using the Asset API
	 * 
	 * @param username
	 * @param password
	 */
	public String exec(ICS ics, String site, String username, String password) {

		log.debug("site=%s", site);

		session = SessionFactory.newSession(username, password);
		adm = (AssetDataManager) session.getManager(AssetDataManager.class
				.getName());
		this.ics = ics;

		this._site = new Site(site);

		// future use
		// sim = (SiteManager) session.getManager(SiteManager.class.getName());

		log.debug("created session, adm, sim");

		StringBuilder sb = new StringBuilder();
		sb.append(">>> Setup BEGIN");

		try {

			// future use
			// sb.append("\nSite: " + getSite().createOrUpdate(sim));

			for (Class<?> clazz : getAssets()) {
				log.debug("instantiating " + clazz.getCanonicalName());
				Method m = clazz.getDeclaredMethod("setup");
				if (m == null)
					continue;
				Object obj = m.invoke("setup");
				if (obj != null && obj instanceof AssetSetup) {
					log.debug("found an actual instance");
					recurse(ics, sb, (AssetSetup) obj, site);
				}
			}

		} catch (Exception e) {
			CharArrayWriter caw = new CharArrayWriter();
			e.printStackTrace(new PrintWriter(caw));
			sb.append("\nException: ").append(caw.toString());
			log.error(e, "Asset creation/update");
		}
		sb.append("\n<<< Setup END\n");
		return sb.toString();
	}

	// recursive setup to handle the getNextAsset
	private void recurse(ICS ics, StringBuilder sb, AssetSetup setup,
			String site) {
		if (setup == null)
			return;
		log.debug("installing " + setup.getName());
		setup.init(site);
		sb.append("\n" + insertOrUpdate(ics, setup));
		recurse(ics, sb, setup.getNextSetup(), site);
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
		log.debug("findByName " + name + ":" + type + " / " + subtype);

		String tmp = Api.tmp();
		if (subtype != null) {
			AssetTag.list().type(type).field1("name").value1(name)
					.field2("subtype").value2(subtype).list(tmp).run(ics);
		} else {
			AssetTag.list().type(type).field1("name").value1(name).list(tmp)
					.run(ics);
		}
		if (ics.GetList(tmp) == null || ics.GetList(tmp).numRows()==0) {
			log.debug("not found");
			return null;
		}
		try {
			String id = ics.GetList(tmp).getValue("id");
			// AssetTag.get().name(tmp).field("id").output(tmp).run(ics);
			// String id = ics.GetVar(tmp);
			log.debug("found id=%s", id);
			AssetId aid = new AssetIdImpl(type, Long.parseLong(id));
			log.debug("loading data for " + aid);
			for (MutableAssetData data : adm.readForUpdate(Arrays.asList(aid))) {
				log.debug("found asset " + data.getAssetId());
				return data;
			}
		} catch (Exception e) {
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
	String insertOrUpdate(ICS ics, AssetSetup setup) {
		log.debug("insertOrUpdate " + setup);

		// if (!false)
		// return asset+" OK";

		String what = setup.toString();

		try {

			MutableAssetData data = findByName(setup.getName(), setup.getC(),
					setup.getSubtype(), setup.getAttributes());

			// inserting
			if (data == null)
				return what + " INSERTING: "
						+ insert(setup, Long.parseLong(ics.genID(true)));

			// updating
			return what + " UPDATING: " + update(setup, data);

		} catch (Exception e) {
			log.error("insertOrUpdate failed", e);
			return what + " ERROR: " + e.getMessage();
		}
	}

	String insert(AssetSetup asset, long id) throws AssetAccessException {
		log.debug("inserting " + asset);

		AssetId aid = new AssetIdImpl(asset.getC(), id);

		MutableAssetData data = adm.newAssetData(asset.getC(),
				asset.getSubtype());

		data.setAssetId(aid);

		_site.setData(data);
		data.getAttributeData("name").setData(asset.getName());
		data.getAttributeData("description").setData(asset.getDescription());
		asset.setData(data);
		try {
			adm.insert(Util.listData(data));
		} catch (Exception e) {
			log.error(e, "insert failed");
			return "ERROR: " + e;
		}
		return "INSERT OK";
	}

	String update(AssetSetup asset, MutableAssetData data)
			throws AssetAccessException {
		log.debug("update " + asset);

		// dump the asset in xml format
		if (data.getAssetId().getType().equals("AttrTypes")) {
			String dump = "\nAsset: " + data.getAssetId()
					+ Util.dumpAssetData(data);
			log.debug(dump);
		}

		_site.setData(data);
		asset.setData(data);

		try {
			adm.update(Util.listData(data));
		} catch (Exception e) {
			log.error(e, "update failed");
			return "ERROR: " + e;
		}
		return "OK";
	}

}
