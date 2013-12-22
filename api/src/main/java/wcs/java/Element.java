package wcs.java;

import static wcs.Api.*;
import wcs.Api;
import wcs.api.Arg;
import wcs.api.Log;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import COM.FutureTense.Interfaces.ICS;
import wcs.java.util.StringUtils;

/**
 * 
 * This class implements element logic and will be invoked by templates and
 * cselements
 * 
 * @author msciab
 * 
 */
public abstract class Element implements wcs.api.Element {

	static Log log = Log.getLog(Element.class);

	// current site
	protected String site;

	protected boolean insite = false;

	/**
	 * Execute the element
	 * 
	 * The bulk of the method is streaming the result and invoking embedded
	 * method calls.
	 * 
	 */
	@Override
	public String exec(ICS ics) {
		try {
			Env env = new Env(ics);
			site = ics.GetVar("site");
			insite = ics.GetVar("rendermode") != null
					&& ics.GetVar("rendermode").equals("insite");

			String device = ics.GetVar("d");
			if (device == null) {
				String packedargs = env.getString("packedargs");
				env.unpackVar("d", packedargs);
				device = ics.GetVar("d");
			}
			return callApply(env, device);
		} catch (Exception ex) {
			String msg = ex.getMessage();
			msg = msg == null ? "NULL" : msg;
			log.error(ex, msg);
			return "<div style='font-color: red'>" //
					+ msg //
					+ "<span style='display: none'>" + ex2str(ex) //
					+ "</span></div>";
		}
	}

	/**
	 * Generic CallElement
	 * 
	 * @param name
	 * @param args
	 * @return
	 */
	public String call(String name, Arg... args) {
		List<Arg> list = new LinkedList<Arg>();
		for (Arg arg : args)
			list.add(arg);
		list.add(arg("ELEMENT", name));
		return Api.call("ICS:CALLELEMENT", list);
	}

	public String callApply(Env env, String device) {
		if (device == null || device.length() == 0) {
			return apply(env);
		}
		try {
			Method method = this.getClass()
					.getMethod("apply" + StringUtils.capitalize(device),
							wcs.api.Env.class);
			return (String) method.invoke(this, env);

		} catch (Exception ex) {
			log.error("could not call method apply"
					+ StringUtils.capitalize(device)
					+ "(). Calling default apply() method instead");
			return apply(env);
		}
	}

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	abstract public String apply(wcs.api.Env e);

}
