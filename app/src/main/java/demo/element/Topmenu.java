package demo.element;

import wcs.core.Id;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;
import wcs.java.Picker;
import wcs.java.SitePlan;
import wcs.java.util.AddIndex;
import wcs.core.Log;
import static java.lang.String.format;

@AddIndex("demo/elements.txt")
public class Topmenu extends Element {

	final static Log log = Log.getLog(Error.class);

	public static AssetSetup setup() {
		return new CSElement(137502363002l, "DmTopmenu", demo.element.Topmenu.class);
	}

	@Override
	public String apply(Env e) {

		log.debug("Testing Topmenu");

		Picker html = Picker.load("/blueprint/template.html", "#topmenu");
		log.debug("picker=" + html);

		html.dump(log);

		SitePlan sp = e.getSitePlan();
		StringBuilder sb = new StringBuilder();
		Id me = e.getId();
		String sep = "&nbsp;|&nbsp;";
		for (Id id : sp.children()) {
			String name = e.getAsset(id).getName();
			if (id.equals(me)) {
				sb.append("<b>").append(name).append("</b>").append(sep);
			} else {
				sb.append(format("<a href='%s'>%s</a>%s", //
						e.getUrl(id), name, sep));
			}
		}
		if (sb.length() > 0)
			sb.setLength(sb.length() - sep.length());

		html.replace("#topmenu", sb.toString());
		return html.html();
	}
}
