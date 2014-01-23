package wcs.java;

import static wcs.Api.*;
import wcs.api.Env;
import wcs.api.Id;
import wcs.api.Log;
import wcs.core.tag.AssetTag;
import wcs.core.tag.SiteplanTag;
import COM.FutureTense.Interfaces.ICS;
import java.util.LinkedList;
import java.util.List;

public class SitePlan implements wcs.api.SitePlan {

	final static Log log = Log.getLog(SitePlan.class);

	private Env e;
	private ICS i;

	private Id currentId;
	private String currentNid;

	/**
	 * Create a siteplan object that points to the root (the publication node)
	 * 
	 * @param e
	 */
	public SitePlan(Env e) {
		this.e = e;
		this.i = e.ics();
		goTo(e.getSitePlanRoot(e.getSiteName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.ISitePlan#current()
	 */
	@Override
	public Id current() {
		return currentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.ISitePlan#goTo(wcs.core.Id)
	 */
	@Override
	public SitePlan goTo(Id id) {

		String list = tmp();
		String name = tmp();

		if (id.c.equals("Publication")) {
			SiteplanTag.root().objectid(id.cid.toString()).list(list).run(i);
			currentId = new Id(e.getString(list, "otype"), e.getLong(list,
					"oid"));
			currentNid = e.getString(list, "nid");
		} else if (id.c.equals("SitePlan")) {
			AssetTag.load().name(name).type(id.c).objectid(id.cid.toString())
					.run(i);
			currentNid = AssetTag.getsitenode().name(name).eval(i, "output");
			currentId = id;
		} else {
			AssetTag.load().name(name).type(id.c).objectid(id.cid.toString())
					.run(i);
			currentNid = AssetTag.getsitenode().name(name).eval(i, "output");
		}
		log.trace("SitePlan.goTo: id=%s nid=%s", currentId.toString(),
				currentNid);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.ISitePlan#children()
	 */
	@Override
	public Id[] children() {
		if (log.trace())
			log.trace("children!");
		String name = tmp();
		String list = tmp();
		SiteplanTag.load().name(name).nodeid(currentNid).run(i);
		SiteplanTag.children()//
				.name(name).code("Placed").order("nrank") //
				.list(list).run(i);
		return ilist2aid(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.ISitePlan#path()
	 */
	@Override
	public Id[] path() {
		if (log.trace())
			log.trace("path");
		String name = tmp();
		String list = tmp();
		SiteplanTag.load().name(name).nodeid(currentNid).run(i);
		SiteplanTag.nodepath()//
				.name(name) //
				.list(list).run(i);
		return ilist2aid(list);
	}

	private Id[] ilist2aid(String list) {
		try {
			List<Id> nodes = new LinkedList<Id>();
			// Id[] id = new Id[e.getSize(list)];
			if (log.trace())
				log.trace("children #%d", e.getSize(list));
			for (int i : e.getRange(list)) {
				String c = e.getString(list, i, "otype");
				Long cid = e.getLong(list, i, "oid");
				// id[i - 1] = new Id(c, cid);
				// stop at the SitePlan node
				if (c.equalsIgnoreCase("SitePlan")) {
					break;
				}
				nodes.add(i - 1, new Id(c, cid));
				log.trace("%s", nodes.get(i - 1).toString());
			}
			return nodes.toArray(new Id[nodes.size()]);
		} catch (Exception ex) {
			log.trace(ex, "ops");
			return new Id[0];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wcs.java.ISitePlan#children()
	 */
	public Id[] descendant(int level) {
		if (log.trace())
			log.trace("all pages!");
		String name = tmp();
		String list = tmp();
		SiteplanTag.load().name(name).nodeid(currentNid).run(i);
		SiteplanTag.listpages().name(name).placedlist(list).level("" + level)
				.run(i);
		List<Id> nodes = new LinkedList<Id>();
		for (int i : e.getRange(list)) {
			String c = e.getString(list, i, "AssetType");
			Long cid = e.getLong(list, i, "Id");
			nodes.add(i - 1, new Id(c, cid));
		}
		return nodes.toArray(new Id[nodes.size()]);
	}

}
