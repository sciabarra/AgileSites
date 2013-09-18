package wcs.java;

import static wcs.core.Common.tmp;
import static wcs.java.util.Util.toDate;
import static wcs.java.util.Util.toInt;
import static wcs.java.util.Util.toLong;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import wcs.core.Arg;
import wcs.core.Common;
import wcs.core.ICSProxyJ;
import wcs.core.Id;
import wcs.core.Log;
import wcs.core.Range;
import wcs.core.WCS;
import wcs.core.tag.AssetTag;
import wcs.core.tag.PublicationTag;
import wcs.core.tag.RenderTag;
import wcs.java.util.AssetDeps;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Facade to the Sites services. It is passed as the main argument to element
 * logic.
 * 
 * @author msciab
 * 
 */
public class Env extends ICSProxyJ {

	private static Log log = Log.getLog(Env.class);
	private Config config;
	private Router router;
	private String site;
	private String normSite;
	private boolean insite;
	private boolean preview;
	
	private boolean hasInsite =  getVersionMajor()==11;
	private boolean hasDevices = getVersionMajor()==11 && getVersionMinor()==8;


	/**
	 * Build the env from the ICS
	 * 
	 * @param ics
	 */
	public Env(ICS ics, String site) {
		init(ics);
		if (site != null) {
			config = Config.getConfig(site);
			this.site = config.getSite();
			this.normSite = WCS.normalizeSiteName(site);
			router = Router.getRouter(site);
		}
		String rendermode = ics.GetVar("rendermode");
		insite = rendermode != null && rendermode.equals("insite");
		preview = rendermode != null && rendermode.startsWith("preview");
	}

	/**
	 * Get a variable or null
	 * 
	 * @param var
	 * @return
	 */
	public String getString(String var) {
		return ics.GetVar(var);
	}

