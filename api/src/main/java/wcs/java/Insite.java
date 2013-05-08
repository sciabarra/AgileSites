package wcs.java;

import wcs.core.Arg;
import wcs.core.Call;

public class Insite extends Call {

	public Insite(Arg... args) {
		super("INSITE:EDIT");
	}

	public Insite() {
		super("INSITE:EDIT");
		//addArg("MODE", "text");
		//addArg("EDITOR","html");
		//addArg("PARAMS", "{}");
	}

	public Insite c(String c) {
		addArg("C", c);
		return this;
	}

	public Insite cid(String cid) {
		addArg("CID", cid);
		return this;
	}

	public Insite editor(String editor) {
		addArg("EDITOR", editor);
		return this;
	}

	public Insite mode(String mode) {
		addArg("MODE", mode);
		return this;
	}

	public Insite params(String params) {
		addArg("PARAMS", params);
		return this;
	}
}
