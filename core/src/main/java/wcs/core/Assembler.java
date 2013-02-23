package wcs.core;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fatwire.cs.core.uri.Definition;
import com.fatwire.cs.core.uri.Definition.ContainerType;
import com.fatwire.cs.core.uri.QueryAssembler;
import com.fatwire.cs.core.uri.Simple;

public class Assembler implements com.fatwire.cs.core.uri.Assembler {

	private QueryAssembler qa = new QueryAssembler();
	private Map<String, String> sitePrefix = new HashMap<String, String>();
	private Pattern sitePattern = Pattern.compile("^/([a-z0-9-]+)(/(.*))?$");
	private Pattern staticBlobs = null;
	private Pattern flexBlobs = null;

	@Override
	public void setProperties(Properties prp) {
		qa.setProperties(prp);
		staticBlobs = Pattern.compile(prp.getProperty("agilewcs.blob.static"));
		flexBlobs = Pattern.compile(prp.getProperty("agilewcs.blob.flex"));
		StringTokenizer st = new StringTokenizer(
				prp.getProperty("agilewcs.sites"), ",");
		while (st.hasMoreTokens()) {
			String site = WCS.normalizeSiteName(st.nextToken());
			sitePrefix.put(site, prp.getProperty("agilewcs." + site));
		}
		WCS.debug("staticBlobs=" + staticBlobs);
		WCS.debug("flexBlobs=" + flexBlobs);
		WCS.debug("sitePrefixes=" + sitePrefix);
	}

	// create an asset definition that will bring into the path
	private Definition assetDef(URI uri, String site, String path) {
		Simple def = new Simple(false, //
				Definition.SatelliteContext.SATELLITE_SERVER, //
				Definition.ContainerType.SERVLET, //
				uri.getScheme(), //
				uri.getAuthority(),//
				Definition.AppType.CONTENT_SERVER,//
				uri.getFragment());

		def.setQueryStringParameter("c", site);
		def.setQueryStringParameter("cid", path);
		def.setQueryStringParameter("pagename", "AAAgileRouter");

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
					} else {
						String k = URLDecoder.decode(pair.substring(0, pos),
								"UTF-8");
						String v = URLDecoder.decode(pair.substring(pos + 1),
								"UTF-8");
						def.setQueryStringParameter(k, v);
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

		def.setQueryStringParameter("blobcol", isSt ? "urldata" : "url");
		def.setQueryStringParameter("blobkey", isSt ? "name" : "id");
		def.setQueryStringParameter("blobwhere", subpath);
		def.setQueryStringParameter("blobtable", isSt ? "MungoBlobs" : "Static");
		addQueryString(def, uri.getQuery());
		return def;
	}

	/**
	 * Disassemble blob
	 * 
	 * @param uri
	 * @param site
	 * @param subpath
	 * @return
	 */
	private Definition disassembleBlob(URI uri, String site, String subpath) {
		Matcher mFlex = flexBlobs.matcher(subpath);
		if (mFlex.find()) {
			return blobDef(uri, subpath, false);
		}
		Matcher mStatic = staticBlobs.matcher(subpath);
		if (mStatic.find()) {
			return blobDef(uri, subpath, false);
		}
		return null;
	}

	@Override
	public Definition disassemble(URI uri, ContainerType type)
			throws URISyntaxException {

		Definition result = null;

		// get path
		String path = uri.getPath();
		Matcher match = sitePattern.matcher(path);

		// search if it is one of the sites
		if (match.find()) {
			String site = match.group(1);
			String subpath = match.group(3);
			if (sitePrefix.containsKey(site)) {
				// check if it is one of the blobs
				result = disassembleBlob(uri, site, subpath);
				if (result == null)
					result = assetDef(uri, site, subpath);
			}
		}
		return result;
	}

	@Override
	public URI assemble(Definition def) throws URISyntaxException {

		String site = def.getParameter("c");
		String suffix = def.getParameter("cid");
		String mode = def.getParameter("rendermode");
		String prefix = sitePrefix.get(site);

		if (mode != null && mode.startsWith("preview"))
			return qa.assemble(def);

		if (prefix != null)
			return suffix == null ? new URI(prefix) : new URI(prefix + suffix);

		return qa.assemble(def);

	}
}
