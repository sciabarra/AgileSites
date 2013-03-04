package wcs.java;

import static wcs.core.Common.tmp;
import static wcs.java.util.Util.toDate;
import static wcs.java.util.Util.toInt;
import static wcs.java.util.Util.toLong;

import java.util.ArrayList;
import java.util.List;

import wcs.core.Arg;
import wcs.core.ICSProxyJ;
import wcs.core.Id;
import wcs.java.tag.AssetTag;
import wcs.java.util.Log;
import wcs.java.util.Range;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Env
 * 
 * @author msciab
 * 
 */
public class Env extends ICSProxyJ {

	private static Log log = new Log(Env.class);

	private Config config;

	/**
	 * Build the env from the ICS
	 * 
	 * @param ics
	 */
	public Env(ICS ics, String site) {
		init(ics);
		config = Config.getConfig(site);
		log.debug("Loading Config Class");
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
	 * Check if it is a field
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
	 * Return the current config
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * Find assets
	 */
	public List<Id> find(String type, Arg... args) {
		// load all the pages with a given name
		AssetTag.List list = AssetTag.list();
		String ls = tmp();
		list.type(type).list(ls);
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

}
