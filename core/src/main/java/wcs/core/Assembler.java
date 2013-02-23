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
	private Pattern sitePattern = Pattern
			.compile("^.*?/(ContentServer|BlobServer|Satellite)/([a-z0-9]+)(/(.*))?$");
	private Pattern staticBlobs = null;
	private Pattern flexBlobs = null;

	@Override
	public void setProperties(Properties prp) {
		// WCS.debug("Satellite.setProperties=" + prp);
		try {
			staticBlobs = Pattern.compile(prp
					.getProperty("agilewcs.blob.static"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		WCS.debug("staticBlobs=" + staticBlobs);
		try {
			flexBlobs = Pattern.compile(prp.getProperty("agilewcs.blob.flex"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		WCS.debug("flexBlobs=" + flexBlobs);
		try {
			StringTokenizer st = new StringTokenizer(
					prp.getProperty("agilewcs.sites"), ",");
			while (st.hasMoreTokens()) {
				String site = WCS.normalizeSiteName(st.nextToken());
				String prefix = prp.getProperty("agilewcs." + site);
				if (prefix != null)
					sitePrefix.put(site, prefix);
				else
					WCS.debug("no site prefix for " + site);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		WCS.debug("sitePrefixes=" + sitePrefix);

		try {
			qa.setProperties(prp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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

		WCS.debug("c=" + site);
		WCS.debug("cid=" + path);
		WCS.debug("pagename=" + "AAAgileRouter");

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
						WCS.debug("qs: " + k);
					} else {
						String k = URLDecoder.decode(pair.substring(0, pos),
								"UTF-8");
						String v = URLDecoder.decode(pair.substring(pos + 1),
								"UTF-8");
						def.setQueryStringParameter(k, v);
						WCS.debug("qs: " + k + "=" + v);
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
		String blobkey = isSt ? "name" : "id";
		String blobwhere = subpath;
		String blobtable = isSt ? "MungoBlobs" : "Static";

		def.setQueryStringParameter("blobcol", blobcol);
		def.setQueryStringParameter("blobkey", blobkey);
		def.setQueryStringParameter("blobwhere", blobwhere);
		def.setQueryStringParameter("blobtable", blobtable);

		WCS.debug("blobcol=" + blobcol);
		WCS.debug("blobkey=" + blobkey);
		WCS.debug("blobwhere=" + blobwhere);
		WCS.debug("blobtable=" + blobtable);

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
		if (flexBlobs != null) {
			Matcher mFlex = flexBlobs.matcher(subpath);
			if (mFlex.find()) {
				WCS.debug("flexBlob subpath=" + subpath);
				return blobDef(uri, subpath, false);
			}
		}
		if (staticBlobs != null) {
			Matcher mStatic = staticBlobs.matcher(subpath);
			if (mStatic.find()) {
				WCS.debug("staticBlob subpath=" + subpath);
				return blobDef(uri, subpath, true);
			}
		}
		return null;
	}

	@Override
	public Definition disassemble(URI uri, ContainerType type)
			throws URISyntaxException {

		WCS.debug("dissassembling " + uri);
		WCS.debug("query:  ?" + uri.getQuery());
		// WCS.debug("fragment: #" + uri.getFragment());

		Definition result = null;

		// get path
		String path = uri.getPath();
		Matcher match = sitePattern.matcher(path);

		// search if it is one of the sites
		if (match.find()) {
			String site = match.group(2);
			String subpath = match.group(4);
			WCS.debug("site=" + site + " subpath=" + subpath);
			if (sitePrefix.containsKey(site)) {
				// check if it is one of the blobs
				result = disassembleBlob(uri, site, subpath);

				if (result == null) {
					WCS.debug("*** asset found");
					return assetDef(uri, site, subpath);
				} else {
					WCS.debug("*** blob found");
					return result;
				}

			} else {
				WCS.debug("no known site " + site);
			}
		} else {
			WCS.debug("no site ");
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
