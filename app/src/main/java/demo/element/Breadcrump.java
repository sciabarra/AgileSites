package demo.element;
import static wcs.core.Common.*;
import static wcs.core.Log.*;
import wcs.core.Log;
import wcs.core.Index;
import wcs.core.Asset;
import wcs.core.SitePlan;
import wcs.core.Id;
import wcs.java.Picker;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;
import static java.lang.String.format;

@Index("demo/elements.txt")
public class Breadcrump extends Element {

	final static Log log = Log.getLog(Breadcrump.class);

	public static AssetSetup setup() {
		return new CSElement("DmBreadcrump", demo.element.Breadcrump.class);
	}

	@Override
	public String apply(Env e) {

		log.debug("Testing Breadcrump");

		Picker html = Picker.load("/blueprint/template.html", "#breadcrump");
		StringBuilder sb = new StringBuilder();
		String sep = "&nbsp;&raquo;&nbsp;";

		Asset a = e.getAsset();
		log.trace("id="+a.getId());
		SitePlan sp = e.getSitePlan().goTo(a.getId());

		Id[] path = sp.path();
		log.trace("path len=%d", path.length);
		for(int i = path.length-1; i>=0; i--) {
			Id id = path[i];
			log.trace("id: %s", id);
			if (!id.c.equals("Publication")) {
				String name = e.getAsset(id).getName();
				sb.append(format("<a href='%s'>%s</a>%s",  e.getUrl(id), name, sep));
			}
		}
		sb.append("<b>").append(a.getName()).append("</b>");
		return html.replace("#breadcrump", sb.toString()).dump(log).html();
	}
}
