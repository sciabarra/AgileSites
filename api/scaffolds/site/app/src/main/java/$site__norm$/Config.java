package $site;format="normalize"$;

public class Config extends wcs.java.Config {

	@Override
	public String getSite() {
		return "$site$";
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
		 * if(type.startsWith("Agile_"))
		 *	return "Agile_A";
		 */

		return null;
	}

}
