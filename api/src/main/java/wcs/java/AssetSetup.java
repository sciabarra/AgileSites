package wcs.java;

import wcs.core.Id;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.fatwire.assetapi.data.AssetId;
import com.fatwire.assetapi.data.AttributeData;
import com.fatwire.assetapi.data.AttributeDataImpl;
import com.fatwire.assetapi.data.BlobObject;
import com.fatwire.assetapi.data.BlobObjectImpl;
import com.fatwire.assetapi.data.MutableAssetData;
import com.fatwire.assetapi.def.AttributeTypeEnum;
import com.openmarket.xcelerate.asset.AssetIdImpl;
import com.openmarket.xcelerate.asset.AttributeDefImpl;

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
	 * Return a list of references assets
	 * 
	 */
	public abstract List<Id> getReferences();

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

	static public class KV {
		AttributeTypeEnum type;
		String name;
		Object value;

		public KV(String k, AttributeTypeEnum t, Object v) {
			this.type = t;
			this.name = k;
			this.value = v;
		}
	}

	public KV kv(String k, AttributeTypeEnum t, Object v) {
		return new KV(k, t, v);
	}

	@SuppressWarnings({ "rawtypes"})
	public HashMap map(KV... kvs) {
		HashMap<String, AttributeData> m = new HashMap<String, AttributeData>();
		for (KV kv : kvs) 
			m.put(kv.name, new AttributeDataImpl(new AttributeDefImpl(kv.name,
					kv.type), kv.name, kv.type, kv.value));
		return m;
	}

	public BlobObject blob(String filename, String value) {
		File file = new File(filename);
		return new BlobObjectImpl(//
				file.getParent().toString(), //
				file.getName(), //
				value.getBytes());
	}

	public String base64(String encoded) {
		return "";
	}

	public AssetId ref(String c, long cid) {
		return new AssetIdImpl(c, cid);
	}

	public Id id(String c, long cid) {
		return new Id(c, cid);
	}

	

}
