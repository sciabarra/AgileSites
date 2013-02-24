package agilewcs;

public class Config extends wcs.java.Config {

	@Override
	public String getAttributeType(String type) {
		if (type == null)
			return null;

		if (type.equals("Page"))
			return "PageAttribute";

		
		return null;
	}

}
