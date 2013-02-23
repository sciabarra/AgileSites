package agilewcs;

import wcs.java.Env;
import wcs.java.util.QueryString;

public class Router extends wcs.java.Router {

	@Override
	public String route(Env env, String path, QueryString qs) {

		StringBuilder sb = new StringBuilder();
		sb.append("<h1>").append(path).append("</h1>");
		sb.append("<p>").append(qs.getMap().toString()).append("</p>");
		return sb.toString();

	}

}
