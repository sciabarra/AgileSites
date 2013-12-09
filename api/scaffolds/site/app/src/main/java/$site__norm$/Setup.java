package $site;format="normalize"$;

public class Setup extends wcs.java.Setup {

	@Override
	public Class<?>[] getAssets() {
		return Util.classesFromResource("$site$", "elements.txt");
	}
}
