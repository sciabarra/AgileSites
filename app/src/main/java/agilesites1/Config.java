package agilesites1;

public class Config extends wcs.java.Config {

	@Override
	public String getSite() {
		return "AgileSites";
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
