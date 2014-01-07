package wcs.java;

import static wcs.Api.arg;
import static wcs.Api.tmp;
import static wcs.Api.toDate;
import static wcs.Api.toInt;
import static wcs.Api.toLong;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import wcs.Api;
import wcs.api.Arg;
import wcs.api.Asset;
import wcs.api.AssetDeps;
import wcs.api.Content;
import wcs.api.Id;
import wcs.api.Log;
import wcs.api.Range;
import wcs.core.WCS;
import wcs.core.tag.AssetTag;
import wcs.core.tag.RenderTag;
import wcs.core.tag.SatelliteTag;
import wcs.java.util.Util;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Facade to the Sites services. It is passed as the main argument to element
 * logic.
 * 
 * @author msciab
 * 
 */
public class Env extends wcs.core.ICSProxyJ implements Content, wcs.api.Env {

	private static Log log = Log.getLog(Env.class);
	@SuppressWarnings("unused")
	private boolean hasInsite = getVersionMajor() == 11;
	//private boolean hasDevices = getVersionMajor() == 11
	//		&& getVersionMinor() == 8;
	private wcs.api.Config config;
	private wcs.api.Router router;
	private String site;
	private boolean insite;
	private boolean preview;

	/**
	 * Build the env - with this costructor you have an invalid env that still
	 * needs to be initialized with init to be usable
	 */
	public Env() {
	}

	/**
	 * Build the env from the ICS and initialize it
	 * 
	 * @param ics
	 */
	public Env(ICS ics) {
		init(ics);
	}

