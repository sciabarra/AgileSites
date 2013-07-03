package com.sciabarra.fatwire.serializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fatwire.assetapi.data.AssetData;
import com.fatwire.assetapi.data.AssetId;

public class AssetList implements Iterable<AssetData> {

	List<AssetData> assets = new ArrayList<AssetData>();

	public AssetList() {
	}

	public AssetList(Iterator<AssetData> it) {
		while (it.hasNext())
			add(it.next());
	}

	public void add(AssetData data) {
		// System.out.println("adding " + data.getAssetId());
		assets.add(data);
	}

	public List<AssetId> getIds() {
		List<AssetId> list = new ArrayList<AssetId>();
		for (AssetData data : assets)
			list.add(data.getAssetId());
		return list;
	}

	public List<AssetData> getAssets() {
		return assets;
	}

	public boolean contains(AssetId aid) {
		for (AssetId cur : getIds())
			if (cur.getId() == aid.getId()
					&& cur.getType().equals(aid.getType()))
				return true;
		return false;
	}

	public boolean contains(AssetData data) {
		return contains(data.getAssetId());
	}

	public String firstName() {
		if (assets.size() == 0)
			return null;
		return assets.get(0).getAttributeData("name").getData().toString();
	}

	public String firstType() {
		if (assets.size() == 0)
			return null;
		return assets.get(0).getAssetId().getType();
	}

	@Override
	public Iterator<AssetData> iterator() {
		return assets.iterator();
	}

	public String toString() {
		return getIds().toString();
	}
}