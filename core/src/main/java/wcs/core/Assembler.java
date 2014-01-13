package wcs.core;

import wcs.Api;
import wcs.api.Log;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.Definition.ContainerType;
import com.fatwire.cs.core.uri.QueryAssembler;
import com.fatwire.cs.core.uri.Simple;

public class Assembler implements com.fatwire.cs.core.uri.Assembler {

	public static URI buildUri(String prefix, String suffix) {
		if (log.debug())
			log.debug("buildUri: %s / %s", prefix, suffix);
		try {
			URI uri = new URI(prefix);
			uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
					uri.getPort(), uri.getPath() + Api.nn(suffix), null, null);
			return uri;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	private URI buildBlobUri(String prefix, String blobId) {
		try {
			if (prefix == null)
				return null;
			URI uri = new URI(prefix);
			uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
					uri.getPort(), uri.getPath() + "/" + blobId, null, null);
			return uri;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	final static Log log = Log.getLog(Assembler.class);

	private QueryAssembler qa = new QueryAssembler();
	private Map<String, String> sitePrefix = new HashMap<String, String>();
	private Map<String, String> siteName = new HashMap<String, String>();
	private Pattern sitePattern = Pattern
			.compile("^.*?/(ContentServer|BlobServer|Satellite)/([a-z0-9]+)(/(.*))?$");
	private Pattern staticBlobs = null;
	private Pattern flexBlobs = null;

	@Override
	public void setProperties(Properties prp) {
		// log.debug("Satellite.setProperties=" + prp);
		try {
			staticBlobs = Pattern.compile(prp
					.getProperty("agilesites.blob.static"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (log.debug())
			log.debug("staticBlobs=" + staticBlobs);
		try {
			flexBlobs = Pattern
					.compile(prp.getProperty("agilesites.blob.flex"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (log.debug())
			log.debug("flexBlobs=" + flexBlobs);
		try {
			// look for sites
			Enumeration<?> keys = prp.keys();
			while (keys.hasMoreElements()) {

				String key = keys.nextElement().toString();
				if (!(key.startsWith("agilesites.site.") || key
						.startsWith("agilesites.name.")))
					continue;

				String site = key.substring("agilesites.1234.".length());
				String value = prp.getProperty(key);
				if (log.trace())
					log.trace("%s: %s", key, value);

				if (value != null) {
					if (key.startsWith("agilesites.site")) {
						sitePrefix.put(site, value);
					}
					if (key.startsWith("agilesites.name")) {
						siteName.put(site, value);
					}
				} else if (log.debug())
					log.debug("no site name or prefix for " + site);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(ex, "Assembler.setProperties");
		}
		if (log.debug())
			log.debug("sitePrefixes=" + sitePrefix);

		try {
			qa.setProperties(prp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// create an asset definition that will bring into the path
	private Definition assetDef(URI uri, String site, String path, String d) {
		Simple def = new Simple(false, //
				Definition.SatelliteContext.SATELLITE_SERVER, //
				Definition.ContainerType.SERVLET, //
				uri.getScheme(), //
				uri.getAuthority(),//
				Definition.AppType.CONTENT_SERVER,//
				uri.getFragment());

		def.setQueryStringParameter("pagename", "AAAgileRouter");
		def.setQueryStringParameter("site", site);

		if (d != null)
			def.setQueryStringParameter("d", d);
		if (path != null)
			def.setQueryStringParameter("url", path);
		String q = uri.getRawQuery();
		if (q != null)
			def.setQueryStringParameter("q", q);

		if (log.trace())
			log.trace("%s=%s %s=%s q=%s", "site", site, "url", path, q);

		return def;

	}

	private void addQueryString(Simple def, String query) {
		// encode the query string additional parameters for blobs
		if (query != null)
			for (String pair : query.split("&")) {
				try {
					int pos = pair.indexOf("=");
					if (pos == -1) {
						String k = URLDecoder.decode(pair, "UTF-8");
						def.setQueryStringParameter(k, "");
						if (log.trace())
							log.trace("query string: %s", k);
					} else {
						String k = URLDecoder.decode(pair.substring(0, pos),
								"UTF-8");
						String v = URLDecoder.decode(pair.substring(pos + 1),
								"UTF-8");
						def.setQueryStringParameter(k, v);
						if (log.trace())
							log.trace("query string: %s = %v ", k, v);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
	}

	// blobcol / blobkey / blobwhere / blobtable / blobnocache
	// http://localhost:8080/ss/Satellite?blobkey=id&blobnocache=true&blobwhere=1251873748810&blobheader=image%2Fpng&blobcol=urldata&blobtable=StaticFile
	// http://localhost:8080/ss/Satellite/urldata/id/1251873748810/StaticFile/true/hello.png
	private Definition blobDef(URI uri, String subpath, boolean isSt) {
		Simple def = new Simple(false, //
				Definition.SatelliteContext.SATELLITE_SERVER, //
				Definition.ContainerType.SERVLET, //
				uri.getScheme(), //
				uri.getAuthority(), //
				Definition.AppType.BLOB_SERVER, //
				uri.getFragment());

		String blobcol = isSt ? "urldata" : "url";
		String blobkey = isSt ? "id" : "name";
		String blobwhere = subpath.substring(0, subpath.indexOf("/"));
		String blobtable = isSt ? "MungoBlobs" : "Static";

		def.setQueryStringParameter("blobcol", blobcol);
		def.setQueryStringParameter("blobkey", blobkey);
		def.setQueryStringParameter("blobwhere", blobwhere);
        def.setQueryStringParameter("blobtable", blobtable);
        def.setQueryStringParameter("ssbinary", "true");

		if (log.trace())
			log.trace("blobcol=%s blobkey=%s blowhere=%s blobtable=%s",
					blobcol, blobkey, blobwhere, blobtable);

		addQueryString(def, uri.getQuery());
		return def;
	}

	/**
	 * Disassemble blob - null if it is not possible
	 * 
	 * @param uri
	 * @param site
	 * @param subpath
	 * @return
	 */
	private Definition disassembleBlob(URI uri, String site, String subpath) {
		if (subpath == null)
			return null;
		if (flexBlobs != null) {
			Matcher mFlex = flexBlobs.matcher(subpath);
			if (mFlex.find()) {
				if (log.trace())
					log.trace("flexBlob subpath=%s", subpath);
				return blobDef(uri, subpath, false);
			}
		}
		if (staticBlobs != null) {
			Matcher mStatic = staticBlobs.matcher(subpath);
			if (mStatic.find()) {
				if (log.trace())
					log.trace("staticBlob subpath=%s", subpath);
				return blobDef(uri, subpath, true);
			}
		}
		return null;
	}

	@Override
	public Definition disassemble(URI uri, ContainerType type)
			throws URISyntaxException {

		// get path
		Definition result = null;
		String path = uri.getPath();
		if (log.trace())
			log.debug("dissassembling %s", path);

		// search if it is one of the configured sites
		Matcher match = sitePattern.matcher(path);
		if (match.find()) {
			String site = match.group(2);
			String subpath = match.group(4);
			if (siteName.containsKey(site) && sitePrefix.containsKey(site)) {
				// check if it is one of the blobs
				site = siteName.get(site);
				if (log.debug())
					log.debug("site=%s subpath=%s", site, subpath);
				result = disassembleBlob(uri, site, subpath);
				if (result == null) {
					if (log.debug())
						log.debug("**** asset found");
					return assetDef(uri, site, subpath, getDeviceParam(uri));
				} else {
					if (log.debug())
						log.debug("*** blob found");
					return result;
				}
			} else {
				log.warn("no prefix for site " + site);
			}
		}
		return qa.disassemble(uri, type);
	}

	@Override
	public URI assemble(Definition def) throws URISyntaxException {

		if ("BlobServer".equalsIgnoreCase(def.getAppType().toString())) {
			String site = def.getParameter("blobkey");
			String prefix = sitePrefix.get(site);
			String where = def.getParameter("blobwhere");
			if (log.debug())
				log.debug("assemble: buildBlobUri prefix=%s, where=%s", prefix,
						where);
            URI uri =  buildBlobUri(prefix, where);
            return uri != null ? uri : qa.assemble(def);
		}

		// get site/url, fallback to c/cid if not found
		String site = def.getParameter("site");
		if (site == null)
			site = def.getParameter("c");

		String url = def.getParameter("url");
		if (url == null)
			def.getParameter("cid");

		String rendermode = def.getParameter("rendermode");
		if (rendermode == null)
			rendermode = "live";

		if (log.debug())
			log.debug("assemble: site=%s url=%s rendermode=%s", site, url,
					rendermode);

		if (!rendermode.equals("live"))
			return qa.assemble(def);

		String prefix = sitePrefix.get(site);
		if (prefix != null) {
			try {
				// the prefix is supposed to be already properly encoded...
				return buildUri(prefix, url);
			} catch (Exception ex) {
				log.trace("unencodable url!", ex);
			}
		}
		return qa.assemble(def);
	}

	final static Pattern devicePattern = Pattern.compile("(^d=|\\&d=)(.+$)");

	private String getDeviceParam(URI uri) {
		String query = uri.getRawQuery();
		if (query == null) {
			return null;
		}
		Matcher m = devicePattern.matcher(query);
		if (m.find()) {
			String val = m.group(2);
			return val.substring(0, val.indexOf('&'));
		}
		return null;
	}
}
