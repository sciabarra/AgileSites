package wcs.java;

import wcs.core.Arg;
import wcs.core.Call;

public class Insite extends Call {

	public Insite(Arg... args) {
		super("INSITE:EDIT");
	}

	public Insite() {
		super("INSITE:EDIT");
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

	/*
	 * public Edit value(String value) { addArg("VALUE", value); return this; }
	 */

	/*
	 * public Edit field(String field) { addArg("FIELD", field); return this;
	 * }/*
	 */

	/*
	 * public Edit assetid(String assetid) { addArg("ASSETID", assetid); return
	 * this; }/*
	 */

	/*
	 * public Edit assettype(String assettype) { addArg("ASSETTYPE", assettype);
	 * return this; }/*
	 */

	/*
	 * public Edit width(String width) { addArg("WIDTH", width); return this;
	 * }/*
	 */

	/*
	 * public Edit height(String height) { addArg("HEIGHT", height); return
	 * this; }/*
	 */
}
