package wcs.java.util;

import COM.FutureTense.Interfaces.FTValList;
import COM.FutureTense.Interfaces.ICS;

/**
 * Presentation object, helper for Attribute Editors
 * 
 * @author msciab
 * 
 */
public class Presentation {
	private ICS ics;
	private FTValList args = new FTValList();

	public Presentation(ICS ics) {
		this.ics = ics;
	}

	public String get(String presInst, String attribute) {
		String tmp = Util.tmpVar();
		args.removeAll();
		args.setValString("NAME", presInst);
		args.setValString("ATTRIBUTE", attribute);
		args.setValString("VARNAME", tmp);
		ics.runTag("presentation.getprimaryattributevalue", args);
		String result = ics.GetVar(tmp);
		ics.RemoveVar(tmp);
		return result;
	}
}