	/**
	 * Initialize the env - second step constructor
	 * 
	 * @param ics
	 */
	public void init(ICS ics) {
		super.init(ics);
		this.site = ics.GetVar("site");
		config = wcs.core.WCS.getConfig(ics);
		router = wcs.core.WCS.getRouter(ics);
		String rendermode = ics.GetVar("rendermode");
		insite = rendermode != null && rendermode.equals("insite");
		preview = rendermode != null && rendermode.startsWith("preview");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getString(java.lang.String)
	 */
	@Override
	public String getString(String var) {
		return ics.GetVar(var);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getString(java.lang.String, java.lang.String)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getString(java.lang.String, int, java.lang.String)
	 */
	@Override
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getString(java.lang.String, int)
	 */
	@Override
	public String getString(String list, int row) {
		return getString(list, row, "value");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getSize(java.lang.String)
	 */
	@Override
	public int getSize(String list) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return 0;
		return ls.numRows();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getRange(java.lang.String)
	 */
	@Override
	public Iterable<Integer> getRange(String list) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return new Range(0);
		return new Range(ls.numRows());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getDate(java.lang.String)
	 */
	@Override
	public java.util.Date getDate(String var) {
		return toDate(getString(var));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getInt(java.lang.String)
	 */
	@Override
	public Integer getInt(String var) {
		return toInt(getString(var));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getLong(java.lang.String)
	 */
	@Override
	public Long getLong(String var) {
		return toLong(getString(var));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getDate(java.lang.String, java.lang.String)
	 */
	@Override
	public java.util.Date getDate(String ls, String field) {
		return toDate(getString(ls, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getInt(java.lang.String, java.lang.String)
	 */
	@Override
	public Integer getInt(String ls, String field) {
		return toInt(getString(ls, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getLong(java.lang.String, java.lang.String)
	 */
	@Override
	public Long getLong(String ls, String field) {
		return toLong(getString(ls, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getDate(java.lang.String, int, java.lang.String)
	 */
	@Override
	public java.util.Date getDate(String ls, int pos, String field) {
		return toDate(getString(ls, pos, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getDate(java.lang.String, int)
	 */
	@Override
	public java.util.Date getDate(String ls, int pos) {
		return toDate(getString(ls, pos, "value"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getLong(java.lang.String, int, java.lang.String)
	 */
	@Override
	public Long getLong(String ls, int pos, String field) {
		return toLong(getString(ls, pos, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getLong(java.lang.String, int)
	 */
	@Override
	public Long getLong(String ls, int pos) {
		return toLong(getString(ls, pos, "value"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getInt(java.lang.String, int, java.lang.String)
	 */
	@Override
	public Integer getInt(String ls, int pos, String field) {
		return toInt(getString(ls, pos, field));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getInt(java.lang.String, int)
	 */
	@Override
	public Integer getInt(String ls, int pos) {
		return toInt(getString(ls, pos, "value"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getError()
	 */
	@Override
	public int getError() {
		return ics.GetErrno();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isError()
	 */
	@Override
	public boolean isError() {
		return getError() != 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isVar(java.lang.String)
	 */
	@Override
	public boolean isVar(String variable) {
		return ics.GetVar(variable) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isList(java.lang.String)
	 */
	@Override
	public boolean isList(String list) {
		return ics.GetList(list) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isList(java.lang.String, int)
	 */
	@Override
	public boolean isList(String list, int n) {
		return ics.GetList(list) != null && ics.GetList(list).numRows() > n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isListCol(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isListCol(String list, String field) {
		return getString(list, field) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isObj(java.lang.String)
	 */
	@Override
	public boolean isObj(String object) {
		return ics.GetObj(object) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#isInsite()
	 */
	@Override
	public boolean isInsite() {
		return insite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String object) {
		return ics.GetObj(object);
	}

	/**
	 * Return the asset identified by c/cid (or null if not possible)
	 * 
	 */
	@Override
	public wcs.api.Asset getAsset(String c, Long cid) {
		if (c != null && cid != null)
			return new wcs.java.Asset(this, c, cid);
		else
			return null;
	}

	/**
	 * Return the asset identified by and Id (or null if not found)
	 * 
	 */
	@Override
	public Asset getAsset(Id id) {
		return getAsset(id.c, id.cid);
	}

	/**
	 * Return the asset identified by the current c/cid
	 */
	@Override
	public Asset getAsset() {
		return getAsset(getC(), getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getC()
	 */
	@Override
	public String getC() {
		return getString("c");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getCid()
	 */
	@Override
	public Long getCid() {
		return getLong("cid");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getId()
	 */
	@Override
	public Id getId() {
		return new Id(getC(), getCid());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.core.Env#getConfig()
	 */
	@Override
	public wcs.api.Config getConfig() {
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getRouter()
	 */
	@Override
	public wcs.api.Router getRouter() {
		return router;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getSiteName()
	 */
	@Override
	public String getSiteName() {
		return site;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getSiteId()
	 */
	@Override
	public String getSiteId() {
		return getSiteId(site);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getSitePlan()
	 */
	@Override
	public wcs.api.SitePlan getSitePlan() {
		return new wcs.java.SitePlan(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getUrl(wcs.core.Id, wcs.core.Arg)
	 */
	@Override
	public String getUrl(Id id, Arg... args) {
		// System.out.println("getUrl:" + id);

		if (insite || preview) {
			return RenderTag.getpageurl().pagename(site).c(id.c)
					.cid(id.cid.toString()).assembler("query")
					.eval(ics, "outstr");
		}

		String nsite = WCS.normalizeSiteName(site);
		String url = getRouter().link(this, id, args);

		String res;

		String rendermode = ics.GetVar("rendermode");
		if (rendermode == null)
			rendermode = "live";

		res = SatelliteTag.link().pagename("AAAgileRouter")
				.set("rendermode", rendermode).set("url", url)
				.set("site", nsite).assembler("agilesites")
				.eval(ics, "outstring");

		if (log.debug())
			log.debug("getUrl: outstr=" + res);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#getUrl(java.lang.String, java.lang.Long, wcs.core.Arg)
	 */
	@Override
	public String getUrl(String c, Long cid, Arg... args) {
		return getUrl(new Id(c, cid), args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#find(java.lang.String, wcs.core.Arg)
	 */
	@Override
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
	@Override
	public Asset findOne(String type, Arg... args) {
		List<Id> result = find(type, args);
		if (result.size() != 1)
			return null;
		return getAsset(result.get(0).c, result.get(0).cid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#call(java.lang.String, wcs.core.Arg)
	 */
	@Override
	public String call(String name, Arg... args) {
		List<Arg> list = new LinkedList<Arg>();
		for (Arg arg : args)
			list.add(arg);
		list.add(arg("ELEMENTNAME",
				site + "/" + Util.normalizedName(site, name)));
		return Api.call("RENDER:CALLELEMENT", list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#clearError()
	 */
	@Override
	public void clearError() {
		ics.ClearErrno();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency()
	 */
	@Override
	public void addDependency() {
		RenderTag.unknowndeps().run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency(java.lang.String)
	 */
	@Override
	public void addDependency(String c) {
		RenderTag.unknowndeps().assettype(c).run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency(java.lang.String, java.lang.Long)
	 */
	@Override
	public void addDependency(String c, Long cid) {
		RenderTag.logdep().c(c).cid(cid.toString()).run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency(java.lang.String, java.lang.Long,
	 * wcs.java.util.AssetDeps)
	 */
	@Override
	public void addDependency(String c, Long cid, AssetDeps deps) {
		RenderTag.logdep().c(c).cid(cid.toString()).deptype(deps.toString())
				.run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency(wcs.core.Id)
	 */
	@Override
	public void addDependency(Id id) {
		RenderTag.logdep().c(id.c).cid(id.cid.toString()).run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#addDependency(wcs.core.Id, wcs.java.util.AssetDeps)
	 */
	@Override
	public void addDependency(Id id, AssetDeps deps) {
		RenderTag.logdep().c(id.c).cid(id.cid.toString())
				.deptype(deps.toString()).run(ics);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String attribute) {
		return isVar(attribute);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.IEnv#exists(java.lang.String, int)
	 */
	@Override
	public boolean exists(String attribute, int pos) {
		return isList(attribute, pos);
	}

	@Override
	public ICS ics() {
		return ics;
	}

	/**
	 * Dump all the variables (NOT the lists...)
	 */
	@Override
	public String dump() {
		StringBuilder sb = new StringBuilder();
		@SuppressWarnings("unchecked")
		Enumeration<String> en = ics.GetVars();
		while (en.hasMoreElements()) {
			String v = en.nextElement();
			sb.append(v).append("=").append(getString(v)).append("\n");
		}
		return sb.toString();
	}

	/**
	 * Dump the list with the given name
	 */
	@Override
	public String dump(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append("=");
		for (int i : getRange(name))
			sb.append(getString(name, i)).append(",");
		sb.setCharAt(sb.length() - 1, '\n');
		return sb.toString();
	}

	/**
	 * unpacks a var and puts it in ICS. temporary workaround for the
	 * render:unpagkargs tag wrapper that's not working
	 * 
	 * @param var
	 * @param packed
	 */
	public void unpackVar(String var, String packed) {
		Map<String, String> m = new HashMap<String, String>();
		ics.decode(packed, m);
		String value = m.get(var);
		ics.SetVar(var, value);
	}
}
