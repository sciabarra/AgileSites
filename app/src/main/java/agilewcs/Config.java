package agilewcs;

public class Config extends wcs.java.Config {

	@Override
	public String getAttributeType(String type) {
		if (type == null)
			return null;

		if (type.equals("Page"))
			return "PageAttribute";

		if (type.startsWith("Agile_"))
			return "Agile_A";

		return null;
	}

	@Override
	public String getDefaultTemplate(String type) {
		return "Router";
	}

}
