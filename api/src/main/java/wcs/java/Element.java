package wcs.java;
import static wcs.Api.*;
import wcs.Api;
import wcs.api.Arg;
import wcs.api.Log;
import java.util.LinkedList;
import java.util.List;
import COM.FutureTense.Interfaces.ICS;

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
			return apply(env);
		} catch (NullPointerException npe) {
			log.error(npe, "NPE: ");
			return "NULL <span style='display: none'>" + ex2str(npe)
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
		list.add(arg("ELEMENT", name));
		return Api.call("ICS:CALLELEMENT", list);
	}

	/**
	 * Convenience method for defining args
	 * 
	 * @param name
	 * @return
	 */
	public Arg arg(String k, String v) {
		return arg(k, v);
	}

	/**
	 * Convenience method to log in a functional way
	 * 
	 * @param name
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
	abstract public String apply(wcs.api.Env env);

}
