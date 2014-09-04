package wcs.core;

import wcs.Api;
import wcs.api.Log;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.Definition.ContainerType;
import com.fatwire.cs.core.uri.QueryAssembler;
import com.fatwire.cs.core.uri.Simple;

public class Assembler implements com.fatwire.cs.core.uri.Assembler {

	final static Log log = Log.getLog(Assembler.class);
	private QueryAssembler qa = new QueryAssembler();
	private Map<String, String> sitePrefix = new HashMap<String, String>();
	private Map<String, String> siteName = new HashMap<String, String>();
	private Pattern sitePattern = Pattern
		.compile("^.*?/(ContentServer|BlobServer|Satellite)(/([a-z0-9]+)(/(.*))?)$");
	private Pattern flexBlobs = Pattern.compile("^/[a-z0-9]+/(\\d{6,})/.*$");
	private Pattern staticBlobs = Pattern.compile("^.*\\.(\\w+)$");
	private Pattern hashedBlob = Pattern.compile("^.*_[a-h0-9]{32}\\..*$");
	private Set staticSet;

	public Assembler() {
		initStaticSet("js,json,css,map,gif,png,jpg,jpeg,ico");
	}

	public void initStaticSet(String exts) {
		if(exts!=null) {
			StringTokenizer st = new StringTokenizer(exts, ",");
			staticSet = new HashSet<String>();
			while(st.hasMoreTokens())
				staticSet.add(st.nextToken());
		}
		if (log.debug())
			log.debug("extensions: %s ", staticSet.toString());
	}


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


	@Override
	public void setProperties(Properties prp) {
		// log.debug("Satellite.setProperties=" + prp);
		initStaticSet(prp.getProperty("agilesites.statics"));
	
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
	private Definition blobDef(URI uri, String key, boolean isSt) {
	    // hashedBlob: "^.*_[a-h0-9]{32}\\..*$"
		Simple def = new Simple(false, //
				Definition.SatelliteContext.SATELLITE_SERVER, //
				Definition.ContainerType.SERVLET, //
				uri.getScheme(), //
				uri.getAuthority(), //
				Definition.AppType.BLOB_SERVER, //
				uri.getFragment());

		boolean isHash = hashedBlob.matcher(key).find();
		String blobwhere = key ;
		String blobcol = isSt? "url" : "urldata";
		String blobkey = isSt ? (isHash ? "hashfilepath" : "filepath") : "id";
		String blobtable = isSt ? "Static" : "MungoBlobs" ;

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
	 * @param subpath without the site prefix
	 * @return
	 */
	private Definition disassembleBlob(URI uri, String path) {
		// flexBlob: "^/[a-z0-9]+/(\\d{6,})/.*$"
		if (path == null)
			return null;
		Matcher mFlex = flexBlobs.matcher(path);
		if (mFlex.find()) {
			if (log.trace())
				log.trace("flexBlob path=%s", path);
			return blobDef(uri, mFlex.group(1), false);
		}
		return null;
	}

	/**
	 * Disassemble static - null if it is not possible
	 * 
	 * @param path including the site prefix
	 *
	 * @return
	 */
	private Definition disassembleStatic(URI uri, String path) {
	    // staticBlobs: "^.*\\.(\\w+)$"
		if (path == null)
			return null;

		Matcher m = staticBlobs.matcher(path);
		if (m.find() && staticSet.contains(m.group(1).toLowerCase())) {
			if (log.trace())
				log.trace("staticBlob subpath=%s", path);
			return blobDef(uri, path, true);
		}
		return null;
	}

	@Override
	public Definition disassemble(URI uri, ContainerType type)
			throws URISyntaxException {

	    // sitePattern: "^.*?/(ContentServer|BlobServer|Satellite)(/([a-z0-9]+)(/(.*))?)$"

		// get path
		Definition result = null;
		String fullpath = uri.getPath();
		if (log.trace())
			log.debug("dissassembling %s", fullpath);

		// search if it is one of the configured sites
		Matcher match = sitePattern.matcher(fullpath);
		if (match.find()) {

			String path = match.group(2);
			String site = match.group(3);
			String subpath = match.group(4);
			if (log.trace())
				log.debug("found path=%s subpath=%s site=%s", path, subpath, site);


			//  check for blobs
			result = disassembleBlob(uri, path);
			if(result!=null) {
				if (log.debug())
					log.debug("*** blob found ***");
				return result;
			}

			//  check for statics
			result = disassembleStatic(uri, path);
			if(result!=null) {
				if (log.debug())
					log.debug("*** static found ***");
				return result;
			}
	
			// then check for site
			if (siteName.containsKey(site) && sitePrefix.containsKey(site)) {
				if (log.debug())
					log.debug("site=%s subpath=%s", site, subpath);
				// check if it is one of the blobs
				site = siteName.get(site);
				if (site != null) {
					if (log.debug())
						log.debug("*** asset found ***");
					return assetDef(uri, site, subpath, getDeviceParam(uri));
				}
			}
			log.warn("no prefix for site " + site);
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
			URI uri = buildBlobUri(prefix, where);
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
