package agilesites;

public class Config extends wcs.java.Config {

	public static String site = "AgileSites";

	@Override
	public String getSite() {
		return site;
	}

	@Override
	public String getAttributeType(String type) {
		if (type == null)
			return null;

		if (type.equals("Page"))
			return "PageAttribute";

		return null;
	}

}
