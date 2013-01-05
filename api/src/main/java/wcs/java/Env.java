package wcs.java;

import static wcs.java.Element.scheduleCall;
import static wcs.java.util.Util.arg;
import static wcs.java.util.Util.toDate;
import static wcs.java.util.Util.toInt;
import static wcs.java.util.Util.toLong;

import java.util.ArrayList;
import java.util.List;

import wcs.core.ICSProxyJ;
import wcs.java.tag.RenderTag;
import wcs.java.util.Range;
import wcs.java.util.Util;
import wcs.java.util.Util.Arg;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Env
 * 
 * @author msciab
 * 
 */
public class Env extends ICSProxyJ {

	private Config config;
	private String site;

	/**
	 * Build the env from the ICS
	 * 
	 * @param ics
	 */
	public Env(ICS ics) {
		init(ics);
		site = ics.GetVar("site");
		this.config = Config.getConfigBySite(site);
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
	 * @param var
	 * @return
	 */
	public String getString(String list, String field) {
		IList ls = ics.GetList(list);
		if (ls == null)
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
	 * @param var
	 * @return
	 */
	public String getString(String list, int row, String field) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return null;
		ls.moveTo(row);
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
	 * Call a template by name with a specific c/cid and extra args
	 * 
	 */
	public String callTemplate(String c, Long cid, String name, Arg... args) {
		List<Arg> list = new ArrayList<Arg>();
		list.add(arg("SITE", site));
		list.add(arg("C", c));
		list.add(arg("CID", cid.toString()));
		list.add(arg("TNAME", name));

		list.add(arg("TTYPE", ics.GetVar("tid") != null //
		? "Template"
				: "CSElement"));
		list.add(arg("TID", ics.GetVar("tid") != null //
		? ics.GetVar("tid")
				: ics.GetVar("eid")));

		// TODO: use the slot properly
		list.add(arg("SLOTNAME", name.replace('/', '_')));

		// copy additional args
		for (Arg arg : args)
			list.add(arg);
		return scheduleCall("!RCT", list.toArray(new Arg[0]));
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
	public boolean isVariable(String variable) {
		return ics.GetVar(variable) != null;
	}

	/**
	 * Check if it is a list
	 */
	public boolean isList(String list) {
		return ics.GetList(list) != null;
	}

	/**
	 * Check if it is a list
	 */
	public boolean isField(String list, String field) {
		return getString(list, field) != null;
	}

	/**
	 * Get an object
	 */
	public Object getObject(String object) {
		return ics.GetObj(object);
	}

	/**
	 * Return the asset identified by c/cid
	 */
	public Asset getAsset(String c, Long cid) {
		return new AssetImpl(this, c, cid);
	}

	/**
	 * Return the URL to render this asset using the configured default template
	 */
	public String getAssetUrl(String c, Long cid) {
		return getAssetUrl(c, cid, config.getDefaultTemplate(c));
	}

	/**
	 * Return the URL to render this asset using a specified template
	 */
	public String getAssetUrl(String c, Long cid, String template) {
		
		String outstr = Util.tmpVar();
		String tid = ics.GetVar("tid");
		String ttype = "Template";
		if (tid == null) {
			tid = ics.GetVar("eid");
			ttype = "CSElement";
		}

		RenderTag.gettemplateurl(outstr, template).c(c).cid(cid.toString())
				.site(site).tid(tid).ttype(ttype).run(ics);

		String res = getString(outstr);
		ics.RemoveVar(outstr);
		return res;
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
	 * Return the current config
	 */
	public Config getConfig() {
		return config;
	}
}
