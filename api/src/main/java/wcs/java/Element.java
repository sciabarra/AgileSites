package wcs.java;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import wcs.core.Arg;
import wcs.core.Common;
import wcs.core.Log;
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
public abstract class Element implements wcs.core.Element {

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
			site = ics.GetVar("site");
			insite = ics.GetVar("rendermode") != null
					&& ics.GetVar("rendermode").equals("insite");
            Env env = new Env(ics, site);
            String device = ics.GetVar("d");
            if (device == null) {
                String packedargs = env.getString("packedargs");
                env.unpackVar("d", packedargs);
                device = ics.GetVar("d");
            }
			return callApply(env, device);
		} catch (NullPointerException npe) {
			log.error(npe, "NPE: ");
			return "NULL <span style='display: none'>" + Common.ex2str(npe)
					+ "</span>";
		} catch (Exception ex) {
			log.error(ex, "exception in element");
			return ex.getMessage();
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
		list.add(Common.arg("ELEMENT", name));
		return Common.call("ICS:CALLELEMENT", list);
	}

	/**
	 * Convenience method for defining args
	 * 
	 * @param k
     * @param v
	 * @return
	 */
	public Arg arg(String k, String v) {
		return Common.arg(k, v);
	}

	/**
	 * Convenience method to log in a functional way
	 * 
	 * @param msg
	 * @return
	 */
	public String log(String msg) {
		log.debug(msg);
		return msg;
	}

	/**
	 * The method to be overriden by an implementing template
	 * 
	 * @param env
	 * @return
	 */
	public String callApply(Env env, String device) {
        if (device == null || device.length() == 0) {
            return apply(env);
        }
        try {
            Method method = this.getClass().getMethod("apply" + StringUtils.capitalize(device), Env.class);
            return (String) method.invoke(this,env);

        } catch (Exception ex) {
            log.error("could not call method apply" + StringUtils.capitalize(device) + "(). Calling default apply() method instead");
            return apply(env);
        }
    }

    abstract public String apply (Env e);

}
