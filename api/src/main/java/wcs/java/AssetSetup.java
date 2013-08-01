package wcs.java;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.fatwire.assetapi.data.MutableAssetData;

/**
 * Extend this class for installing assets
 * 
 * @author msciab
 * 
 */
public abstract class AssetSetup extends Asset {

	private final static SimpleDateFormat dateParser = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");

	private AssetSetup nextSetup = null;

	public AssetSetup(long id, String type, String subtype, String name) {
		super(id, type, subtype, name);
	}

	/**
	 * Return a list of expected attributes
	 * 
	 */
	public abstract List<String> getAttributes();

	/**
	 * Define asset data for this asset
	 * 
	 * @return
	 */
	public abstract void setData(MutableAssetData data);

	/**
	 * Chain another asset setup (or null if not chained asset)
	 * 
	 * @return
	 */
	public AssetSetup getNextSetup() {
		return nextSetup;
	}

	/**
	 * Set next asset setup
	 * 
	 * @param assetSetup
	 */
	public void setNextSetup(AssetSetup assetSetup) {
		nextSetup = assetSetup;
	}

	/**
	 * Create a date parsing the standard format
	 */
	public java.util.Date date(String date) {
		try {
			return dateParser.parse(date);
		} catch (ParseException e) {
			return new java.util.Date(0);
		}
	}

	/**
	 * Create a date parsing the standard format
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public java.util.List list(Object... objects) {
		List list = new LinkedList();
		for (Object obj : objects)
			list.add(obj);
		return list;
	}

	static class KV {
		String k;
		Object v;

		public KV(String k, Object v) {
			this.k = k;
			this.v = v;
		}
	}

	public KV kv(String k, Object v) {
		return new KV(k, v);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap map(KV... kvs) {
		HashMap m = new HashMap();
		for (KV kv : kvs)
			m.put(kv.k, kv.v);
		return m;
	}
	
	public String blob(String name, String value) { return ""; } 
	
	public String base64(String encoded) { return ""; }
}
