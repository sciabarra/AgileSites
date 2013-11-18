package demo.element;

import wcs.core.Id;
import wcs.core.Log;
import wcs.java.Asset;
import wcs.java.AssetSetup;
import wcs.java.CSElement;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;
import wcs.java.SitePlan;
import wcs.java.util.AddIndex;

@AddIndex("demo/elements.txt")
public class Tree extends Element {

	final static Log log = Log.getLog(Tree.class);
	private SitePlan sp ;
	private Env e;
	

	public static AssetSetup setup() {
		return new CSElement("DmTree", demo.element.Tree.class);
	}

	/**
	 * Recursive visit of the site plan to build the tree
	 * 
	 * @param parent
	 * @param result
	 */
	private void visit(Id parent, StringBuilder result) {
		sp.goTo(parent);
		for (Id id : sp.children()) {
			Asset a = e.getAsset(id); 
			String node = String.format(//
					"d.add(%d, %d, '%s', '%s');\n", //
					id.cid, parent.cid, a.getName(), a.getUrl());
			result.append(node);
			visit(id, result);
		}
	}

	@Override
	public String apply(Env e) {
		this.e = e;
		this.sp = e.getSitePlan();

		// get model and view
		Picker html = Picker.load("/blueprint/template.html", "#tree");

		// navigate the siteplan
		Id parent = sp.current();
		StringBuilder result = new StringBuilder("d.add("+parent.cid+", -1,'"+e.getConfig().getSite()+"');");
		visit(parent, result);

		html.replace("#tree-body", result.toString());
		return html/* .dump(log) */.html();
	}
}
