package wcs.java;

import java.util.List;

import wcs.java.util.Util;

import com.fatwire.assetapi.data.MutableAssetData;

/**
 * Extend this class for creating assets in setup
 * 
 * @author msciab
 * 
 */
public abstract class AssetSetup extends AssetBase {

	private AssetSetup nextSetup = null;

	public AssetSetup(String type, String subtype, String name) {
		super(type, subtype, name);
	}

	@Override
	public String getName() {
		return Util.normalizedName(getSite(), super.getName());
	}

	public void setTypeSubtype(String type, String subtype) {
		this.c = type;
		this.subtype = subtype;
		id = null;
	}

	public void setCid(long cid) {
		this.cid = cid;
		id = null;
	}

	/**
	 * Return a list of expected attributes
	 * 
	 */
	abstract List<String> getAttributes();

	/**
	 * Define asset data for this asset
	 * 
	 * @return
	 */
	abstract void setData(MutableAssetData data);

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

}
