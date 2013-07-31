package demo.element;

import org.springframework.stereotype.Component;
import wcs.core.Id;
import wcs.java.Env;
import wcs.java.Element;
import wcs.java.CSElement;
import wcs.java.AssetSetup;
import wcs.java.Picker;
import wcs.java.SitePlan;
import wcs.core.Log;

import javax.annotation.Resource;

import static java.lang.String.format;

@Component
public class Topmenu extends Element {

	final static Log log = Log.getLog(Error.class);

    @Resource(name="env")
    Env e;

    @Resource(name="sitePlan")
    SitePlan sp;

	public static AssetSetup setup() {
		return new CSElement("DmTopmenu", demo.element.Topmenu.class);
	}

	@Override
	public String apply() {

		log.debug("Testing Topmenu");

		Picker html = Picker.load("/blueprint/template.html", "#topmenu");
		log.debug("picker=" + html);

		html.dump(log);

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