	/**
	 * Get a variable or null
	 *
	 * @param list
     * @param field
	 * @return
	 */
	public String getString(String list, String field) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return null;
		if (ls.numRows() == 0)
			return null;
		try {
			return ls.getValue(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	/**
	 * Get a variable or null
	 * 
	 * @param list
     * @param row
     * @param field
	 * @return
	 */
	public String getString(String list, int row, String field) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return null;
		if (row <= ls.numRows())
			ls.moveTo(row);
		else
			return null;
		try {
			return ls.getValue(field);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	/**
	 * Return size of a list
	 * 
	 * @param list
	 * @return
	 */
	public int getSize(String list) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return 0;
		return ls.numRows();
	}

	/**
	 * Return an iterable sequence of integers to loop a list
	 * 
	 * @param list
	 * @return
	 */
	public Iterable<Integer> getRange(String list) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return new Range(0);
		return new Range(ls.numRows());
	}

	/**
	 * Get variable as a date (or null)
	 * 
	 * @param var
	 * @return
	 */
	public java.util.Date getDate(String var) {
		return toDate(getString(var));
	}

	/**
	 * Get variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public Integer getInt(String var) {
		return toInt(getString(var));
	}

	/**
	 * Get variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public Long getLong(String var) {
		return toLong(getString(var));
	}

	/**
	 * Get field as date or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public java.util.Date getDate(String ls, String field) {
		return toDate(getString(ls, field));
	}

	/**
	 * Get field as long or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public Integer getInt(String ls, String field) {
		return toInt(getString(ls, field));
	}

	/**
	 * Get field as long or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public Long getLong(String ls, String field) {
		return toLong(getString(ls, field));
	}

	/**
	 * Get field at given position as a date, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public java.util.Date getDate(String ls, int pos, String field) {
		return toDate(getString(ls, pos, field));
	}

	/**
	 * Get field at given position as a long, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public Long getLong(String ls, int pos, String field) {
		return toLong(getString(ls, pos, field));
	}

	/**
	 * Get field at given position as a long, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public Integer getInt(String ls, int pos, String field) {
		return toInt(getString(ls, pos, field));
	}

	/**
	 * Get Error nummber
	 */
	public int getError() {
		return ics.GetErrno();
	}

	/**
	 * Check if in error state
	 */
	public boolean isError() {
		return getError() != 0;
	}

	/**
	 * Check if is a variable
	 */
	public boolean isVar(String variable) {
		return ics.GetVar(variable) != null;
	}

	/**
	 * Check if it is a list
	 */
	public boolean isList(String list) {
		return ics.GetList(list) != null;
	}

	/**
	 * Check if exists as a list column
	 */
	public boolean isListCol(String list, String field) {
		return getString(list, field) != null;
	}

	/**
	 * Check if exists as an object
	 */
	public boolean isObj(String object) {
		return ics.GetObj(object) != null;
	}

	/**
	 * Check if we are in the insite editing mode
	 */
	public boolean isInsite() {
		return insite;
	}

	/**
	 * Get an object
	 */
	public Object getObject(String object) {
		return ics.GetObj(object);
	}

	/**
	 * Return the asset identified by c/cid (or null if not possible)
	 * 
	 */
	public Asset getAsset(String c, Long cid) {
		if (c != null && cid != null)
			return new AssetImpl(this, c, cid);
		else
			return null;
	}

	/**
	 * Return the asset identified by and Id (or null if not found)
	 * 
	 */
	public Asset getAsset(Id id) {
		return getAsset(id.c, id.cid);
	}

	/**
	 * Return the asset identified by the current c/cid
	 */
	public Asset getAsset() {
		return getAsset(getC(), getCid());
	}

	/**
	 * Return current "c" (content type)
	 * 
	 * @return
	 */
	public String getC() {
		return getString("c");
	}

	/**
	 * Return current "cid" (content id)
	 */
	public Long getCid() {
		return getLong("cid");
	}

	/**
	 * Return current asset id
	 */
	public Id getId() {
		return new Id(getC(), getCid());
	}

	/**
	 * Return the current config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * Return the current router
	 */
	public Router getRouter() {
		return router;
	}

	/**
	 * The current site name
	 */
	public String getSiteName() {
		return site;
	}

	/**
	 * The current site id
	 */
	public String getSiteId() {
		return getSiteId(site);
	}


	public SitePlan getSitePlan() {
		return new SitePlan(this);
	}

	/**
	 * Return the URL to render this asset - note that rendering is different if
	 * we are in insite/preview mode or in live mode
	 */
	public String getUrl(Id id, Arg... args) {
		if (insite || preview) {
			return RenderTag.getpageurl().pagename(normSite).c(id.c)
					.cid(id.cid.toString()).assembler("query")
					.eval(ics, "outstr");
		}
		String pCid = getRouter().link(this, id, args);
		String pC = WCS.normalizeSiteName(getConfig().getSite());

		String res; 

		if(hasDevices) {
		   res = RenderTag.getpageurl().pagename("AAAgileRouter")// 
				.c(pC).cid(pCid).assembler("agilesites").set("d", "Default").eval(ics, "outstr");
		} else {
			res = RenderTag.getpageurl().pagename("AAAgileRouter")//
				.c(pC).cid(pCid).assembler("agilesites").eval(ics, "outstr");
		}


		log.debug("getUrl: outstr=" + res);
		return res;
	}

	/**
	 * Return the URL to render this asset
	 */
	public String getUrl(String c, Long cid, Arg... args) {
		return getUrl(new Id(c, cid), args);
	}

	/**
	 * Find assets
	 */
	public List<Id> find(String type, Arg... args) {
		// load all the pages with a given name
		AssetTag.List list = AssetTag.list();
		String ls = tmp();
		list.type(type).list(ls);
		list.pubid(getSiteId());
		int n = 1;
		for (Arg arg : args) {
			switch (n) {
			case 1:
				list.field1(arg.name);
				list.value1(arg.value);
				break;
			case 2:
				list.field2(arg.name);
				list.value2(arg.value);
				break;
			case 3:
				list.field3(arg.name);
				list.value3(arg.value);
				break;
			case 4:
				list.field4(arg.name);
				list.value4(arg.value);
				break;
			case 5:
				list.field5(arg.name);
				list.value5(arg.value);
				break;
			default:
				log.warn("too many arguments for find, argument >5 ignored");
			}
			n++;
		}

		list.run(ics);
		List<Id> result = new ArrayList<Id>();
		for (Integer pos : getRange(ls)) {
			result.add(new Id(type, getLong(ls, pos, "id")));
		}
		return result;
	}

	/**
	 * Find one assets
	 */
	public Asset findOne(String type, Arg... args) {
		List<Id> result = find(type, args);
		if (result.size() != 1)
			return null;
		return getAsset(result.get(0).c, result.get(0).cid);
	}

	/**
	 * Call a CS Element in current site
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public String call(String name, Arg... args) {
		List<Arg> list = new LinkedList<Arg>();
		for (Arg arg : args)
			list.add(arg);
		list.add(Common.arg("ELEMENTNAME", site + "/" + name));
		return Common.call("RENDER:CALLELEMENT", list);
	}

	/**
	 * Clear the current error code
	 */
	public void clearError() {
		ics.ClearErrno();
	}

	/**
	 * Add a dependency on anything
	 * 
	 */
	public void addDependency() {
		RenderTag.unknowndeps().run(ics);
	}

	/**
	 * Add a dependency on any asset of the given type
	 * 
	 * @param c
	 */
	public void addDependency(String c) {
		RenderTag.unknowndeps().assettype(c).run(ics);
	}

	/**
	 * Add an EXACT dependency on a given asset
	 * 
	 * @param c
	 * @param cid
	 */
	public void addDependency(String c, Long cid) {
		RenderTag.logdep().c(c).cid(cid.toString()).run(ics);
	}

	/**
	 * Add a dependency of the specified type on a given asset
	 * 
	 * @param c
	 * @param cid
	 */
	public void addDependency(String c, Long cid, AssetDeps deps) {
		RenderTag.logdep().c(c).cid(cid.toString()).deptype(deps.toString())
				.run(ics);
	}

	/**
	 * Add an EXACT dependency on a given asset
	 * 
	 * @param id
	 */
	public void addDependency(Id id) {
		RenderTag.logdep().c(id.c).cid(id.cid.toString()).run(ics);
	}

	/**
	 * Add a dependency of the specified type on a given asset
	 * 
	 * @param id
	 * @param deps
	 */
	public void addDependency(Id id, AssetDeps deps) {
		RenderTag.logdep().c(id.c).cid(id.cid.toString())
				.deptype(deps.toString()).run(ics);
	}

}
