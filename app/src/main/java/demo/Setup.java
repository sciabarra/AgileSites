package demo;

import java.util.List;

import wcs.java.util.Util;
import static wcs.java.util.Util.listString;

public class Setup extends wcs.java.Setup {

	@Override
	public Long getId() {
		return 1351275793143l;
	}

	@Override
	public String getName() {
		return Config.site;
	}

	@Override
	public String getDescription() {
		return Config.site;
	}
	
	
	@Override
	public List<String> getUsers() {
		return listString("fwadmin");
	}

	@Override
	public List<String> getUserRoles(String arg0) {
		return listString("AdvancedUser", "GeneralAdmin", "SiteAdmin");
	}
	
	
	@Override
	public List<String> getAssetTypes() {
		return listString("Static", "Page", "PageAttribute", "PageDefinition", "SiteEntry", "Template", "CSElement", "AttrTypes");
	}

	@Override
	public Class<?>[] getAssets() {
		return Util.classesFromResource(Config.site, "elements.txt");
	}
}
