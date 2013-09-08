package wcs.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wcs.core.Id;
import wcs.core.Common;
import wcs.core.tag.AssetTag;
import wcs.core.tag.SiteplanTag;
import wcs.core.Log;
import COM.FutureTense.Interfaces.ICS;

import javax.annotation.Resource;

public class SitePlan {

	final static Log log = Log.getLog(SitePlan.class);

    private Env e;

	private Id currentId;
	private String currentNid;

	/**
	 * Create a siteplan object that points to the root (the publication node)
	 * 
	 */
	public SitePlan(Env env) {
		this.e = env;
        //goTo(new Id("Publication", Long.parseLong(e.getSiteId())));
    }

	/**
	 * Return id of the current page
	 * 
	 * @return
	 */
	public Id current() {
        if (currentId == null) {
            goTo(new Id("Publication", Long.parseLong(e.getSiteId())));
         }
        return currentId;
    }

    public String getCurrentNid() {
        if (currentNid == null) {
            goTo(new Id("Publication", Long.parseLong(e.getSiteId())));
        }
        return currentNid;
    }

    private ICS getIcs() {
        return e.ics;
    }
	/**
	 * Move to the page identified by the id
	 * 
	 * @param id
	 * @return
	 */
	public SitePlan goTo(Id id) {

		String list = Common.tmp();
		String name = Common.tmp();

		if (id.c.equals("Publication")) {
			SiteplanTag.root().objectid(id.cid.toString()).list(list).run(getIcs());
			currentId = new Id(e.getString(list, "otype"), e.getLong(list,
					"oid"));
			currentNid = e.getString(list, "nid");
		} else {
			AssetTag.load().name(name).type(id.c).objectid(id.cid.toString())
					.run(getIcs());
			currentNid = AssetTag.getsitenode().name(name).eval(getIcs(), "output");
		}
		log.trace("SitePlan.goTo: id=%s nid=%s", currentId.toString(),
				currentNid);
		return this;
	}

	/**
	 * 
	 * Get the children of a page
	 * 
	 * @return
	 */
	public Id[] children() {
		log.trace("children!");
		String name = Common.tmp();
		String list = Common.tmp();
		SiteplanTag.load().name(name).nodeid(getCurrentNid()).run(getIcs());
		SiteplanTag.children()//
				.name(name).code("Placed").order("nrank") //
				.list(list).run(getIcs());
		return ilist2aid(list);
	}

	/**
	 * 
	 * Get the path of a page up to the root
	 * 
	 * @return
	 */
	public Id[] path() {
		log.trace("path");
		String name = Common.tmp();
		String list = Common.tmp();
		SiteplanTag.load().name(name).nodeid(getCurrentNid()).run(getIcs());
		SiteplanTag.nodepath()//
				.name(name) //
				.list(list).run(getIcs());
		return ilist2aid(list);
	}

	private Id[] ilist2aid(String list) {
		try {
			Id[] id = new Id[e.getSize(list)];
			log.trace("children #%d", id.length);
			for (int i : e.getRange(list)) {
				String c = e.getString(list, i, "otype");
				Long cid = e.getLong(list, i, "oid");
				id[i - 1] = new Id(c, cid);
				log.trace("%s", id[i - 1].toString());
			}
			return id;
		} catch (Exception ex) {
			log.trace(ex, "ops");
			return new Id[0];
		}
	}

}
