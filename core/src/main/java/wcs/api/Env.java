package wcs.api;

import java.util.List;


import COM.FutureTense.Interfaces.ICS;

public interface Env extends Content {
	
	/**
	 * Initialize the class with the ICS
	 */
	public void init(ICS ics);

	/**
	 * Return the underlying ICS
	 */
	public ICS ics();
	
	/**
	 * Return the siteplan root of the given site
	 */
	public Id getSitePlanRoot(String siteName);


	/**
	 * Get a variable or null
	 * 
	 * @param var
	 * @return
	 */
	public abstract String getString(String var);

	/**
	 * Get the field of a list or null
	 * 
	 * @param list
	 * @param field
	 * @return
	 */
	public abstract String getString(String list, String field);

	/**
	 * Get the nth field of a list or null
	 * 
	 * @param list
	 * @param row
	 * @param field
	 * @return
	 */
	public abstract String getString(String list, int row, String field);

	/**
	 * Get the field "value" of a list or null
	 * 
	 * @param list
	 * @param row
	 * @param field
	 * @return
	 */
	public abstract String getString(String list, int row);

	/**
	 * Return size of a list
	 * 
	 * @param list
	 * @return
	 */
	public abstract int getSize(String list);

	/**
	 * Return an iterable sequence of integers to loop a list
	 * 
	 * @param list
	 * @return
	 */
	public abstract Iterable<Integer> getRange(String list);

	/**
	 * Get variable as a date (or null)
	 * 
	 * @param var
	 * @return
	 */
	public abstract java.util.Date getDate(String var);

	/**
	 * Get variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public abstract Integer getInt(String var);

	/**
	 * Get variable as Long or null
	 * 
	 * @param var
	 * @return
	 */
	public abstract Long getLong(String var);

	/**
	 * Get field as date or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public abstract java.util.Date getDate(String ls, String field);

	/**
	 * Get field as long or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public abstract Integer getInt(String ls, String field);

	/**
	 * Get field as long or null
	 * 
	 * @param ls
	 * @param field
	 * @return
	 */
	public abstract Long getLong(String ls, String field);

	/**
	 * Get field at given position as a date, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract java.util.Date getDate(String ls, int pos, String field);

	/**
	 * Get the field value at given position as a date, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract java.util.Date getDate(String ls, int pos);

	/**
	 * Get field at given position as a long, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract Long getLong(String ls, int pos, String field);

	/**
	 * Get field "value" at given position as a long, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract Long getLong(String ls, int pos);

	/**
	 * Get field at given position as an int, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract Integer getInt(String ls, int pos, String field);

	/**
	 * Get the field "value" at given position as an int, or null
	 * 
	 * @param ls
	 * @param field
	 * @param pos
	 * @return
	 */
	public abstract Integer getInt(String ls, int pos);

	/**
	 * Get Error nummber
	 */
	public abstract int getError();

	/**
	 * Check if in error state
	 */
	public abstract boolean isError();

	/**
	 * Check if is a variable
	 */
	public abstract boolean isVar(String variable);

	/**
	 * Check if it is a list
	 */
	public abstract boolean isList(String list);

	/**
	 * Check if it is a list with enough rows
	 */
	public abstract boolean isList(String list, int n);

	/**
	 * Check if exists as a list column
	 */
	public abstract boolean isListCol(String list, String field);

	/**
	 * Check if exists as an object
	 */
	public abstract boolean isObj(String object);

	/**
	 * Check if we are in the insite editing mode
	 */
	public abstract boolean isInsite();

	/**
	 * Get an object
	 */
	public abstract Object getObject(String object);

	/**
	 * Return the asset identified by c/cid (or null if not possible)
	 * 
	 */
	public abstract Asset getAsset(String c, Long cid);

	/**
	 * Return the asset identified by and Id (or null if not found)
	 * 
	 */
	public abstract Asset getAsset(Id id);

	/**
	 * Return the asset identified by the current c/cid
	 */
	public abstract Asset getAsset();

	/**
	 * Return current "c" (content type)
	 * 
	 * @return
	 */
	public abstract String getC();

	/**
	 * Return current "cid" (content id)
	 */
	public abstract Long getCid();

	/**
	 * Return current asset id
	 */
	public abstract Id getId();

	/**
	 * Return the current config
	 */
	public abstract Config getConfig();

	/**
	 * Return the current router
	 */
	public abstract Router getRouter();

	/**
	 * The current site name
	 */
	public abstract String getSiteName();

	/**
	 * The current site id
	 */
	public abstract String getSiteId();

	public abstract SitePlan getSitePlan();

	/**
	 * Return the URL to render this asset - note that rendering is different if
	 * we are in insite/preview mode or in live mode
	 */
	public abstract String getUrl(Id id, Arg... args);

	/**
	 * Return the URL to render this asset
	 */
	public abstract String getUrl(String c, Long cid, Arg... args);

	/**
	 * Find assets
	 */
	public abstract List<Id> find(String type, Arg... args);

	/**
	 * Find one assets
	 */
	public abstract Asset findOne(String type, Arg... args);

	/**
	 * Call a CS Element in current site
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public abstract String call(String name, Arg... args);

	/**
	 * Clear the current error code
	 */
	public abstract void clearError();

	/**
	 * Add a dependency on anything
	 * 
	 */
	public abstract void addDependency();

	/**
	 * Add a dependency on any asset of the given type
	 * 
	 * @param c
	 */
	public abstract void addDependency(String c);

	/**
	 * Add an EXACT dependency on a given asset
	 * 
	 * @param c
	 * @param cid
	 */
	public abstract void addDependency(String c, Long cid);

	/**
	 * Add a dependency of the specified type on a given asset
	 * 
	 * @param c
	 * @param cid
	 */
	public abstract void addDependency(String c, Long cid, AssetDeps deps);

	/**
	 * Add an EXACT dependency on a given asset
	 * 
	 * @param id
	 */
	public abstract void addDependency(Id id);

	/**
	 * Add a dependency of the specified type on a given asset
	 * 
	 * @param id
	 * @param deps
	 */
	public abstract void addDependency(Id id, AssetDeps deps);

	/**
	 * Check if the given attribute is a valid value
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract boolean exists(String attribute);

	/**
	 * Check if the given attribute at the given position is a valid value
	 * 
	 * @param attribute
	 * @return
	 */
	public abstract boolean exists(String attribute, int pos);

}