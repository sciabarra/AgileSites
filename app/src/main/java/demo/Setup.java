package demo;

import wcs.java.util.Util;

public class Setup extends wcs.java.Setup {

	@Override
	public Class<?>[] getAssets() {
		return Util.classesFromResource(Config.site, "elements.txt");
	}
}
