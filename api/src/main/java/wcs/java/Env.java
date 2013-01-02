package wcs.java;

import static wcs.java.util.Util.toDate;
import static wcs.java.util.Util.toInt;
import static wcs.java.util.Util.toLong;
import wcs.core.ICSProxyJ;
import wcs.java.util.Range;
import wcs.java.util.Util.Arg;
import wcs.java.util.Util.Id;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;

/**
 * Env
 * 
 * @author msciab
 * 
 */
public class Env extends ICSProxyJ {

	/**
	 * Build the env from the ICS
	 * 
	 * @param ics
	 */
	public Env(ICS ics) {
		init(ics);
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
	 * IList iterator
	 * 
	 * public Iterable<IList> iterator(String list) { IList ls =
	 * ics.GetList(list); if (ls == null) return new ArrayList<IList>(); return
	 * new IterableIListWrapper(ls); }
	 */

	/**
	 * Return an iterable sequence of integers to loop a list
	 * 
	 * @param list
	 * @return
	 */

	public Iterable<Integer> range(String list) {
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
	 * Call a template
	 * 
	 * @param id
	 * @param name
	 * @param args
	 */
	public void call(String name, Id id, Arg... args) {
		// RenderTag.Calltemplate tag = RenderTag.calltemplate(name);
		// tag.c(id.type).cid(Long.toString(id.id)).run(ics);
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
	 * Get the current asset (using current c/cid values)
	 * 
	 * @return
	 */
	public Asset getAsset() {
		return getAsset(getC(), getCid());
	}

	/**
	 * Return the asset identified by c/cid
	 */
	public Asset getAsset(String c, Long cid) {
		return new AssetImpl(this, c, cid);
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
}
