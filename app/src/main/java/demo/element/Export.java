package demo.element;

import wcs.core.Log;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.SiteEntry;

public class Export extends Element {

	final static Log log = Log.getLog(Export.class);

	public static AssetSetup setup() {
		return new CSElement("DmExport", demo.element.Export.class,
				new SiteEntry("demo-export", true, "Demo/DmExport"));
	}

	@Override
	public String apply(Env e) {
		log.debug("Testing Export");

		StringBuilder sb = new StringBuilder();
		sb.append("hello");
		
		
		String res = e.sql("select * from AssetPublication");
		for(int i: e.getRange(res)) {
			sb.append(e.getString(res, i, "xxx"));
		}
		
		return sb.toString();

		// sample logic with the html picker
		// Picker html = Picker.load("/demo/index.html" , "#content");
		// html.replace("#title", e.getString("Title"));
		// return html/*.dump(log)*/.html();
	}

}
