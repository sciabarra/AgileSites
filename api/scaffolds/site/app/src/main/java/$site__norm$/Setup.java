package $site;format="normalize"$;
import wcs.java.util.Util;

public class Setup extends wcs.java.Setup {
	@Override
	public Class<?>[] getAssets() {
		return Util.classesFromResource("$site$", "elements.txt");
	}
}
