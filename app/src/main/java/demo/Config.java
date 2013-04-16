package demo;

public class Config extends wcs.java.Config {

	public static final String site = "Demo";

	@Override
	public String getSite() {
		return site;
	}

	@Override
	public String getAttributeType(String type) {
		if (type == null)
			return null;

		// simple configuration for Pages
		if (type.equals("Page"))
			return "PageAttribute";

		// sample configuration for flex family
		// assuming the types in the flex family
		// shares a common prefix
		/**
		 * if(type.startsWith("MySite_")) return "MySite_A";
		 */

		return null;
	}

}
