package hubby.element;
import static wcs.Api.*;
import wcs.api.Log;
import wcs.api.Index;
import wcs.api.Asset;
import wcs.api.Model;
import wcs.api.Env;
import wcs.java.Picker;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.SiteEntry;
import wcs.java.Element;

@Index("hubby/elements.txt")
public class Wrapper extends Element {
	private final static Log log = Log.getLog(Wrapper.class);
	public static AssetSetup setup() {
		return new CSElement("Wrapper", hubby.element.Wrapper.class,
				new SiteEntry("", true, "Hubby/Hubby_Wrapper"));
	}

	@Override
	public String apply(Env e) {
		if(log.trace())
			log.trace("Hubby Wrapper");
		
		// change relative references to absolute
		Picker html = Picker.load("/hubby/index.html");
		html.prefixAttrs("link[rel=stylesheet]", "href", "/cs/hubby/");
		html.prefixAttrs("script", "src", "/cs/hubby/");

		// handle generic errors
		if (e.isVar("error")) {			
			Model m = model(arg("name", "Error"), arg("description", e.getString("error"))); 
			return html.replace("body", e.call("Error",// 
							arg("error", e.getString("error"))))
							.outerHtml(m);
		}

		// handle asset not found or template not found
		Asset a = e.getAsset();
		if (a == null || a.getTemplate()==null || a.getTemplate().trim().length()==0) {
			String error = (a==null) ? "Asset not found" : "Layout not found";
			Model m = model(arg("name", "Error"), arg("description", error));
			return html.replace("body", //
					e.call("Error",arg("error", error)))//
					.outerHtml(m);
		}

		// render the asset using his default template
		Model m = model(arg("name",a.getName()), arg("description", a.getDescription())); 
		html.replace("body", a.call(a.getTemplate()));
		return html.outerHtml(m);
	}
}
