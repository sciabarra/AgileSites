package agilewcs.article;

import java.util.Date;

import wcs.java.Asset;
import wcs.java.Element;
import wcs.java.Env;
import wcs.java.Picker;

public class Layout extends Element {

	@Override
	public String apply(Env e) {
		Asset a = e.getAsset();
		//return a.getName();

		Long id = a.getId();
		String name = a.getName();
		String desc = a.getDescription();
		Date date = a.getStartDate();
		
		String author = a.getString("Author");
		Date today = a.getDate("Birthday");
		Integer amount = a.getInt("Count");

		//return id+name+desc+date;
		
		Picker p = new Picker("/index.html");
		p.attr("head link", "href", "/cs/css/default.css");
		p.attr("head link", "href", "/cs/css/default.css");
		p.replace("div#content h1.title", a.getName());
		p.replace("div#content p.meta small", "by " + a.getString("Author"));
		p.replace("div#content p.meta small", "by " + a.getString("Author"));
			
		return p.html();

		//return "hello world";
	}
}
