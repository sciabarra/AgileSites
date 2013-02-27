package agilewcs.typeless;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;

public class Wrapper extends Element {

	@Override
	public String apply(Env e) {

		Asset a = e.getAsset(e.getC(), e.getCid());

		
		System.out.println("---c=" + e.getC());
		System.out.println("---cid=" + e.getCid());
		System.out.println("---template=" + a.getTemplate());
		
		return a.call("AwSummary");

		// Picker p = new Picker("/agilewcs/index.html");
		// return p.html();

	}

}
