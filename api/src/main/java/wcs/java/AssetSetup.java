package wcs.java;

import java.util.List;

import com.fatwire.assetapi.data.MutableAssetData;

/**
 * Extend this class for installing assets
 * 
 * @author msciab
 * 
 */
public abstract class AssetSetup extends Asset {
	
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
	 * @param assetSetup
	 */
	public void setNextSetup(AssetSetup assetSetup) {
		nextSetup = assetSetup; 
	}
}
