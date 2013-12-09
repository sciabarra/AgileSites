package demo;

public class Config extends wcs.java.Config {
	
	// configure sitename and attribute type
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

		return null;
	}

}
