package wcs.java;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import wcs.core.ICSProxyJ;
import wcs.java.util.Util.Arg;
import wcs.java.util.Util.Id;
import COM.FutureTense.Interfaces.ICS;
import COM.FutureTense.Interfaces.IList;
import COM.FutureTense.Util.IterableIListWrapper;

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
	 */
	public Iterable<IList> iterator(String list) {
		IList ls = ics.GetList(list);
		if (ls == null)
			return new ArrayList<IList>();
		return new IterableIListWrapper(ls);
	}

	/**
	 * Get as a date
	 */
	private static SimpleDateFormat fmt = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private java.util.Date toDate(String s) {
		if (s != null) {
			try {
				return fmt.parse(s);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	private Long toLong(String l) {
		if (l == null)
			return null;
		try {
			long ll = Long.parseLong(l);
			return new Long(ll);
		} catch (NumberFormatException ex) {
			return null;
		} catch (Exception ex) {
			// ex.printStackTrace();
			return null;
		}

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

	public int getCounter(String counter) throws Exception {
		if (counter != null) {
			System.out.println("counter=" + counter);
			try {
				return ics.GetCounter(counter);
			} catch (NullPointerException ex) {
				return -1;
				//throw new Exception("not found counter " + counter);
			}
		} else
			throw new Exception("counter name is null");
	}
}
