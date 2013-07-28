package wcs.java;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import wcs.core.Log;
import wcs.java.util.Util;
import com.fatwire.assetapi.common.AssetAccessException;
import com.fatwire.assetapi.common.SiteAccessException;
import com.fatwire.assetapi.data.AssetDataManager;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AssetTypeDefManager;
import com.fatwire.assetapi.query.Condition;
import com.fatwire.assetapi.query.ConditionFactory;
import com.fatwire.assetapi.query.OpTypeEnum;
import com.fatwire.assetapi.query.SimpleQuery;
import com.fatwire.assetapi.site.SiteManager;
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

	private Site site = null;

	/**
	 * Return the current site - this is set by exec
	 * 
	 * @return
	 */
	public Site getSite() {
		return site;
	}

	// implementation

	private Session session;
	private AssetDataManager adm;
	private SiteManager sim;
	private AssetTypeDefManager atdm;

	/**
	 * Execute the setup creating the assets using the Asset API
	 * 
	 * @param username
	 * @param password
	 */
	public String exec(ICS ics, String site, String username, String password) {

		session = SessionFactory.newSession(username, password);
		adm = (AssetDataManager) session.getManager(AssetDataManager.class
				.getName());
		sim = (SiteManager) session.getManager(SiteManager.class.getName());
		atdm = (AssetTypeDefManager) session
				.getManager(AssetTypeDefManager.class.getName());

		// future use
		log.debug("created session, adm, sim");

		StringBuilder msg = new StringBuilder();
		msg.append(">>> Setup BEGIN");
		setupStaticType(ics, msg);
		setupSite(ics, site, msg);
		msg.append("\n--- Import Assets");
		setupAssets(msg, ics, site);
		msg.append("\n--- Import Static");
		// setupStatic(ics, site, msg);
		msg.append("\n<<< Setup END\n");
		return msg.toString();
	}

	private void setupSite(ICS ics, String siteName, StringBuilder sb) {
		boolean drop = ics.GetVar("drop") != null
				&& ics.GetVar("drop").equals("yes");
		this.site = new Site(siteName, this);
		try {
			site.dropThencreateOrUpdate(drop, sim, sb);
		} catch (SiteAccessException e) {
			e.printStackTrace();
		}
	}

	private void setupAssets(StringBuilder sb, ICS ics, String site) {
		for (Class<?> clazz : getAssets())
			try {
				setupAsset(clazz, ics, site, sb);
			} catch (Exception e) {
				log.trace(e, "cannot setup %s", clazz.getCanonicalName());
			}
	}

	private void setupAsset(Class<?> clazz, ICS ics, String site,
			StringBuilder sb) {
		log.debug("instantiating " + clazz.getCanonicalName());
		Method m = null;
		try {
			m = clazz.getDeclaredMethod("setup");
			if (m == null)
				return;
			Object obj = m.invoke("setup");
			if (obj != null && obj instanceof AssetSetup) {
				log.debug("found an actual instance");
				recurse(ics, sb, (AssetSetup) obj, site);
			}
		} catch (Exception e) {
			CharArrayWriter caw = new CharArrayWriter();
			e.printStackTrace(new PrintWriter(caw));
			sb.append("\nException: ").append(caw.toString());
			log.error(e, "Asset creation/update");
		}
	}

	// recursive setup to handle the getNextAsset
	private void recurse(ICS ics, StringBuilder sb, AssetSetup setup,
			String site) {
		if (setup == null)
			return;
		log.debug("installing " + setup.getName());
		setup.setSite(site);
		sb.append("\n" + insertOrUpdate(ics, setup));
		recurse(ics, sb, setup.getNextSetup(), site);
	}

	private void setupStaticType(ICS ics, StringBuilder sb) {
		try {
			sb.append("\nFound asset type "
					+ atdm.findByName("Static", "").getName());
		} catch (Exception e) {
			try {
				atdm.createAssetMakerAssetType(
						"Static",
						"Static.xml",
						"<ASSET "
								+ " NAME=\"Static\" "
								+ " DESCRIPTION=\"Static\" "
								+ " PROCESSOR=\"4.0\" "
								+ " DEFDIR=\"$(CS.Property.agilesites.static)\">"
								+ "<PROPERTIES>" + "<PROPERTY "
								+ " NAME=\"url\" " + " DESCRIPTION=\"URL\">"
								+ "<STORAGE " + " TYPE=\"VARCHAR\""
								+ " LENGTH=\"255\"/>" + "<INPUTFORM "
								+ " TYPE=\"UPLOAD\" " + " WIDTH=\"255\" "
								+ " REQUIRED=\"YES\""
								+ " LINKTEXT=\"UPLOAD\"/>" + "</PROPERTY>"
								+ "</PROPERTIES>" + "</ASSET>", false, false);
				sb.append("\nCreated Static");
			} catch (Exception ex) {
				sb.append("Exception " + ex.getMessage());
			}
		}
	}

	@SuppressWarnings("unused")
	private void setupStatic(ICS ics, String site, StringBuilder sb) {
		sb.append("\njar=").append(ics.GetVar("jar"));
		File jar = new File(ics.GetVar("jar"));
		int n = jar.getAbsoluteFile().getParentFile().getAbsolutePath()
				.length();
		Static st = new Static(jar, n);
		st.setSite(site);
		sb.append("\n" + insertOrUpdate(ics, st));
	}

	/**
	 * Find an asset by id
	 * 
	 * @param c
	 * @param cid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	MutableAssetData findById(String c, long cid) {
		AssetId aid = new AssetIdImpl(c, cid);
		Iterator it;
		try {
			it = (Iterator) adm.readForUpdate(Util.list(aid)).iterator();

			if (it.hasNext())
				return (MutableAssetData) it.next();
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
	String insertOrUpdate(ICS ics, AssetSetup setup) {
		log.debug("insertOrUpdate " + setup);

		// if (!false)
		// return asset+" OK";

		String what = setup.toString();

		try {
			MutableAssetData data;
			Long id = setup.getCid();
			if (id == null)
				data = findByName(setup.getName(), setup.getC(), null,
						setup.getAttributes());
			else
				data = findById(setup.getC(), id.longValue());

			// inserting
			if (data == null)
				return what
						+ " INSERTING: "
						+ insert(setup,
								id == null ? Long.parseLong(ics.genID(true))
										: id.longValue());

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

		site.setData(data);
		data.setAssetId(aid);
		data.getAttributeData("name").setData(asset.getName());
		data.getAttributeData("description").setData(asset.getDescription());
		setTimeStamp(data, asset.getCid().longValue());		
		asset.setData(data);
		try {
			adm.insert(Util.listData(data));
		} catch (Exception e) {
			log.error(e, "insert failed");
			return "ERROR: " + e;
		}
		return "INSERT " + aid + " OK ";
	}

	String update(AssetSetup asset, MutableAssetData data)
			throws AssetAccessException {
		log.debug("update " + asset);
		AssetId aid = data.getAssetId();

		site.setData(data);
		data.getAttributeData("name").setData(asset.getName());

		setTimeStamp(data, asset.getCid().longValue());
		asset.setData(data);

		try {
			adm.update(Util.listData(data));
		} catch (Exception e) {
			log.error(e, "update failed");
			return "ERROR: " + e;
		}
		return "UPDATE " + aid + " OK";
	}
	
	private void setTimeStamp(MutableAssetData data, long id) {
		data.getAttributeData("createdby").setData("AgileSites");
		data.getAttributeData("createddate").setData(
				new java.util.Date(id));
		data.getAttributeData("updatedby").setData("AgileSites");
		data.getAttributeData("updateddate").setData(new java.util.Date());
	}

	abstract public Long getId();

	abstract public String getName();

	abstract public String getDescription();

	abstract public List<String> getUsers();

	abstract public List<String> getUserRoles(String arg0);

	abstract public List<String> getAssetTypes();

	abstract public Class<?>[] getAssets();

}
