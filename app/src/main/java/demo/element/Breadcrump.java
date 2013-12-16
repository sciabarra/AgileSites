package demo.element;

import static java.lang.String.format;
import wcs.api.Asset;
import wcs.api.Env;
import wcs.api.Id;
import wcs.api.Index;
import wcs.api.Log;
import wcs.api.SitePlan;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Picker;

@Index("demo/elements.txt")
public class Breadcrump extends Element {

	final static Log log = Log.getLog(Breadcrump.class);

	public static AssetSetup setup() {
		return new CSElement("Breadcrump", demo.element.Breadcrump.class);
	}

	@Override
	public String apply(Env e) {

		if (log.debug())
			log.debug("Demo Breadcrump");

		Picker html = Picker.load("/blueprint/template.html", "#breadcrump");
		StringBuilder sb = new StringBuilder();
		String sep = "&nbsp;&raquo;&nbsp;";

		Asset a = e.getAsset();
		if (log.trace())
			log.trace("id=" + a.getId());
		SitePlan sp = e.getSitePlan().goTo(a.getId());

		Id[] path = sp.path();
		if (log.trace())
			log.trace("path len=%d", path.length);
		for (int i = path.length - 1; i >= 0; i--) {
			Id id = path[i];
			if (log.trace())
				log.trace("id: %s", id);
			if (!id.c.equals("Publication")) {
				String name = e.getAsset(id).getName();
				sb.append(format("<a href='%s'>%s</a>%s", e.getUrl(id), name,
						sep));
			}
		}
		sb.append("<b>").append(a.getName()).append("</b>");
		return html.replace("#breadcrump", sb.toString()).dump(log).html();
	}
}
