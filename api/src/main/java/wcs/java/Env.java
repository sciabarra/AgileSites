package wcs.java;

import java.util.List;
import java.util.Map;
import wcs.core.ICSProxyJ;
import wcs.java.Util.Arg;
import wcs.java.Util.Id;
import COM.FutureTense.Interfaces.ICS;
import wcs.java.tag.RenderTag;

import static wcs.java.Util.id;

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
	public Asset getAsset(Id id) {
		// TODO
		return null;
	}

	/**
	 * Get the current id (that is, c/cid)
	 * 
	 * @return
	 */
	public Id getId() {
		return id(getVar("c"), Long.parseLong(getVar("cid")));
		// TODO logic to get the subtype
	}

	/**
	 * Call a template
	 * 
	 * @param id
	 * @param name
	 * @param args
	 */
	public void call(String name, Id id, Arg... args) {
		//RenderTag.Calltemplate tag = RenderTag.calltemplate(name);
		//tag.c(id.type).cid(Long.toString(id.id)).run(ics);
	}
}
