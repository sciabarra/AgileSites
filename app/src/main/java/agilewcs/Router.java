package agilewcs;

import wcs.java.Env;
import wcs.java.util.QueryString;

public class Router extends wcs.java.Router {

	@Override
	public String route(Env env, String[] path, QueryString qs) {

		StringBuffer sb = new StringBuffer();
		sb.append("<ul>");
		for (String s : path) {
			sb.append("<li>").append(s).append("</li>");
		}
		sb.append("</ul>");
		sb.append("<p>").append(qs.getMap().toString()).append("</p>");
		return sb.toString();

	}

}
