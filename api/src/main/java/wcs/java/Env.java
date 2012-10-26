package wcs.java;

import java.util.List;
import java.util.Map;
import wcs.core.ICSProxyJ;
import wcs.java.Util.Arg;
import wcs.java.Util.Qid;
import COM.FutureTense.Interfaces.ICS;

import static wcs.java.Util.qid;

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
	 * Get a variable
	 * 
	 * @param var
	 * @return
	 */
	public String getVar(String var) {
		return ics.GetVar(var);
	}

	/**
	 * Get a List as a list of maps... as it should be
	 * 
	 * @param list
	 * @return
	 */
	public List<Map<String, Object>> getList(String list) {
		// TODO
		return null;// ics.GetList(list);
	}

	/**
	 * Get an asset by id
	 * 
	 * @param id
	 * @return
	 */
	public Asset getAsset(Qid id) {
		// TODO
		return null;
	}

	/**
	 * Get the current id (that is, c/cid)
	 * 
	 * @return
	 */
	public Qid getQid() {
		return qid(getVar("c"), Long.parseLong(getVar("cid")));
		// TODO logic to get the subtype
	}

	/**
	 * Call a template
	 * 
	 * @param id
	 * @param name
	 * @param args
	 */
	public void call(Qid id, String name, Arg... args) {
		// TODO
	}

}
